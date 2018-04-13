package com.sail.git;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;

public class RevisionManager {

	private ArrayList<Revision> revisionList;
	private CommitManager commitManager;
	
	public RevisionManager(CommitManager _commitManager) {
		this.revisionList = new ArrayList();
		this.commitManager = _commitManager;
	}
	
	public void run() throws IOException, InterruptedException {
		for(int commitIndex=0;commitIndex<(this.commitManager.getCommitList().size()-1);commitIndex++) {
			String currentCommitSHA = this.commitManager.getCommitList().get(commitIndex).getSha();
			String previousCommitSHA = this.commitManager.getCommitList().get(commitIndex+1).getSha();
			
			ProcessBuilder pb = new ProcessBuilder("cmd","/C","git","diff","--name-status",currentCommitSHA,previousCommitSHA);
			pb.directory(new File(commitManager.getRepositoryPath()));
			Process process = pb.start();
			int errCode = process.waitFor();
			//System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
			
			String output = ProcessUtility.output(process.getInputStream());
			//System.out.println("Echo Error Output:\n" + ProcessUtility.output(process.getErrorStream()));  
			
			Revision revision = this.parse(output,commitIndex);
			this.revisionList.add(revision);
		}
		//for the last commit all files have been added. So we need to determine list of all files in that particular reviiosn and include them in the added file list
		Commit firstCommit = (this.commitManager.getCommitList().get(this.commitManager.getCommitList().size()-1));
		ArrayList<String> fileList = RevisionFileListCollector.getFileList(firstCommit.getSha(),commitManager.getRepositoryPath());
		Revision revision = new Revision(fileList,null,null,(this.commitManager.getCommitList().size()-1));
		this.revisionList.add(revision);
	}
	
	public Revision parse(String output, int commitIndex) throws IOException {
		//step-1:convert the output into list of lines 
		List<String> lineList = IOUtils.readLines(new StringReader(output));
		ArrayList<String> filesAdded = new ArrayList();
		ArrayList<String> filesDeleted = new ArrayList();
		ArrayList<String> filesChanged = new ArrayList();
		Revision revision = null;
		
		for(String line:lineList) {
			if(line.length()>0 && line.matches("\\s+")==false){
				String split[] = line.split("\\s+");
				if(split[0].equals("A")) {
					filesAdded.add(line.trim());
				}
				else if(split[0].equals("D")) {
					filesDeleted.add(line.trim());
				}
				else if(split[0].equals("M")) {
					filesChanged.add(line.trim());
				}
			}
		}
		revision = new Revision(filesAdded,filesDeleted,filesChanged,commitIndex);
		return revision;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommitManager commitManager = new CommitManager("D:\\Muhammad\\version_evaluation\\FrameworkInfoCollector");
		try {
			commitManager.run();
			RevisionManager revisionManager = new RevisionManager(commitManager);
			revisionManager.run();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
