package io.github.linpl.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.github.linpl.annotations.AutoMapper;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;

import static javax.tools.Diagnostic.Kind.ERROR;

public class MapperConfigGenerator {

    public void write(ProcessingEnvironment processingEnv) {
        try (final Writer writer = processingEnv.getFiler()
            .createSourceFile(AutoMapperProperties.getMapperPackage() + "." + AutoMapperProperties.getConfigClassName())
            .openWriter()) {
            JavaFile.builder(AutoMapperProperties.getMapperPackage(), createConfigTypeSpec()).build().writeTo(writer);
        } catch (IOException e) {
            processingEnv.getMessager()
                .printMessage(ERROR,
                    "Error while opening " + AutoMapperProperties.getConfigClassName() + " output file: " +
                    e.getMessage());
        }
    }

    private TypeSpec createConfigTypeSpec() {
        return TypeSpec.interfaceBuilder(AutoMapperProperties.getConfigClassName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(buildMapperConfigAnnotationSpec())
            .build();
    }

    private AnnotationSpec buildMapperConfigAnnotationSpec() {
        CodeBlock.Builder usesCodeBuilder = CodeBlock.builder().add("{");
        usesCodeBuilder.add("$T.class",
            ClassName.get(AutoMapperProperties.getAdapterPackage(), AutoMapperProperties.getAdapterClassName()));
        CodeBlock usesCodeBlock = usesCodeBuilder.add("}").build();
        return AnnotationSpec.builder(ClassName.get("org.mapstruct", "MapperConfig"))
            .addMember("componentModel", "\"spring\"")
            .addMember("uses", usesCodeBlock)
            .build();
    }

}
