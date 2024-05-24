package io.github.linpeilie.processor.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.processor.ContextConstants;
import io.github.linpeilie.processor.metadata.AutoMapperMetadata;
import io.github.linpeilie.processor.metadata.AutoMappingMetadata;
import io.github.linpeilie.utils.CollectionUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import sun.reflect.annotation.AnnotationType;

import static javax.tools.Diagnostic.Kind.ERROR;

public class AutoMapperGenerator {

    public static final String CONVERT_METHOD_NAME = "convert";

    private static final Map<String, Integer> AUTO_MAPPER_INDEX = new HashMap<>();

    public void write(AutoMapperMetadata metadata, ProcessingEnvironment processingEnv) {
        String mapperPackage = metadata.mapperPackage();

        /*
            当前处理方式，本地使用 IDEA 开发时，当修改 Source/Target 类时，可能还会出现类名冲突的问题，
            当出现该问题时，需要执行 clean 把之前构建的类清掉。
         */
        String mapperName = metadata.mapperName();
        // 同名类时，增加后缀
        Integer index = AUTO_MAPPER_INDEX.getOrDefault(mapperName, 0);
        if (index > 0) {
            mapperName = mapperName + "__" + index;
        }
        AUTO_MAPPER_INDEX.put(metadata.mapperName(), ++index);

        try (final Writer writer = processingEnv.getFiler()
            .createSourceFile(mapperPackage + "." + mapperName)
            .openWriter()) {
            JavaFile.builder(metadata.mapperPackage(), createTypeSpec(processingEnv, metadata, mapperName))
                .build()
                .writeTo(writer);
        } catch (IOException e) {
            processingEnv.getMessager()
                .printMessage(ERROR,
                    "Error while opening " + metadata.mapperName() + " output file: " + e.getMessage());
        }
    }

    private TypeSpec createTypeSpec(ProcessingEnvironment processingEnv,
        AutoMapperMetadata metadata,
        String mapperName) {
        ParameterizedTypeName converterName =
            ParameterizedTypeName.get(metadata.getSuperClass(), metadata.getSuperGenerics());

        final ClassName targetClassName = metadata.getTargetClassName();

        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(mapperName)
            .addSuperinterface(converterName)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(buildGeneratedMapperAnnotationSpec(metadata));

        ParameterSpec source = ParameterSpec.builder(metadata.getSourceClassName(), "source").build();
        ParameterSpec target = ParameterSpec.builder(targetClassName, "target")
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.mapstruct", "MappingTarget")).build())
            .build();
        ParameterSpec context =
            ParameterSpec.builder(ClassName.get("io.github.linpeilie", "CycleAvoidingMappingContext"), "context")
                .addAnnotation(ClassName.get("org.mapstruct", "Context"))
                .build();
        if (metadata.getFieldMappingList() != null && !metadata.getFieldMappingList().isEmpty()) {
            builder.addMethod(addConvertMethodSpec(
                metadata.isCycleAvoiding() ? CollectionUtils.newArrayList(source, context) : Collections.singletonList(
                    source),
                metadata.getFieldMappingList(),
                targetClassName,
                CONVERT_METHOD_NAME));
        }

        boolean targetIsImmutable = classIsImmutable(processingEnv, targetClassName);
        if (targetIsImmutable) {
            builder.addMethod(
                addEmptyConvertMethodForImmutableEntity(
                    metadata.isCycleAvoiding() ? CollectionUtils.newArrayList(source, target,
                        context) : CollectionUtils.newArrayList(source, target),
                    targetClassName,
                    CONVERT_METHOD_NAME));
        } else if (metadata.getFieldMappingList() != null && !metadata.getFieldMappingList().isEmpty()) {
            builder.addMethod(addConvertMethodSpec(
                metadata.isCycleAvoiding() ? CollectionUtils.newArrayList(source, target,
                    context) : CollectionUtils.newArrayList(source, target),
                metadata.getFieldMappingList(),
                targetClassName,
                CONVERT_METHOD_NAME));
        }

        return builder.build();
    }

    private MethodSpec addEmptyConvertMethodForImmutableEntity(List<ParameterSpec> parameterSpecs,
        ClassName targetClassName,
        String methodName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .addParameters(parameterSpecs)
            .returns(targetClassName)
            .addCode("return target;");
        return builder.build();
    }

