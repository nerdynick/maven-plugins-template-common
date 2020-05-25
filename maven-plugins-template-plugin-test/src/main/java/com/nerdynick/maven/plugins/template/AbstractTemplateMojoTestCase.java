package com.nerdynick.maven.plugins.template;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.google.common.io.Resources;

public abstract class AbstractTemplateMojoTestCase<M extends Mojo> extends AbstractMojoTestCase {
	@BeforeClass
	public void setup() throws Exception {
		super.setUp();
	}
	
	@AfterClass
	public void teardown() throws Exception {
		super.tearDown();
	}
	
	protected File getTestPom() throws URISyntaxException {
		File pom = new File(Resources.getResource("test-pom.xml").toURI());
		assertNotNull(pom);
		assertTrue(pom.exists());
		
		return pom;
	}
	
	abstract protected String getMojoGoal();
	
	@SuppressWarnings("unchecked")
	protected M getMojo() throws URISyntaxException, Exception {
		return (M) this.lookupMojo(this.getMojoGoal(), this.getTestPom());
	}
}
