package DataStructures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Dictionary {
	
	private int minimumDegree = 0;
	private DictionaryNode root = null;
	private int nTerms = 0; // Unique terms in the dict
	
	public Dictionary(int minimumDegree)
	{
		this.minimumDegree = minimumDegree;
		this.root = new DictionaryNode(minimumDegree);
		this.root.setLeaf(true);
	}
	
	public Object[] Search(String term)
	{
		//System.out.println("Searching for term [ " + term + " ]");
		return this.root.Search(term);
	}

	public PostingList getPostings(String term){
		return this.root.getPostings(term);
	}
	
	public boolean Add(String term, float docFreq, float idf)
	{
        if (this.Search(term) != null) return false;

		nTerms += 1;

		if (this.root.isFull())
		{
			DictionaryNode s = new DictionaryNode(this.minimumDegree);
			s.setChild(this.root, 0);
			this.root = s;
		}
		return this.root.InsertOnNonFullNode(term, docFreq, idf);
	}

	public void Add2Posting(String term, int docId, int termCount, float termFreq, float tf_idf, ArrayList<Integer> termPos){
		
		Object [] searchResult = this.root.Search(term);
		if (searchResult == null) return;
		
		int index = (int) searchResult[1];
        DictionaryNode tempNode = (DictionaryNode) searchResult[0];
        
		tempNode.getPostings(index).add(docId, termCount, termFreq, tf_idf, termPos);
		return;
	}

	public void setGlobalStatistics(int nDocs){

		Stack <DictionaryNode> nodeStack = new Stack<DictionaryNode>();
		Stack <Integer> pointerStack = new Stack<Integer>();
		float docFreq;
		float idf;
		PostingListNode tempPLNode;
		PostingList tempPL;

		nodeStack.add(this.root);
		pointerStack.add(0);


		while(!nodeStack.isEmpty()){
			DictionaryNode curr = nodeStack.peek();
			int index = pointerStack.peek();

			if (index == minimumDegree * 4 - 1){
				nodeStack.pop();
				pointerStack.pop();
				if (!pointerStack.isEmpty()){
					int temp = pointerStack.pop();
					pointerStack.add(temp + 1);
				}

			} else if (index % 2 == 0 && curr.getChildren(index/2) != null){ // Explore deeper children
				nodeStack.add(curr.getChildren(index/2));
				pointerStack.add(0); 

			} else if (index % 2 == 1 && !(curr.getTerms(index/2).equals(""))){ // Do compute on keys
				// Set up global variables
				tempPL = curr.getPostings(index/2);

				docFreq = (float) tempPL.lenght() / nDocs;
				idf = (float) Math.log(nDocs/tempPL.lenght()); // i dont think we need doubles
				
				curr.setDocFreq(docFreq, index/2);
				curr.setIdf(idf, index/2);
				
				tempPLNode = tempPL.getRoot();
				while (tempPLNode != null ) {
					tempPLNode.setTf_idf(tempPLNode.getTermFreq() * idf);
					tempPLNode = tempPLNode.getNext();
				}

				pointerStack.pop();
				pointerStack.add(index+1);

			} else {
				pointerStack.pop();
				pointerStack.add(index + 1);

			}
		}
	}

	public void save(String filename){

		try (FileWriter writer = new FileWriter(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + filename)){
			
			Stack <DictionaryNode> nodeStack = new Stack<DictionaryNode>();
			Stack <Integer> pointerStack = new Stack<Integer>();

			nodeStack.add(this.root);
			pointerStack.add(0);

			String term;
			// I do not store docCount since it is posting.lenght()
			float docFreq;
			float idf;
			PostingListNode posting;

			int docID;
			int termCount;
			float termFreq;
			float tf_idf;
			ArrayList<Integer> termPos;

			while(!nodeStack.isEmpty()){
				DictionaryNode curr = nodeStack.peek();
				int index = pointerStack.peek();

				if (index == minimumDegree * 4 - 1){
					nodeStack.pop();
					pointerStack.pop();
					if (!pointerStack.isEmpty()){
						int temp = pointerStack.pop();
						pointerStack.add(temp + 1);
					}

				} else if (index % 2 == 0 && curr.getChildren(index/2) != null){ // Explore deeper children
					nodeStack.add(curr.getChildren(index/2));
					pointerStack.add(0); 

				} else if (index % 2 == 1 && !(curr.getTerms(index/2).equals(""))){ // Do compute on keys
					// DO 
					term = curr.getTerms(index/2);
					docFreq = curr.getDocFreq(index/2);
					idf = curr.getIdf(index/2);
					posting = curr.getPostings(index/2).getRoot();

					writer.write(term+";"+docFreq+";"+idf+";{");
					while (posting != null){
						docID = posting.getDocId();
						termCount = posting.getTermCount();
						termFreq = posting.getTermFreq();
						tf_idf = posting.getTf_idf();
						termPos = posting.getTermPos();
						
						writer.write("{"+docID+";"+termCount+";"+termFreq+";"+tf_idf+";"+termPos.toString()+"};");

						posting = posting.getNext();
					}
					writer.write("};\n");

					pointerStack.pop();
					pointerStack.add(index+1);

				} else {
					pointerStack.pop();
					pointerStack.add(index + 1);

				}
			}
			writer.close();

		
		} catch (IOException e){
			System.out.println(e);
		}
		return;

	}

	//From a string and an index, reads chars until finding a ;
	public String readVal(String text, int startIndex){
		String output = "";
		int i = startIndex;

		while (i < text.length() && text.charAt(i) != ';'){
			output += text.charAt(i);
			i++;
		}

		return output;
	}


	public void loadFromIndex(String filename){ // HAY QUE TESTEARLO
		if (this.root == null) return;

	try (BufferedReader reader = new BufferedReader(new FileReader(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + filename))){ 
		String tempString;
		int tempInt = 0;
		char tempChar;
		int docIndex;

		String term;
		float docFreq;
		float idf;

		Integer docId;
		Integer termCount;
		Float termFreq;
		float tf_idf;
		ArrayList<Integer> termPos;

		tempString = reader.readLine();
		while(tempString != null){
			docIndex = 0;

			term = readVal(tempString, docIndex);
			docIndex += term.length() + 1;

			docFreq = Float.parseFloat(readVal(tempString, docIndex));
			docIndex += Float.toString(docFreq).length() + 1;

			idf = Float.parseFloat(readVal(tempString, docIndex));
			docIndex += Float.toString(idf).length() + 3;

			this.Add(term, docFreq, idf);
			while(docIndex < tempString.length()-4){
				docId = Integer.parseInt(readVal(tempString, docIndex));
				docIndex += Integer.toString(docId).length() + 1;

				termCount = Integer.parseInt(readVal(tempString, docIndex));
				docIndex += Integer.toString(termCount).length() + 1;

				termFreq = Float.parseFloat(readVal(tempString, docIndex));
				docIndex += Float.toString(termFreq).length() + 1;

				tf_idf = Float.parseFloat(readVal(tempString, docIndex));
				docIndex += Float.toString(tf_idf).length() + 2;

				termPos = new ArrayList<Integer>();

				while (tempString.charAt(docIndex) != ']'){
					tempChar = tempString.charAt(docIndex);
					if (Character.isDigit(tempChar)){
						tempInt = tempInt * 10 + (tempChar - '0');
					} else {
						if (tempChar != ' ')
							termPos.add(tempInt);
						tempInt = 0;
					}
					docIndex += 1;
				}
				termPos.add(tempInt);

				this.Add2Posting(term, docId, termCount, termFreq, tf_idf, termPos);
				docIndex += 4; // Space between end of relevant data of a posting and the next

			}
			tempString = reader.readLine();
		}
		} catch (Exception e){
			System.out.println(e);
		}
		return;
	}

	public void loadFromDataset(String datafile){

		int nDocs = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(Config.Constants.mainpath + "//Dataset//" + datafile))){
			String datasetLine = reader.readLine();
            int docID;

			int docLen;
			//int nUTokens;
			//int nVignettes;

            String[] tokens;
            int termCount;
            float termFreq;
            ArrayList<Integer> termPos;

            int i;
			while (datasetLine != null){ // Read Doc 1 by 1.
                docID = Integer.parseInt(datasetLine.substring(2,8));
                //System.out.print(docID+" ");
                if (datasetLine.length() > 11)
                    datasetLine = datasetLine.substring(12).toLowerCase();

                tokens = Indexer.Preprocessing.generalTokenizeStrings(datasetLine);
                
                //processedTokens = new String[tokens.length];
                for (i = 0; i < tokens.length; i++){
                    tokens[i] = Indexer.PorterStemmer.Stem(Indexer.Preprocessing.wordSquishing(tokens[i]));
                }

                // DocDict 
                docLen = tokens.length;
                //nUTokens = Preprocessing.nUTokens(tokens);
                //nVignettes = Preprocessing.nVignettes(datasetLine);
                //docDict.insert(docID, docLen, nUTokens, nVignettes); // HECHO


                for (i = 0; i < tokens.length; ++i){ // For each token in the doc
                    // Crear el termino en el diccionario
                    termCount = Indexer.Preprocessing.termCount(tokens[i], tokens);
                    termFreq = (float) termCount / docLen;
                    termPos = Indexer.Preprocessing.termPos(tokens[i], tokens);

                    // Añadir nodo al diccionario SIN datos globales (LUEGO)
                    this.Add(tokens[i], 0, 0); 

                    // Añadir a la posting list del diccionario
                    this.Add2Posting(tokens[i], docID, termCount, termFreq, 0, termPos); // DOES not add tf_idf
                }

                datasetLine = reader.readLine();
                nDocs += 1;
            }

            //docDict.save("docDict.txt"); // Does not require global variables
            reader.close();

        } catch (IOException e) {
            System.out.println(e);
        }
		this.setGlobalStatistics(nDocs);
        return;
    }

	
	public void Print()
	{
		if (this.root == null) return;
		this.root.print();
	}

	public int getnTerms() {
		return this.nTerms; 
	}

}
