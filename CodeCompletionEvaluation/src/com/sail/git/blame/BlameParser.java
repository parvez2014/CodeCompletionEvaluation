package com.sail.git.blame;

import java.util.ArrayList;

public class BlameParser {

	//List of lines containing the output of a blame command on a file in a specific revision
	public static ArrayList<BlameLine> parse(ArrayList<String> lineList) {
		String SHA = null;
		String authorName = null;
		String date = null;
		String content = null;
		int lineNumber = -1;
		ArrayList<BlameLine> blameLineList = new ArrayList();
		for(String line:lineList) {
			String splits[] = line.split("\\s+");
			SHA = splits[0];
			authorName = splits[1];
			date = splits[2]+" "+splits[3]+" "+splits[4];
			lineNumber = Integer.parseInt(splits[5].substring(0,(splits[5].length()-1)));
			content = splits[6];
			BlameLine blameLine = new BlameLine(lineNumber,content,authorName,date,SHA);
			blameLineList.add(blameLine);
		}
		return blameLineList;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
