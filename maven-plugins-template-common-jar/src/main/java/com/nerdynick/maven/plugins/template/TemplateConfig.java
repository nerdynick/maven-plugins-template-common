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

public class TemplateConfig {
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
	
	public List<Template> getTemplates() throws IOException{
		List<Template> templates = new ArrayList<>();
		
		if(!outputDir.exists()){
			if(!outputDir.mkdirs()){
				throw new IOException("Failed to create outputDir of "+ outputDir);
			}
		}
		
		Path outputPath = outputDir.toPath();
		
		if(inputDir != null){
			handleDir(inputDir, outputPath, templates);
		}
		
		if(inputDirs != null){
			for(FileSet s: inputDirs){
				handleDir(s, outputPath, templates);
			}
		}
		
		if(inputFiles != null){
			for(File f: inputFiles){
				checkFile(f);
				templates.add(new Template(f, outputPath.resolve(f.getName()).toFile()));
			}
		}
		
		if(inputFile != null){
			checkFile(inputFile);
			
			if(outputName != null && !outputName.isEmpty()){
				templates.add(new Template(inputFile, outputPath.resolve(outputName).toFile()));
			} else {
				templates.add(new Template(inputFile, outputPath.resolve(inputFile.getName()).toFile()));
			}
		}
		
		return templates;
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
			templates.add(new Template(f, outputPath.resolve(f.getName()).toFile()));
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
