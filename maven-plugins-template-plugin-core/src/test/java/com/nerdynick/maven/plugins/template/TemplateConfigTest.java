package com.nerdynick.maven.plugins.template;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.FileSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public class TemplateConfigTest {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateConfigTest.class);
	
	@Test
	public void testGetTemplates_WithOutputExt() throws IOException {
		File outputDir = Files.createTempDir();
		outputDir.deleteOnExit();
		Path outputDirPath = outputDir.toPath();
		
		File inputDir = Files.createTempDir();
		inputDir.deleteOnExit();
		Path inputDirPath = inputDir.toPath();
		
		File inputFile1 = inputDirPath.resolve("tempfile_1.tmp").toFile();
		inputFile1.createNewFile();
		inputFile1.deleteOnExit();
		
		FileSet inputSet = new FileSet();
		inputSet.setDirectory(inputDir.getAbsolutePath());
		
		TemplateConfig config = new TemplateConfig();
		config.setInputDir(inputSet);
		config.setOutputDir(outputDir);
		config.setOutputExt("txt");
		
		LOG.debug("Template Config: {}", config);
		
		List<Template> templates = config.getTemplates();
		
		LOG.debug("Templates: {}", templates);
		
		assertEquals(1, templates.size());
		
		Template template = templates.get(0);
		
		assertEquals(inputFile1, template.input);
		assertEquals(outputDirPath.resolve("tempfile_1.txt").toFile(), templates.get(0).output);
	}

	@Test
	public void testGetTemplates_DirScanWithExclude() throws IOException {
		File outputDir = Files.createTempDir();
		outputDir.deleteOnExit();
		Path outputDirPath = outputDir.toPath();
		
		File inputDir = Files.createTempDir();
		inputDir.deleteOnExit();
		Path inputDirPath = inputDir.toPath();
		
		File inputFile1 = inputDirPath.resolve("tempfile_1.tmp").toFile();
		inputFile1.createNewFile();
		inputFile1.deleteOnExit();
		File inputFile2 = inputDirPath.resolve("tempfile_2.tmp").toFile();
		inputFile2.createNewFile();
		inputFile2.deleteOnExit();
		
		FileSet inputSet = new FileSet();
		inputSet.setDirectory(inputDir.getAbsolutePath());
		inputSet.addExclude("tempfile_2.+");
		
		TemplateConfig config = new TemplateConfig();
		config.setInputDir(inputSet);
		config.setOutputDir(outputDir);
		
		List<Template> templates = config.getTemplates();
		
		assertEquals(1, templates.size());
		assertEquals(inputFile1, templates.get(0).input);
		assertEquals(outputDirPath.resolve(inputFile1.getName()).toFile(), templates.get(0).output);
	}
	
	@Test
	public void testGetTemplates_DirScanWithInclude() throws IOException {
		File outputDir = Files.createTempDir();
		outputDir.deleteOnExit();
		Path outputDirPath = outputDir.toPath();
		
		File inputDir = Files.createTempDir();
		inputDir.deleteOnExit();
		Path inputDirPath = inputDir.toPath();
		
		File inputFile1 = inputDirPath.resolve("tempfile_1.tmp").toFile();
		inputFile1.createNewFile();
		inputFile1.deleteOnExit();
		File inputFile2 = inputDirPath.resolve("tempfile_2.tmp").toFile();
		inputFile2.createNewFile();
		inputFile2.deleteOnExit();
		
		FileSet inputSet = new FileSet();
		inputSet.setDirectory(inputDir.getAbsolutePath());
		inputSet.addInclude("tempfile_1.+");
		
		TemplateConfig config = new TemplateConfig();
		config.setInputDir(inputSet);
		config.setOutputDir(outputDir);
		
		List<Template> templates = config.getTemplates();
		
		assertEquals(1, templates.size());
		assertEquals(inputFile1, templates.get(0).input);
		assertEquals(outputDirPath.resolve(inputFile1.getName()).toFile(), templates.get(0).output);
	}

}
