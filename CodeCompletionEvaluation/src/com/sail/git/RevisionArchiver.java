package com.sail.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;
import com.sail.config.Config;
import com.sail.git.commit.Commit;
import com.sail.git.commit.CommitManager;
import com.sail.git.revision.RevisionFileListCollector;

public class RevisionArchiver {

	private String repositoryPath;
	private String archiveFolderPath;
	private CommitManager commitManager;
	
	public RevisionArchiver(String repositoryPath, String _archiveFolderPath, CommitManager _commitManager) {
		this.repositoryPath = repositoryPath;
		this.archiveFolderPath = _archiveFolderPath;
		this.commitManager = _commitManager;
	}
	
	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public String getArchiveFolderPath() {
		return archiveFolderPath;
	}

	public void setArchiveFolderPath(String archiveFolderPath) {
		this.archiveFolderPath = archiveFolderPath;
	}

	public CommitManager getCommitManager() {
		return commitManager;
	}

	public void setCommitManager(CommitManager commitManager) {
		this.commitManager = commitManager;
	}

	//archive all revisions from start to endIndex-1
	public void run(int fromIndex, int toIndex) throws IOException, InterruptedException {
		//move to the revision base folder path
		for(int i=fromIndex;i<commitManager.getCommitList().size()&& i<toIndex;i++) {
			Commit commit = commitManager.getCommitList().get(i);
			String archiveFilePath = this.archiveFolderPath+File.separator+commit.getSha()+".zip";
			System.out.println(""+archiveFilePath);
			
			//now checkout the revision in this folder
			ProcessBuilder pb = new ProcessBuilder("git","archive","--format","zip","--output",archiveFilePath,commit.getSha());
			pb.directory(new File(repositoryPath));
			Process process = pb.start();		
			String output = ProcessUtility.output(process.getInputStream());
			int errCode = process.waitFor();
		}
	}
	
	//archive all revision in the given repository
	public void run() throws IOException, InterruptedException {
		this.run(0,this.commitManager.getCommitList().size());
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommitManager commitManager = new CommitManager(Config.REPOSITORY_PATH);
		try {
			commitManager.run();
			RevisionArchiver  revisionArchiver = new RevisionArchiver(Config.REPOSITORY_PATH,Config.ARCHIVE_REPOSITORY_REVISION_PATH,commitManager) ;
			revisionArchiver.run();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