    private boolean classIsImmutable(ProcessingEnvironment processingEnv, ClassName className) {
        final TypeElement targetElement = processingEnv.getElementUtils()
            .getTypeElement(className.reflectionName().replaceAll("\\$", "."));
        final List<? extends AnnotationMirror> annotationMirrors = targetElement.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (annotationMirror.getAnnotationType().asElement().getSimpleName().contentEquals("Immutable")) {
                return true;
            }
        }
        return false;
    }

    private MethodSpec addConvertMethodSpec(List<ParameterSpec> parameterSpecs,
        List<AutoMappingMetadata> autoMappingMetadataList,
        ClassName target, String methodName) {
        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName)
            .addParameters(parameterSpecs)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(ClassName.get(ContextConstants.DoIgnore.packageName, ContextConstants.DoIgnore.className))
            .returns(target);
        if (CollectionUtils.isNotEmpty(autoMappingMetadataList)) {
            methodSpecBuilder.addAnnotations(buildMappingAnnotations(autoMappingMetadataList));
        }
        return methodSpecBuilder.build();
    }

    private List<AnnotationSpec> buildMappingAnnotations(final List<AutoMappingMetadata> autoMappingMetadataList) {
        return autoMappingMetadataList.stream().map(autoMappingMetadata -> {
            final AnnotationSpec.Builder builder = AnnotationSpec.builder(ClassName.get("org.mapstruct", "Mapping"))
                .addMember("target", CodeBlock.builder().add("$S", autoMappingMetadata.getTarget()).build());
            if (autoMappingMetadata.getIgnore() != null) {
                builder.addMember("ignore",
                    CodeBlock.builder().add(String.valueOf(autoMappingMetadata.getIgnore())).build());
            }
            if (autoMappingMetadata.getDateFormat() != null) {
                builder.addMember("dateFormat",
                    CodeBlock.builder().add("$S", autoMappingMetadata.getDateFormat()).build());
            }
            if (autoMappingMetadata.getNumberFormat() != null) {
                builder.addMember("numberFormat",
                    CodeBlock.builder().add("$S", autoMappingMetadata.getNumberFormat()).build());
            }
            if (autoMappingMetadata.getDefaultValue() != null) {
                builder.addMember("defaultValue",
                    CodeBlock.builder().add("$S", autoMappingMetadata.getDefaultValue()).build());
            }
            if (autoMappingMetadata.getExpression() != null) {
                builder.addMember("expression",
                    CodeBlock.builder().add("$S", autoMappingMetadata.getExpression()).build());
            } else if (autoMappingMetadata.getConstant() != null) {
                builder.addMember("constant", CodeBlock.builder().add("$S", autoMappingMetadata.getConstant()).build());
            } else {
                builder.addMember("source", CodeBlock.builder().add("$S", autoMappingMetadata.getSource()).build());
            }
            if (autoMappingMetadata.getDefaultExpression() != null) {
                builder.addMember("defaultExpression",
                    CodeBlock.builder().add("$S", autoMappingMetadata.getDefaultExpression()).build());
            }
            if (autoMappingMetadata.getConditionExpression() != null) {
                builder.addMember("conditionExpression",
                    CodeBlock.builder().add("$S", autoMappingMetadata.getConditionExpression()).build());
            }
            if (autoMappingMetadata.getQualifiedByName() != null) {
                builder.addMember("qualifiedByName", CodeBlock.builder()
                    .add("$L",
                        "{" + CollectionUtils.join(autoMappingMetadata.getQualifiedByName(), ",", "\"", "\"") + "}")
                    .build());
            }
            if (autoMappingMetadata.getConditionQualifiedByName() != null) {
                builder.addMember("conditionQualifiedByName", CodeBlock.builder()
                    .add("$L",
                        "{" + CollectionUtils.join(autoMappingMetadata.getConditionQualifiedByName(), ",", "\"", "\"") +
                        "}")
                    .build());
            }
            if (autoMappingMetadata.getDependsOn() != null) {
                builder.addMember("dependsOn", CodeBlock.builder().add("$L",
                    "{" + CollectionUtils.join(autoMappingMetadata.getDependsOn(), ",", "\"", "\"") + "}").build());
            }
            if (CollectionUtils.isNotEmpty(autoMappingMetadata.getQualifiedBy())) {
                CodeBlock.Builder qualifiedCodeBlockBuilder = CodeBlock.builder().add("{");
                for (int i = 0; i < autoMappingMetadata.getQualifiedBy().size(); i++) {
                    qualifiedCodeBlockBuilder.add("$T.class", autoMappingMetadata.getQualifiedBy().get(i));
                    if (i != autoMappingMetadata.getQualifiedBy().size() - 1) {
                        qualifiedCodeBlockBuilder.add(",");
                    }
                }
                qualifiedCodeBlockBuilder.add("}");
                builder.addMember("qualifiedBy", qualifiedCodeBlockBuilder.build());
            }
            if (autoMappingMetadata.getNullValueCheckStrategy() != null) {
                CodeBlock nullValueCheckStrategyCodeBlock = CodeBlock.builder()
                    .add("$T.$L", ClassName.get(ContextConstants.NullValueCheckStrategy.packageName,
                            ContextConstants.NullValueCheckStrategy.className),
                        autoMappingMetadata.getNullValueCheckStrategy()).build();
                builder.addMember("nullValueCheckStrategy", nullValueCheckStrategyCodeBlock);
            }
            if (autoMappingMetadata.getNullValuePropertyMappingStrategy() != null) {
                CodeBlock nullValuePropertyMappingStrategy = CodeBlock.builder()
                    .add("$T.$L", ClassName.get(ContextConstants.NullValuePropertyMappingStrategy.packageName,
                            ContextConstants.NullValuePropertyMappingStrategy.className),
                        autoMappingMetadata.getNullValuePropertyMappingStrategy()).build();
                builder.addMember("nullValuePropertyMappingStrategy", nullValuePropertyMappingStrategy);
            }
            if (autoMappingMetadata.getMappingControl() != null) {
                builder.addMember("mappingControl",
                    CodeBlock.builder().add("$T.class", autoMappingMetadata.getMappingControl()).build());
            }

            return builder.build();
        }).collect(Collectors.toList());
    }

    private AnnotationSpec buildGeneratedMapperAnnotationSpec(AutoMapperMetadata metadata) {
        Map<String, Object> autoMapperDefaultValueMap = AnnotationType.getInstance(AutoMapper.class).memberDefaults();

        List<ClassName> usesClassNameList =
            Optional.ofNullable(metadata.getUsesClassNameList()).orElse(new ArrayList<>());

        List<ClassName> importsClassNameList =
            Optional.ofNullable(metadata.getImportsClassNameList()).orElse(new ArrayList<>());

        // config
        CodeBlock configCodeBlock = CodeBlock.builder()
            .add("$T.class", metadata.getMapstructConfigClass())
            .build();

        // uses
        CodeBlock.Builder usesCodeBuilder = CodeBlock.builder().add("{");
        for (int i = 0; i < usesClassNameList.size(); i++) {
            usesCodeBuilder.add("$T.class", usesClassNameList.get(i));
            if (i != usesClassNameList.size() - 1) {
                usesCodeBuilder.add(",");
            }
        }
        CodeBlock usesCodeBlock = usesCodeBuilder.add("}").build();

        // imports
        final CodeBlock.Builder importsCodeBuilder = CodeBlock.builder().add("{");
        for (int i = 0; i < importsClassNameList.size(); i++) {
            importsCodeBuilder.add("$T.class", importsClassNameList.get(i));
            if (i != importsClassNameList.size() - 1) {
                importsCodeBuilder.add(",");
            }
        }
        final CodeBlock importsCodeBlock = importsCodeBuilder.add("}").build();

        AnnotationSpec.Builder builder =
            AnnotationSpec.builder(
                    ClassName.get(ContextConstants.Mapper.packageName, ContextConstants.Mapper.className))
                .addMember("config", configCodeBlock)
                .addMember("uses", usesCodeBlock)
                .addMember("imports", importsCodeBlock);

        // unmappedSourcePolicy
        if (metadata.getUnmappedSourcePolicy() != null
            && !autoMapperDefaultValueMap.get("unmappedSourcePolicy").equals(metadata.getUnmappedSourcePolicy())) {
            builder.addMember("unmappedSourcePolicy", CodeBlock.builder()
                .add("$T.$L", ClassName.get(ContextConstants.ReportingPolicy.packageName,
                        ContextConstants.ReportingPolicy.className),
                    metadata.getUnmappedSourcePolicy())
                .build());
        }

        // unmappedTargetPolicy
        if (metadata.getUnmappedTargetPolicy() != null
            && !autoMapperDefaultValueMap.get("unmappedTargetPolicy").equals(metadata.getUnmappedTargetPolicy())) {
            builder.addMember("unmappedTargetPolicy", CodeBlock.builder()
                .add("$T.$L",
                    ClassName.get(ContextConstants.ReportingPolicy.packageName,
                        ContextConstants.ReportingPolicy.className),
                    metadata.getUnmappedTargetPolicy())
                .build());
        }

        // typeConversionPolicy
        if (metadata.getTypeConversionPolicy() != null &&
            !autoMapperDefaultValueMap.get("typeConversionPolicy").equals(metadata.getTypeConversionPolicy())) {
            builder.addMember("typeConversionPolicy", CodeBlock.builder()
                .add("$T.$L",
                    ClassName.get(ContextConstants.ReportingPolicy.packageName,
                        ContextConstants.ReportingPolicy.className),
                    metadata.getTypeConversionPolicy())
                .build());
        }

        // collectionMappingStrategy
        if (metadata.getCollectionMappingStrategy() != null &&
            !autoMapperDefaultValueMap.get("collectionMappingStrategy")
                .equals(metadata.getCollectionMappingStrategy())) {
            builder.addMember("collectionMappingStrategy", CodeBlock.builder()
                .add("$T.$L",
                    ClassName.get(ContextConstants.CollectionMappingStrategy.packageName,
                        ContextConstants.CollectionMappingStrategy.className),
                    metadata.getCollectionMappingStrategy())
                .build());
        }

        // nullValueMappingStrategy
        if (metadata.getNullValueMappingStrategy() != null &&
            !autoMapperDefaultValueMap.get("nullValueMappingStrategy").equals(metadata.getNullValueMappingStrategy())) {
            builder.addMember("nullValueMappingStrategy", CodeBlock.builder()
                .add("$T.$L",
                    ClassName.get(ContextConstants.NullValueMappingStrategy.packageName,
                        ContextConstants.NullValueMappingStrategy.className),
                    metadata.getNullValueMappingStrategy())
                .build());
        }

        // nullValueIterableMappingStrategy
        if (metadata.getNullValueIterableMappingStrategy() != null &&
            !autoMapperDefaultValueMap.get("nullValueIterableMappingStrategy")
                .equals(metadata.getNullValueIterableMappingStrategy())) {
            builder.addMember("nullValueIterableMappingStrategy", CodeBlock.builder()
                .add("$T.$L",
                    ClassName.get(ContextConstants.NullValueMappingStrategy.packageName,
                        ContextConstants.NullValueMappingStrategy.className),
                    metadata.getNullValueIterableMappingStrategy())
                .build());
        }

        // nullValuePropertyMappingStrategy
        if (metadata.getNullValuePropertyMappingStrategy() != null &&
            !autoMapperDefaultValueMap.get("nullValuePropertyMappingStrategy")
                .equals(metadata.getNullValuePropertyMappingStrategy())) {
            builder.addMember("nullValuePropertyMappingStrategy", CodeBlock.builder()
                .add("$T.$L",
                    ClassName.get(ContextConstants.NullValuePropertyMappingStrategy.packageName,
                        ContextConstants.NullValuePropertyMappingStrategy.className),
                    metadata.getNullValuePropertyMappingStrategy())
                .build());
        }

        // nullValueCheckStrategy
        if (metadata.getNullValueCheckStrategy() != null &&
            !autoMapperDefaultValueMap.get("nullValueCheckStrategy").equals(metadata.getNullValueCheckStrategy())) {
            builder.addMember("nullValueCheckStrategy", CodeBlock.builder()
                .add("$T.$L",
                    ClassName.get(ContextConstants.NullValueCheckStrategy.packageName,
                        ContextConstants.NullValueCheckStrategy.className),
                    metadata.getNullValueCheckStrategy())
                .build());
        }

        // mappingControl
        if (metadata.getMappingControl() != null &&
            !((Class<?>) autoMapperDefaultValueMap.get("mappingControl")).getName()
                .equals(metadata.getMappingControl().reflectionName())) {
            builder.addMember("mappingControl",
                CodeBlock.builder().add("$T.class", metadata.getMappingControl()).build());
        }

        return builder.build();
    }

}
