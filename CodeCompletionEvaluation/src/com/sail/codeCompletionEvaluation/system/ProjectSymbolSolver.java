package com.sail.codeCompletionEvaluation.system;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.sail.config.Config;

public class ProjectSymbolSolver {
	public static ProjectSymbolSolver self = null;
	private CombinedTypeSolver combinedTypeSolver;
	private ProjectSymbolSolver(){
		this.combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new JavaParserTypeSolver(new File(Config.REPOSITORY_PATH)));	    
	    combinedTypeSolver.add(new ReflectionTypeSolver());		  
	    this.addLibFolder(new File(Config.LIB_FOLDER));
	    ResolvedReferenceTypeDeclaration resolvedReferenceType = combinedTypeSolver.solveType("org.apache.commons.io.IOUtils");
	    System.out.println("ResolvedReferenceTypeDeclaration: "+resolvedReferenceType.getClassName());
	}
	
	public CombinedTypeSolver getCombinedTypeSolver() {
		return combinedTypeSolver;
	}
	public void addLibFolder(File libFolder) {
		if(libFolder.isDirectory()&& libFolder.listFiles()!=null) {
			for(File file:libFolder.listFiles()) {
				try {
					this.addJar(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void addJar(File jarFile) throws FileNotFoundException, IOException {
		if(jarFile.getName().endsWith(".jar")) {
			System.out.println("Add jar file: "+jarFile.getAbsolutePath());
			this.combinedTypeSolver.add(new JarTypeSolver(new FileInputStream(jarFile)));
		}
	}

	public static ProjectSymbolSolver getInstance(){
		if(self==null){
			self = new ProjectSymbolSolver();
		}
		return self;
	}

}
