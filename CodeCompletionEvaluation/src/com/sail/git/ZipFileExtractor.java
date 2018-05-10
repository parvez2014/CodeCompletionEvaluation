package com.sail.git;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;
import com.sail.config.Config;

//extract all zip files inside a given directory with the same name of the zip file
public class ZipFileExtractor {

	public static void run(File file) throws IOException, InterruptedException {
		System.out.println("File: "+file.getAbsolutePath());
		
		if(file.isDirectory()) {
			//extract all files in that particular directory
			System.out.println("File: "+file.getAbsolutePath());
			
			for(File f:file.listFiles()) {
				if(f.getName().endsWith(".zip"))
				extract(f,f.getParent()+File.separator+f.getName().substring(0,f.getName().length()-".zip".length()));
			}
		}else {
			System.out.println("Not a directory");
			
		}
	}
	public static void extract(File zipFile, String outputPath) throws IOException, InterruptedException {
		System.out.println("zipfile: "+zipFile.getAbsolutePath()+" Unzip File: "+outputPath);
		ProcessBuilder pb = new ProcessBuilder("unzip","-d",outputPath,zipFile.getAbsolutePath());
		Process process = pb.start();
		
		String output = ProcessUtility.output(process.getInputStream());
		String errorOutput = ProcessUtility.output(process.getErrorStream());
		List<String> lineList = IOUtils.readLines(new StringReader(output));
		int errCode = process.waitFor();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ZipFileExtractor.run(new File(Config.ARCHIVE_REPOSITORY_REVISION_PATH));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
