package com.sail.git.blame;

public class BlameLine {
	int lineNumber;
	String content;
	String authorName;
	String date;
	String SHA;
	
	public BlameLine(int lineNumber, String content, String authorName, String date, String sHA) {
		super();
		this.lineNumber = lineNumber;
		this.content = content;
		this.authorName = authorName;
		this.date = date;
		SHA = sHA;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSHA() {
		return SHA;
	}
	public void setSHA(String sHA) {
		SHA = sHA;
	}
	
}
