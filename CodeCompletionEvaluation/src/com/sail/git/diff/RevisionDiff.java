package com.sail.git.diff;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sail.codeCompletionEvaluation.process.ProcessUtility;

	/** Sample Input:
    diff --git a/FrameworkInfoLoader/src/com/srlab/frameworkInfo/Utility.java b/FrameworkInfoLoader/src/com/srlab/frameworkInfo/Utility.java
	index 2e7a2a7..e39e8a2 100644
	--- a/FrameworkInfoLoader/src/com/srlab/frameworkInfo/Utility.java
	+++ b/FrameworkInfoLoader/src/com/srlab/frameworkInfo/Utility.java
	@@ -11,7 +11,7 @@ import org.eclipse.jdt.core.dom.MethodInvocation;
	
	 public class Utility {
	
	-       public static String[] frameworks = { "java.awt." };
	+       public static String[] frameworks = { "javax.swing." };
	     public static String basePath = "E:\\output\\";
	     public static final String framework_full_info_path = basePath +"framework_full_info" + ".txt";
	     public static final String framework_class_info_path = basePath +"framework_class_info" + ".txt";
 	*/

public class RevisionDiff {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RevisionDiff revisionDiff = new RevisionDiff();
		try {
			revisionDiff.run("head", "head~1","E:\\codeCompletionEvaluation\\FrameworkInfoCollector");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(String oldSHA, String newSHA, String repositoryPath) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("git","diff","--no-color", oldSHA, newSHA);
		pb.directory(new File(repositoryPath));
		Process process = pb.start();		
		String output = ProcessUtility.output(process.getInputStream());
		int errCode = process.waitFor();
		parse(output);
	
	}
	private HashMap<String,ArrayList<Integer>> parse(String output) throws IOException {
		
		HashMap<String,ArrayList<Integer>> hmFileToAddedLines = new HashMap();
		
		//step-1:convert the output into list of lines
		List<String> lineList = IOUtils.readLines(new StringReader(output));
		
		//step-2: now parse individual lines
		for(int i=0;i<lineList.size();i++) {
			if(lineList.get(i).length()>0 && lineList.get(i).matches("\\s+")==false) {
				if(lineList.get(i).startsWith("---")) {
					String oldFile = lineList.get(i).split("\\s+")[1].substring(2);i++;
					if(lineList.get(i).startsWith("+++")) {
						String newFile = lineList.get(i).split("\\s+")[1].substring(2);i++;
						String splits[] = lineList.get(i).split("\\s+");
						if(splits[0].equals("@@") && splits[3].equals("@@")) {
							System.out.println("Line: "+lineList.get(i));
							int linesRead = this.readHunk(i, lineList, newFile, hmFileToAddedLines);
							i = i+ linesRead;
						}
						else throw new RuntimeException("Parsing Error");
					}
				}
			}
		}	
		return hmFileToAddedLines;
	}
	
	private int readHunk(int lineNumber, List<String> lineList, String fileName,HashMap<String,ArrayList<Integer>> hmFileToAddedLines) {
		int i=lineNumber, j=0, linesRead=0;
		ArrayList<Integer> addedLineNumberList = new ArrayList();
		System.out.println("Line: "+lineList.get(i));
		
		String splits[]=lineList.get(lineNumber).split("\\s+");
		int hunkStartLine = Integer.parseInt(splits[2].split(",")[0].substring(1));
		String firstLine = lineList.get(i).substring(splits[0].length()+splits[1].length()+splits[2].length()+3);
		if(firstLine.startsWith("+ ")) {
			addedLineNumberList.add(hunkStartLine);
		}
		linesRead++;
		while(i<lineList.size() && lineList.get(i).startsWith("diff --git")==false) {
			if(lineList.get(i).startsWith("+ ")) {
				addedLineNumberList.add(hunkStartLine+j);
			}
			i++;
			j++;
			linesRead++;
		}
		hmFileToAddedLines.put(fileName, addedLineNumberList);
		return linesRead;
	}

}
