package DataStructures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class DocDict {
    private int minimumDegree = 0;
	private DocDictNode root = null;
	private int nDocuments = 0;  
	
	public DocDict(int minimumDegree)
	{
		this.minimumDegree = minimumDegree;
		this.root = new DocDictNode(minimumDegree);
		this.root.setLeaf();
	}
	
	public Object[] search(int docID)
	{
		return this.root.search(docID);
	}
	
	public boolean insert(int docID, int docLen, int uToknes, int nVignettes)
	{
		if (this.search(docID) != null) return false;
		this.nDocuments += 1;
		if (this.root.isFull())
		{
			System.out.print("DocDict Root Full");
			DocDictNode s = new DocDictNode(this.minimumDegree);
			s.setChild(this.root, 0);
			this.root = s;
		}
		return this.root.insertOnNonFullNode(docID, docLen, uToknes, nVignettes);
	}
	
	public void print()
	{
		if (this.root == null) return;
		this.root.print();
	}

	// Save and load have boolean return values to see if they finish properly their execution. 
	public boolean save(String filename){
		try (FileWriter writer = new FileWriter(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + filename)){
			writer.write(Integer.toString(nDocuments) + "\n");
			
			Stack <DocDictNode> nodeStack = new Stack<DocDictNode>();
			Stack <Integer> pointerStack = new Stack <Integer>();
			nodeStack.add(this.root);
			pointerStack.add(0);
			while (!nodeStack.isEmpty()){ 
				DocDictNode curr = nodeStack.peek();
				int index = pointerStack.peek();
				
				if (index == minimumDegree * 4 - 1){ // Explor deeper
					nodeStack.pop();
					pointerStack.pop();
					if (!pointerStack.isEmpty()){
						int temp = pointerStack.pop();
						pointerStack.add(temp + 1);
					}
				}
				else if (index % 2 == 0 && curr.getChildren(index/2) != null){ // Explore deeper children
					nodeStack.add(curr.getChildren(index/2));
					pointerStack.add(0); 

				}
				else if (index % 2 == 1 && curr.getDocId(index/2) != 0){ // Do compute on keys
					writer.write(curr.getDocId(index/2) + ";" + curr.getDocLen(index/2) + ";" + curr.getUTokens(index/2) + ";" + curr.getNVignettes(index/2) + ";\n");
					pointerStack.pop();
					pointerStack.add(index+1);
				}
				else {
					pointerStack.pop();
					pointerStack.add(index+1);
					
				}
			}
			
			writer.close();

		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
		return true;
	}

	public void deleteTree(){
		this.root = null;
		return;
	}

	public boolean load(String filename){
		if (this.root == null) return false;
		try (BufferedReader reader = new BufferedReader(new FileReader(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + filename))){ 
			String tempString;
			int tempInt = 0;
			
			tempString = reader.readLine();
			this.nDocuments = Integer.parseInt(tempString);

			tempString = reader.readLine();
			while (tempString != null){
				int temp[] = {0,0,0,0}; // docID, docLen, uTokens, nVignettes
				int i = 0;
				char separator = ';';

				for (int j = 0; j < tempString.length(); ++j){
					System.out.println("Do i explode here?");
					System.out.println(tempString.charAt(j));
					if (!(separator == (tempString.charAt(j))))
						tempInt += tempInt * 10 + Integer.parseInt(tempString.substring(j, j+1));
					else{
						System.out.println("Adding the " + i + " th value");
						temp[i] = tempInt;
						tempInt = 0;
						i += 1;
					}
					
					System.out.println("Yass");
				}
				insert(temp[0], temp[1], temp[2], temp[3]);
				tempString = reader.readLine();
			}

			reader.close();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		
		return true;
	}
}
