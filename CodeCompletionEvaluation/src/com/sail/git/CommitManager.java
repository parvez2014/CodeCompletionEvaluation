package com.sail.git;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;

public class CommitManager {

	public ArrayList<Commit> commitList; //starts from index zero which is the most recent commit
	public String repositoryPath;
	
	public CommitManager(String _repositoryPath) {
		this.commitList = new ArrayList();
		this.repositoryPath = _repositoryPath;
	}
	public void add(Commit commit) {
		this.commitList.add(commit);
	}
	public boolean hasCommitBySHA(String SHA) {
		for(Commit commit:commitList) {
				if(commit.getSha().equals(SHA)) {
					return true;
				}
		}
		return false;
	}
	
	//the goal of this function is to collect all commits and arrange them based on the time
	public void run() throws InterruptedException, IOException {
		ProcessBuilder pb = new ProcessBuilder("cmd","/C","git","log","master");
		pb.directory(new File(this.getRepositoryPath()));
		Process process = pb.start();
		int errCode = process.waitFor();
		//System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
		
		String output = ProcessUtility.output(process.getInputStream());
		//System.out.println("Echo Error Outut:\n" + ProcessUtility.output(process.getErrorStream()));  
		
		this.parse(output);
	}
	
	private String listToString(List<String> lineList) {
		StringBuilder sb = new StringBuilder("");
		for(String line:lineList) {
			if(line.matches("\\s+")==false)
			sb.append(line.trim());
		}
		return sb.toString();
	}
	
	private void parse(String output) throws IOException {
		String author=null;
		String commitSHA=null;
		String date  =null;
		String message=null;
		ArrayList<String> messageLines = null;
		
		//step-1:convert the output into list of lines 
		List<String> lineList = IOUtils.readLines(new StringReader(output));
		
		//step-2: read the commits
		for(int i=0;i<lineList.size();i++) {
			String line = lineList.get(i);
			if(line.startsWith("commit ")) {
				String split[]=line.split("\\s+");
				commitSHA = split[1];i++;
				
				if(lineList.get(i).startsWith("Author:") && lineList.get(i+1).startsWith("Date:")) {
					author= lineList.get(i).substring("Author: ".length()).trim(); i++;
					date  = lineList.get(i).substring("Date: ".length()).trim(); i++;
					
					//now read the messages
					messageLines = new ArrayList();
					for(;i<lineList.size();i++) {
						line = lineList.get(i);
						if(line.startsWith("commit ")) {
							i--;
							break;
						}else {
							messageLines.add(line);
						}
					}
					Commit commit = new Commit(author,date,this.listToString(messageLines),commitSHA);
					this.add(commit);
					commit.print();
					
				}else {
					throw new RuntimeException("Error in parsing output");
				}
			}
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommitManager commitManager = new CommitManager("D:\\Muhammad\\version_evaluation\\FrameworkInfoCollector");
		try {
			commitManager.run();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<Commit> getCommitList() {
		return commitList;
	}
	public String getRepositoryPath() {
		return repositoryPath;
	}

}
