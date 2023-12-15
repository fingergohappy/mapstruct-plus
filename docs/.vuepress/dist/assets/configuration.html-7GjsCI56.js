import{_ as n,o as a,c as s,e as t}from"./app-AKEP8bab.js";const e={},p=t(`<p>MapStructPlus provides multiple configuration items to specify some behavior when the conversion interface is generated.</p><h2 id="how-to-use-it" tabindex="-1">How to use it</h2><p>In the module that needs to be configured, create a new configuration class an annotate it with <code>@MapperConfig</code> annotation.</p><p>In a module, there can noly be one class with this annotation.</p><p>Also, note that <strong>the configuration classes must be placed in the module to be effective</strong>.</p><p>eg：</p><div class="language-java line-numbers-mode" data-ext="java"><pre class="language-java"><code><span class="token annotation punctuation">@MapperConfig</span><span class="token punctuation">(</span>adapterClassName <span class="token operator">=</span> <span class="token string">&quot;DemoConvertMapperAdapter&quot;</span><span class="token punctuation">,</span>
    adapterPackage <span class="token operator">=</span> <span class="token string">&quot;io.github.linpeilie.adapter&quot;</span><span class="token punctuation">,</span>
    mapAdapterClassName <span class="token operator">=</span> <span class="token string">&quot;DemoMapConvertMapperAdapter&quot;</span><span class="token punctuation">)</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">MapStructPlusConfiguration</span> <span class="token punctuation">{</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>In addition, the configuration property supports <strong>adding compilation parameters</strong> to the compiler in the form of <code>-Akey=value</code>.</p><p>For example, when using Maven, you can use the <code>compilerArgs</code> property in the <code>maven-compiler-plugin</code> plugin configuration to configure delivery, for example:</p><p><strong>And configuration in this way takes precedence</strong>, that is, when the mode and configuration class exist together, the property configured in this way takes precedence. This feature is supported from <code>1.3.0</code>.</p><p>eg:</p><div class="language-xml line-numbers-mode" data-ext="xml"><pre class="language-xml"><code><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>build</span><span class="token punctuation">&gt;</span></span>
  <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>plugins</span><span class="token punctuation">&gt;</span></span>
    <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>plugin</span><span class="token punctuation">&gt;</span></span>
      <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>groupId</span><span class="token punctuation">&gt;</span></span>org.apache.maven.plugins<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>groupId</span><span class="token punctuation">&gt;</span></span>
      <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>artifactId</span><span class="token punctuation">&gt;</span></span>maven-compiler-plugin<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>artifactId</span><span class="token punctuation">&gt;</span></span>
      <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>version</span><span class="token punctuation">&gt;</span></span>3.8.0<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>version</span><span class="token punctuation">&gt;</span></span>
      <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>configuration</span><span class="token punctuation">&gt;</span></span>
        <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>source</span><span class="token punctuation">&gt;</span></span>\${maven.compiler.source}<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>source</span><span class="token punctuation">&gt;</span></span>
        <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>target</span><span class="token punctuation">&gt;</span></span>\${maven.compiler.target}<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>target</span><span class="token punctuation">&gt;</span></span>
        <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>annotationProcessorPaths</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>path</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>groupId</span><span class="token punctuation">&gt;</span></span>org.projectlombok<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>groupId</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>artifactId</span><span class="token punctuation">&gt;</span></span>lombok<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>artifactId</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>version</span><span class="token punctuation">&gt;</span></span>\${lombok.version}<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>version</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>path</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>path</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>groupId</span><span class="token punctuation">&gt;</span></span>io.github.linpeilie<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>groupId</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>artifactId</span><span class="token punctuation">&gt;</span></span>mapstruct-plus-processor<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>artifactId</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>version</span><span class="token punctuation">&gt;</span></span>\${mapstruct-plus.version}<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>version</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>path</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>path</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>groupId</span><span class="token punctuation">&gt;</span></span>org.projectlombok<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>groupId</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>artifactId</span><span class="token punctuation">&gt;</span></span>lombok-mapstruct-binding<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>artifactId</span><span class="token punctuation">&gt;</span></span>
            <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>version</span><span class="token punctuation">&gt;</span></span>0.2.0<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>version</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>path</span><span class="token punctuation">&gt;</span></span>
        <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>annotationProcessorPaths</span><span class="token punctuation">&gt;</span></span>
        <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>compilerArgs</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>arg</span><span class="token punctuation">&gt;</span></span>-Amapstruct.plus.adapterClassName=DemoConvertMapperAdapter<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>arg</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>arg</span><span class="token punctuation">&gt;</span></span>-Amapstruct.plus.adapterPackage=io.github.linpeilie.adapter<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>arg</span><span class="token punctuation">&gt;</span></span>
          <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>arg</span><span class="token punctuation">&gt;</span></span>-Amapstruct.plus.mapAdapterClassName=DemoMapConvertMapperAdapter<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>arg</span><span class="token punctuation">&gt;</span></span>
        <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>compilerArgs</span><span class="token punctuation">&gt;</span></span>
      <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>configuration</span><span class="token punctuation">&gt;</span></span>
    <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>plugin</span><span class="token punctuation">&gt;</span></span>
  <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>plugins</span><span class="token punctuation">&gt;</span></span>
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>build</span><span class="token punctuation">&gt;</span></span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="configuration-item" tabindex="-1">Configuration Item</h2><h3 id="mapperpackage" tabindex="-1">mapperPackage</h3><ul><li><strong>Description</strong>：The package name of the generated Mapper transformation interfaces</li><li><strong>Type</strong>：<code>String</code></li><li><strong>Default</strong>：The default is under the same package as the class to be converted.</li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.mapperPackage</code></li></ul><h3 id="unmappedsourcepolicy" tabindex="-1">unmappedSourcePolicy</h3><ul><li><strong>Description</strong>：Policy when there is no corresponding attribute in the source class</li><li><strong>Type</strong>：<code>ReportingPolicy</code></li><li><strong>Optional</strong>： <ul><li><code>IGNORE</code>：ignore</li><li><code>WARN</code>：print warning log</li><li><code>ERROR</code>：throw exception</li></ul></li><li><strong>Default</strong>：<code>IGNORE</code></li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.unmappedSourcePolicy</code></li></ul><h3 id="unmappedtargetpolicy" tabindex="-1">unmappedTargetPolicy</h3><ul><li><strong>Description</strong>：Policy when there is no corresponding attribute in the target class</li><li><strong>Type</strong>：<code>ReportingPolicy</code></li><li><strong>Optional</strong>： <ul><li><code>IGNORE</code>：ignore</li><li><code>WARN</code>：print warning log</li><li><code>ERROR</code>：throw exception</li></ul></li><li><strong>Default</strong>：<code>IGNORE</code></li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.unmappedTargetPolicy</code></li></ul><h3 id="nullvaluemappingstrategy" tabindex="-1">nullValueMappingStrategy</h3><ul><li><strong>Description</strong>：Null object handing policy</li><li><strong>Type</strong>：<code>NullValueMappingStrategy</code></li><li><strong>Optional</strong>： <ul><li><code>RETURN_NULL</code>：return null</li><li><code>RETURN_DEFAULT</code>：return default value</li></ul></li><li><strong>Default</strong>：<code>RETURN_NULL</code></li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.nullValueMappingStrategy</code></li></ul><h3 id="nullvaluepropertymappingstrategy" tabindex="-1">nullValuePropertyMappingStrategy</h3><ul><li><strong>Description</strong>：Policy to deal with when the property value is <code>null</code></li><li><strong>Type</strong>：<code>NullValuePropertyMappingStrategy</code></li><li><strong>Optional</strong>： <ul><li><code>SET_TO_NULL</code>：setting is null</li><li><code>SET_TO_DEFAULT</code>：setting is default value</li><li><code>IGNORE</code>：ignore</li></ul></li><li><strong>Default</strong>：<code>SET_TO_NULL</code></li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.nullValuePropertyMappingStrategy</code></li></ul><h3 id="builder" tabindex="-1">builder</h3><ul><li><strong>Description</strong>：Constructor mode configuration, MapStruct loses the parent class property when used with Lombok&#39;s builder, so the default constructor mode is turned off.</li><li><strong>Type</strong>：<code>Builder</code></li><li><strong>Optional</strong>： <ul><li><code>buildMethod</code>：The constructor creates the constructor when the type is to be build</li><li><code>disableBuilder</code>：Open/Close the constructor, and if closed, use only regular getters/setters</li></ul></li><li><strong>Default</strong>： <ul><li><code>buildMethod</code>：<code>build</code></li><li><code>disableBuilder</code>：<code>true</code></li></ul></li><li><strong>Compile Parameter</strong>： <ul><li><code>-Amapstruct.plus.builder.buildMethod</code></li><li><code>-Amapstruct.plus.builder.disableBuilder</code></li></ul></li></ul><h3 id="adapterpackage" tabindex="-1">adapterPackage</h3><blockquote><p>since <code>1.2.3</code></p></blockquote><ul><li><strong>Description</strong>：The package name of ConvertAdapterClass and MapConvertMapperAdapter</li><li><strong>Type</strong>：<code>String</code></li><li><strong>Default</strong>：io.github.linpeilie</li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.adapterPackage</code></li></ul><h3 id="adapterclassname" tabindex="-1">adapterClassName</h3><blockquote><p>since <code>1.2.3</code></p></blockquote><ul><li><strong>Description</strong>：the class name of ConvertAdapterClass</li><li><strong>Type</strong>：<code>String</code></li><li><strong>Default</strong>：ConvertMapperAdapter</li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.adapterClassName</code></li></ul><h3 id="mapadapterclassname" tabindex="-1">mapAdapterClassName</h3><blockquote><p>since <code>1.2.3</code></p></blockquote><ul><li><strong>Description</strong>：the class name of MapConvertMapperAdapter</li><li><strong>Type</strong>：<code>String</code></li><li><strong>Default</strong>：MapConvertMapperAdapter</li><li><strong>Compile Parameter</strong>：<code>-Amapstruct.plus.mapAdapterClassName</code></li></ul>`,34),o=[p];function l(c,i){return a(),s("div",null,o)}const r=n(e,[["render",l],["__file","configuration.html.vue"]]);export{r as default};
