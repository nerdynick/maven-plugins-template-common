package com.nerdynick.maven.plugins.template;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

public abstract class AbstractTemplateMojo extends AbstractMojo {
	@Parameter(name="project", defaultValue="${project}")
	protected MavenProject project;
	
	@Parameter(name="properties", required=false)
	protected Properties props;
	
	@Parameter(defaultValue = "${project.build.sourceEncoding}")
	protected String encoding;
	
	@Parameter(name="templates", required=true)
	protected List<TemplateConfig> templates;
	
	protected Map<String, String> getProjectProps(){
		Properties props = project.getProperties();
		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		
		props.forEach((k,v)->{
			builder.put(k.toString().replaceAll("\\.", "-"), v.toString());
		});
		return builder.build();
	}
	
	protected Map<String, String> getProjectInfoProps(){
		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		builder.put("project-artifactId", project.getArtifactId());
		builder.put("project-groupId", project.getGroupId());
		builder.put("project-name", project.getName());
		builder.put("project-description", Strings.nullToEmpty(project.getDescription()));
		return builder.build();
	}
	
	protected Charset getCharset() {
		Charset charset;
		if (encoding == null || encoding.isEmpty()) {
            getLog().warn("Using platform encoding " + Charset.defaultCharset());
            charset = Charset.defaultCharset();
        } else {
            charset = Charset.forName(encoding);
        }
		
		return charset;
	}
	

}
