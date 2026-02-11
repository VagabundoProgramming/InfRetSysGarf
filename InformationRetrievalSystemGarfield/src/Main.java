import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.print.Doc;

import Config.Constants;
import DataStructures.DictionaryNode;
import DataStructures.DocDict;
import DataStructures.DocDictNode;
import DataStructures.PostingList;
import DataStructures.Dictionary;
import DataStructures.DictionaryNode;

import Indexer.Preprocessing;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!\n");

        //Preprocessing.rawDatasetPreprocess("garfield.txt", "garfieldProcessed.txt");

        String filename = "garfieldProcessed.txt";
        Dictionary dict = new Dictionary(8);
        DocDict docDict = new DocDict(8);


        // DocDict (All are Non-Global)
        int docLen;
        int nUTokens;
        int nVignettes;

        // Two steps, first add all base info, 
        // Then add global variables by exploring everything. 

        try {
            BufferedReader reader = new BufferedReader (new FileReader(Constants.mainpath + "//Dataset//" + filename));
            
            String datasetLine = reader.readLine();
            int docID;

            String[] tokens;
            int nDocs = 0;
            


            // Dictionary Node has ALL global variables

            // DictNode non-global (all except tf_idf)
            int termCount;
            float termFreq;
            ArrayList<Integer> termPos;


            int i;
            while (datasetLine != null && nDocs < 20){ // Read Doc 1 by 1.
                docID = Integer.parseInt(datasetLine.substring(2,8));
                datasetLine = datasetLine.substring(12).toLowerCase();

                tokens = Indexer.Preprocessing.generalTokenizeStrings(datasetLine);
                
                // DocDict 
                docLen = tokens.length;
                nUTokens = Preprocessing.nUTokens(tokens);
                nVignettes = Preprocessing.nVignettes(datasetLine);

                docDict.insert(docID, docLen, docID, nVignettes); // HECHO

                for (i = 0; i < tokens.length; ++i){ // For each token in the doc
                    // Crear el termino en el diccionario
                     // Simplemente añadir un posting node en vez de todo. 
                    
                    termCount = Indexer.Preprocessing.termCount(tokens[i], tokens);
                    termFreq = termCount / docLen;
                    termPos = Indexer.Preprocessing.termPos(tokens[i], tokens);
                    
                    // Añadir nodo al diccionario SIN datos globales (LUEGO)
                    dict.Add(tokens[i], 0, 0); 

                    // Añadir a la posting list del diccionario
                    dict.Add2Posting(tokens[i], docID, termCount, termFreq, termPos);
                }

                datasetLine = reader.readLine();
                nDocs += 1;
            }

            
            dict.Print();
            System.out.println("\n\n");
            dict.getPostings("the").Print(); 

            System.out.println(dict.getnTerms()); // Funciona




        } catch (IOException e) {
            System.out.println(e);
        }
        



        /* 
        int start = 17120;
        int finish = 17138;
        
        String[] temp = Indexer.DataReader.ExtractLinesRaw(start, finish, "garfieldProcessed.txt"); //Reads lines from start-finish of the dataset and returns them as an array of lists
        Object[] temp2 = Indexer.Preprocessing.Raw2Pairs(temp); // Given an array of raw lines it resutns the doc id array and the text array
        
        int[] docIds = (int[]) temp2[0]; // Importante Acordarse de esto
        String[] texts = (String[]) temp2[1];
        
        System.out.println("Retrieved a total of " + docIds.length + " documents.");

        String tempStem;
        int docLen;
        int nUTokens;
        for (int i = 0; i < docIds.length; ++i){ // For each doc
            String[] tempTok;

            // Index a doc, extract possible doc data
            tempTok = Indexer.Preprocessing.generalTokenizeStrings(texts[i]); 
            docLen = tempTok.length;
            nUTokens = Preprocessing.nUTokens(tempTok);
            // Cannot compute docFreq and idf due to requireing the rest of the data to be stored dict.nDocumetns


            for (int j = 0; j < tempTok.length; ++j){
                tempStem = Indexer.PorterStemmer.Stem(tempTok[j]);
                //System.out.print(tempStem + " / ");
                System.out.println(tempStem);
                tempStem = Indexer.Preprocessing.wordSquishing(tempStem);
                
                a.add(Indexer.PorterStemmer.Stem(tempStem));
            } 
            System.out.println();
        }
        System.out.println("\n\n\n\n\n\n\n");
        System.out.println(a.Search("here"));
        a.Print();
        
        return;
        */
    
    }
}
