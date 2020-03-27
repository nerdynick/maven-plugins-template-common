package com.nerdynick.maven.plugins.template;

import java.io.File;

public class Template{
	final public File input;
	final public File output;
	
	public Template(final File input, final File output){
		this.input = input;
		this.output = output;
	}
	
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder()
			.append(this.getClass().getSimpleName())
			.append('[')
			.append("Input=").append(this.input)
			.append(" Onput=").append(this.output)
			.append(']');
		
			return b.toString();
	}
}