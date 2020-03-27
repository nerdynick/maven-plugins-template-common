package com.nerdynick.maven.plugins.template;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class TemplateConfig {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateConfig.class);
	
	@Parameter(name="outputDir", required=true)
	private File outputDir;
	
	@Parameter(name="inputDir", required=false)
	private FileSet inputDir;
	
	@Parameter(name="inputDirs", required=false)
	private List<FileSet> inputDirs;
	
	@Parameter(name="inputFile", required=false)
	private File inputFile;

	@Parameter(name="inputFiles", required=false)
	private List<File> inputFiles;
	
	@Parameter(name="outputName", required=false)
	private String outputName;
	
	@Parameter(name="outputExt", required=false)
	private String outputExt;
	
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder()
			.append(this.getClass().getSimpleName())
			.append('[')
			.append("InputDir=").append(this.inputDir)
			.append(" InputDirs=").append(this.inputDirs)
			.append(" InputFile=").append(this.inputFile)
			.append(" InputFiles=").append(this.inputFiles)
			.append(" OutputDir=").append(this.outputDir)
			.append(" OutputName=").append(this.outputName)
			.append(" OutputExt=").append(this.outputExt)
			.append(']');
		
		
		return b.toString();
	}
	
	public File getOutputDir() {
		return outputDir;
	}
	public FileSet getInputDir() {
		return inputDir;
	}
	public File getInputFile() {
		return inputFile;
	}
	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}
	public void setInputDir(FileSet inputDir) {
		this.inputDir = inputDir;
	}
	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}
	public String getOutputName() {
		return outputName;
	}
	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}
	public void setOutputExt(String outputExt) {
		this.outputExt = outputExt;
	}
	
	public List<Template> getTemplates() throws IOException{
		List<Template> templates = new ArrayList<>();
		
		if(!outputDir.exists()){
			if(!outputDir.mkdirs()){
				throw new IOException("Failed to create outputDir of "+ outputDir);
			}
		}
		
		Path outputPath = outputDir.toPath();
		
		if(inputDir != null){
			LOG.debug("Input Dir: {}", inputDir);
			handleDir(inputDir, outputPath, templates);
		}
		
		if(inputDirs != null){
			LOG.debug("Input Dirs: {}", inputDirs);
			for(FileSet s: inputDirs){
				handleDir(s, outputPath, templates);
			}
		}
		
		if(inputFiles != null){
			LOG.debug("Input Files: {}", inputFiles);
			for(File f: inputFiles){
				checkFile(f);
				final Template t = new Template(f, this.getOutputFile(outputPath, f));
				LOG.debug("Template: {}", t);
				templates.add(t);
			}
		}
		
		if(inputFile != null){
			LOG.debug("Input File: {}", inputFile);
			checkFile(inputFile);
			final Template t = new Template(inputFile, this.getOutputFile(outputPath, inputFile));
			LOG.debug("Template: {}", t);
			templates.add(t);
		}
		
		return templates;
	}
	
	protected File getOutputFile(final Path outputPath, final File inputFile) {
		if(outputName != null && !outputName.isEmpty()){
			return outputPath.resolve(outputName).toFile();
		} else if(!Strings.isNullOrEmpty(this.outputExt)){
			String filename = inputFile.getName();
			filename = filename.substring(0, filename.lastIndexOf('.')+1) + this.outputExt;
			
			return outputPath.resolve(filename).toFile();
		} else {
			return outputPath.resolve(inputFile.getName()).toFile();
		}
	}
	
	protected void handleDir(final FileSet inputDir, final Path outputPath, List<Template> templates) throws IOException {
		File dir = new File(inputDir.getDirectory());
		checkDir(dir);
		
		Pattern[] includePatterns = inputDir.getIncludes().stream().map(s->Pattern.compile(s)).toArray(Pattern[]::new);
		Pattern[] excludePatterns = inputDir.getExcludes().stream().map(s->Pattern.compile(s)).toArray(Pattern[]::new);
		
		File[] files = dir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				if(excludePatterns.length > 0){
					for(Pattern p: includePatterns){
						if(p.matcher(name).matches()){
							return true;
						}
					}
					
					for(Pattern p: excludePatterns){
						if(p.matcher(name).matches()){
							return false;
						}
					}
					
					return true;
				} else if(includePatterns.length > 0){
					for(Pattern p: includePatterns){
						if(p.matcher(name).matches()){
							return true;
						}
					}
					return false;
				}
				
				return true;
			}
		});
		
		for(File f: files){
			if(f.isDirectory()){
				continue;
			}
			final Template t = new Template(f, this.getOutputFile(outputPath, f));
			LOG.debug("Template: {}", t);
			templates.add(t);
		}
	}
	
	protected void checkDir(File dir) throws IOException {
		if(!dir.exists()){
			throw new IOException(dir +" doesn't exist");
		}
		if(!dir.canRead()){
			throw new IOException(dir +" can't be read");
		}
		if(!dir.isDirectory()){
			throw new IOException(dir +" isn't a directory");
		}
	}
	
	protected void checkFile(File inputFile) throws IOException{
		if(!inputFile.exists()){
			throw new IOException(inputFile +" doesn't exist");
		}
		if(!inputFile.canRead()){
			throw new IOException(inputFile +" can't be read");
		}
		if(!inputFile.isFile()){
			throw new IOException(inputFile +" is not a file");
		}
	}
}
