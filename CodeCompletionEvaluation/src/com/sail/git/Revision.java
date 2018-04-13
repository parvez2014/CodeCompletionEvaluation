package com.sail.git;

import java.util.ArrayList;

public class Revision {
	ArrayList<String> filesAdded;
	ArrayList<String> filesDeleted;
	ArrayList<String> filesChanged;
	
	int currentCommitIndex;

	
	public Revision(ArrayList<String> filesAdded, ArrayList<String> filesDeleted, ArrayList<String> filesChanged,
			int currentCommitIndex) {
		super();
		this.filesAdded = filesAdded;
		this.filesDeleted = filesDeleted;
		this.filesChanged = filesChanged;
		this.currentCommitIndex = currentCommitIndex;
	}

	public ArrayList<String> getFilesAdded() {
		return filesAdded;
	}

	public void setFilesAdded(ArrayList<String> filesAdded) {
		this.filesAdded = filesAdded;
	}

	public ArrayList<String> getFilesDeleted() {
		return filesDeleted;
	}

	public void setFilesDeleted(ArrayList<String> filesDeleted) {
		this.filesDeleted = filesDeleted;
	}

	public ArrayList<String> getFilesChanged() {
		return filesChanged;
	}

	public void setFilesChanged(ArrayList<String> filesChanged) {
		this.filesChanged = filesChanged;
	}

	public int getCurrentCommitIndex() {
		return currentCommitIndex;
	}

	public void setCurrentCommitIndex(int currentCommitIndex) {
		this.currentCommitIndex = currentCommitIndex;
	}
	
}
