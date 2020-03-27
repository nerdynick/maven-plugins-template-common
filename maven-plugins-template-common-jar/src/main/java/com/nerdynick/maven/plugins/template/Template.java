package com.nerdynick.maven.plugins.template;

import java.io.File;

public class Template{
	final public File input;
	final public File output;
	
	public Template(final File input, final File output){
		this.input = input;
		this.output = output;
	}
}