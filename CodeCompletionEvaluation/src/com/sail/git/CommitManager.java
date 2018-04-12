package com.sail.git;

import java.util.ArrayList;

public class CommitManager {

	public ArrayList<Commit> commitList;
	
	public CommitManager() {
		this.commitList = new ArrayList();
	}
	public void add(Commit commit) {
		this.commitList.add(commit);
	}
	public boolean hasCommitBySHA(char sha[]) {
		for(Commit commit:commitList) {
				if(commit.getSha().equals(sha)) {
					return true;
				}
		}
		return false;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
