package com.sail.git;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;
//https://stackoverflow.com/questions/5483830/process-waitfor-never-returns
public class GitUtility {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		List<String> lineList = GitUtility.getFileContent("472aeb1861db7175e56e2594354d0ff0a25772d","FrameworkInfoLoader/src/frameworkinfoloader/Activator.java","D:\\Muhammad\\version_evaluation\\FrameworkInfoCollector");
		for(String line:lineList) {
			System.out.println(line);
		}
	}
	public static List<String> getFileContent(String commit,String fileName,String repositoryPath) throws IOException, InterruptedException{
		
		ProcessBuilder pb = new ProcessBuilder("cmd","/C","git","blame","FrameworkInfoLoader\\src\\frameworkinfoloader\\Activator.java");
		pb.directory(new File(repositoryPath));
		Process process = pb.start();
		//
		String output = ProcessUtility.output(process.getInputStream());
		String errorOutput = ProcessUtility.output(process.getErrorStream());
		System.out.println("Error: "+errorOutput);
		List<String> lineList = IOUtils.readLines(new StringReader(output));
		int errCode = process.waitFor();
		return lineList;
		//System.out.println("Echo Error Output:\n" + ProcessUtility.output(process.getErrorStream()));  
	}
}
