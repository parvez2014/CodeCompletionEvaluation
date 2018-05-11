package com.sail.codeCompletionEvaluation.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.sail.config.Config;

public class LookupManager extends VoidVisitorAdapter<Void>{

	private String repositoryPath;
	private String archivePath;
	
	public LookupManager(String _repositoryPath, String _archivePath) {
		this.repositoryPath = _repositoryPath;
		this.archivePath = _archivePath;
	}
	
	//parse each source file and then fins the method invocation places
	public void lookup(File file) throws FileNotFoundException {
		System.out.println("File: " + file.getAbsolutePath());
		if(file.isFile() && file.getName().endsWith(".java")) {
			// creates an input stream for the file to be parsed
			FileInputStream in = new FileInputStream(file);
        	// parse the file
			CompilationUnit cu = JavaParser.parse(in);
			cu.accept(this,null);
		}
		else if(file.isDirectory() && file.listFiles()!=null){
			for(File f:file.listFiles()) {
				this.lookup(f);
			}
		}
	}
	/****************** all the visitor methods goes here**************/
	
	@Override
	public void visit(MethodCallExpr m, Void arg) {
		// TODO Auto-generated method stub
		if(m.getScope().isPresent()) {
			System.out.println("Method Name: "+m.getName().getIdentifier() +" Scope: "+m.getScope().get());
			
			try {
			System.out.println("Expression expression: "+m);
			ResolvedType resolvedType = JavaParserFacade.get(ProjectSymbolSolver.getInstance().getCombinedTypeSolver()).getType( m.getScope().get());
			JavaParserFacade jpf = JavaParserFacade.get(ProjectSymbolSolver.getInstance().getCombinedTypeSolver());
			SymbolReference<ResolvedMethodDeclaration> resolvedMethodDeclaration = jpf.solve(m);
			if(resolvedType.isReferenceType())
			System.out.println("Resolved Type: "+resolvedType.asReferenceType().getQualifiedName());
			else {
				System.out.println("This is not a reference type");
			}
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Fail to resolve type");
			}
		}
		m.getBegin().ifPresent(p -> System.out.println("Method Call Position"+p.column));
	}
	/*******************************************************************/
	public static void main(String args[]) {
		LookupManager lookupManager = new LookupManager(Config.REPOSITORY_PATH, Config.ARCHIVE_REPOSITORY_REVISION_PATH);
		try {
			lookupManager.lookup(new File(Config.REPOSITORY_PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
