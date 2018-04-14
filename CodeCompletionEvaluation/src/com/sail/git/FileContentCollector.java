package com.sail.git;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;

public class FileContentCollector {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		List<String> lineList = FileContentCollector.getFileContent("7ff4a71abcdd55da1470b42c0f579df995d319ea","FrameworkInfoLoader\\src\\frameworkinfoloader\\Activator.java","E:\\codeCompletionEvaluation\\FrameworkInfoCollector");
		for(String line:lineList) {
			System.out.println(line);
		}
	}
	public static List<String> getFileContent(String commit,String fileName,String repositoryPath) throws IOException, InterruptedException{
		
		ProcessBuilder pb = new ProcessBuilder("cmd","/C","git","blame","-l",commit,fileName);
		pb.directory(new File(repositoryPath));
		Process process = pb.start();
		
		String output = ProcessUtility.output(process.getInputStream());
		String errorOutput = ProcessUtility.output(process.getErrorStream());
		System.out.println("Error: "+errorOutput);
		List<String> lineList = IOUtils.readLines(new StringReader(output));
		int errCode = process.waitFor();
		return lineList;
	}

}
