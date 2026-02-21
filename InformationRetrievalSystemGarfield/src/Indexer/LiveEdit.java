package Indexer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import Config.Constants;
import DataStructures.Dictionary;
import DataStructures.DictionaryNode;
import DataStructures.DocDict;
import DataStructures.DocDictNode;
import DataStructures.PostingListNode;

public class LiveEdit {
    
    Dictionary currDictionary;
    DocDict currDocDict;

    Dictionary liveDictionary;
    DocDict liveDocDict;
    ArrayList<Integer> deletedDocs;

    public LiveEdit (Dictionary currDictionary, DocDict currDocDict) {
        this.currDictionary = currDictionary;
        this.currDocDict = currDocDict;

        this.liveDictionary = new Dictionary(Constants.treeMinDegree);
        this.liveDocDict = new DocDict(Constants.treeMinDegree);
        this.deletedDocs = new ArrayList<Integer>();
    }


    public void runTimeAdd(int docID, String text){
        if (this.liveDocDict.exists(docID)) return;
        if (this.currDocDict.exists(docID) && !isDocDeleted(docID)) return;

        String[] tokens;
        tokens = Indexer.Preprocessing.generalTokenizeStrings(text);
        
        for (int i = 0; i < tokens.length; i++){
            tokens[i] = Indexer.PorterStemmer.Stem(Indexer.Preprocessing.wordSquishing(tokens[i]));
        }
        int docLen = tokens.length;
        int nUTokens = Preprocessing.nUTokens(tokens);
        int nVignettes = Preprocessing.nVignettes(text);
        
		this.liveDocDict.insert(docID, docLen, nUTokens, nVignettes, text); // HECHO

        int termCount;
        float termFreq;
        ArrayList<Integer> termPos;
        for (int i = 0; i < tokens.length; ++i){ // For each token in the doc
            // Crear el termino en el diccionario
            termCount = Indexer.Preprocessing.termCount(tokens[i], tokens);
            termFreq = (float) termCount / docLen;
            termPos = Indexer.Preprocessing.termPos(tokens[i], tokens);

            // Añadir nodo al diccionario SIN datos globales (LUEGO)
            this.liveDictionary.Add(tokens[i], 0, 0); 

            // Añadir a la posting list del diccionario
            this.liveDictionary.Add2Posting(tokens[i], docID, termCount, termFreq, 0, termPos); // DOES not add tf_idf
        }
        
        this.liveDictionary.setGlobalStatistics(this.currDocDict.getNDocuments() + 1); // An aproximation
    }

    public void runTimeDel(int docID){
        if (this.currDocDict.exists(docID))
            deletedDocs.add(docID);
        return;
    }

    public void runTimeMod(int docID, String newText){
        if (this.liveDocDict.exists(docID)) {
            System.out.println("The document has already been modified");
            return; // Some logic cluld solve this, but for the time scope I need to constraint some things
        }
        
        runTimeDel(docID);
        runTimeAdd(docID, newText);
        return;
    }


    
    public boolean isDocDeleted(int docID){
        for (int i = 0; i < deletedDocs.size(); ++i){
            if (deletedDocs.get(i) == docID) return true;
        }
        return false;
    }

    /*  This is not needed, since we write from disk to file and not file-file
    private void copyFile(String fileInput, String fileOutput){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + fileInput));
            try {
                FileWriter writer = new FileWriter(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + fileOutput);

                String tempString;
                tempString = reader.readLine();
                while (tempString != null){
                    writer.write(tempString);
                    tempString = reader.readLine();
                }
                writer.close();

            } catch (IOException e) {
                System.out.println(e);
            }

            reader.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
    */


    private void updateDocDict(String fileName){
        
        try (FileWriter writer = new FileWriter(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + fileName)){
			
            Stack <DocDictNode> nodeStack = new Stack<DocDictNode>();
			Stack <Integer> pointerStack = new Stack<Integer>();

			nodeStack.add(this.currDocDict.getRoot());
			pointerStack.add(0);
			while (!nodeStack.isEmpty()){ 
				DocDictNode curr = nodeStack.peek();
				int index = pointerStack.peek();
				
				if (index == this.currDocDict.getMinimumDegree() * 4 - 1){ // Explor deeper
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
					if (!this.isDocDeleted(curr.getDocId(index/2)))
                        writer.write(curr.getDocId(index/2) + ";" + curr.getDocLen(index/2) + ";" + curr.getUTokens(index/2) + ";" + curr.getNVignettes(index/2) + ";" + curr.getText(index/2) + ";\n");
					
					pointerStack.pop();
					pointerStack.add(index+1);
				}
				else {
					pointerStack.pop();
					pointerStack.add(index+1);
					
				}
			}
            nodeStack.add(this.liveDocDict.getRoot());
			pointerStack.add(0);
			while (!nodeStack.isEmpty()){ 
				DocDictNode curr = nodeStack.peek();
				int index = pointerStack.peek();
				
				if (index == this.liveDocDict.getMinimumDegree() * 4 - 1){ // Explor deeper
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
					writer.write(curr.getDocId(index/2) + ";" + curr.getDocLen(index/2) + ";" + curr.getUTokens(index/2) + ";" + curr.getNVignettes(index/2) + ";" + curr.getText(index/2) + ";\n");
					
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
			return;
		}
		return;
	}

    private void updateDictionary(String fileName){
		try (FileWriter writer = new FileWriter(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + fileName)){
			
			Stack <DictionaryNode> nodeStack = new Stack<DictionaryNode>();
			Stack <Integer> pointerStack = new Stack<Integer>();

			nodeStack.add(this.currDictionary.getRoot());
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

				if (index == this.currDictionary.getMinimumDegree() * 4 - 1){
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
					while (posting != null && !this.isDocDeleted(posting.getDocId())){
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

            nodeStack.add(this.liveDictionary.getRoot());
			pointerStack.add(0);
            while(!nodeStack.isEmpty()){
				DictionaryNode curr = nodeStack.peek();
				int index = pointerStack.peek();

				if (index == this.liveDictionary.getMinimumDegree() * 4 - 1){
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


    public void updateIndexes(String filenameDocDict, String filenameDict){ // To implement
        updateDocDict(filenameDocDict);
        updateDictionary(filenameDict);
    }

	public Dictionary getCurrDictionary() {
		return currDictionary;
	}
	public DocDict getCurrDocDict() {
		return currDocDict;
	}
	public Dictionary getLiveDictionary() {
		return liveDictionary;
	}
	public DocDict getLiveDocDict() {
		return liveDocDict;
	}
	public ArrayList<Integer> getDeletedDocs() {
		return deletedDocs;
	}
	

}


