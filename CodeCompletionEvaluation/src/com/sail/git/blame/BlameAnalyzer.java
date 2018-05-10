package com.sail.git.blame;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;

public class BlameAnalyzer {

	private String repositoryPath;
	
	public BlameAnalyzer (String _repositoryPath) {
		this.repositoryPath = _repositoryPath;
	}
	public ArrayList<BlameLine> blame(String filePath) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("git","blame","-l","-w",filePath);
		pb.directory(new File(this.getRepositoryPath()));
		Process process = pb.start();
		int errCode = process.waitFor();
		String output = ProcessUtility.output(process.getInputStream());
		ArrayList<BlameLine> blameLineList = BlameParser.parse(IOUtils.readLines(new StringReader(output)));
		return blameLineList;
	}
	
	public String getRepositoryPath() {
		return repositoryPath;
	}
	
	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
