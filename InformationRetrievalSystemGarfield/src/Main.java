import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
        int nDocs = 0;

        
        //String[] l = Indexer.Preprocessing.generalTokenizeStrings("Man, I haven't had lasagna in, like, forever. RRRrrrrr - What would you like for dinner? LASAGNA! - Some steamed rice? NO! LASAGNA! - Maybe peas and carrots? l-a-s-a-g-n-a...lasagna! - Or, I could whip up a nice... - Here! Noodles! Onions! Ricotta! Mozzarella! Tomatoes! Sausage! - Or pupapth wathanya. Now you're talking.");
        /*String[] l = {"zz"};
        for (int i = 0; i < l.length; ++i){
            System.out.print(l[i]);
            System.out.println(" --> " + Indexer.Preprocessing.wordSquishing(Indexer.PorterStemmer.Stem(l[i])));
        }*/
        //System.out.println(Indexer.Preprocessing.generalTokenizeStrings("I eat too much, I sleep too much and I don't exercise at all. - There's certainly room for improvement. - I think I'll take up smoking."));
        //TimeUnit.SECONDS.sleep(100500);
        
        
        
        
        try { 
            BufferedReader reader = new BufferedReader (new FileReader(Constants.mainpath + "//Dataset//" + filename));
            
            String datasetLine = reader.readLine();
            int docID;

            String[] tokens;
            int termCount;
            float termFreq;
            ArrayList<Integer> termPos;

            int i;
            while (datasetLine != null){ // Read Doc 1 by 1.
                docID = Integer.parseInt(datasetLine.substring(2,8));
                System.out.print(docID+" ");
                if (datasetLine.length() > 11)
                    datasetLine = datasetLine.substring(12).toLowerCase();

                tokens = Indexer.Preprocessing.generalTokenizeStrings(datasetLine);
                
                //processedTokens = new String[tokens.length];
                for (i = 0; i < tokens.length; i++){
                    tokens[i] = Indexer.PorterStemmer.Stem(Indexer.Preprocessing.wordSquishing(tokens[i]));
                }

                // DocDict 
                docLen = tokens.length;
                nUTokens = Preprocessing.nUTokens(tokens);
                nVignettes = Preprocessing.nVignettes(datasetLine);
                docDict.insert(docID, docLen, nUTokens, nVignettes); // HECHO


                for (i = 0; i < tokens.length; ++i){ // For each token in the doc
                    // Crear el termino en el diccionario
                    termCount = Indexer.Preprocessing.termCount(tokens[i], tokens);
                    termFreq = (float) termCount / docLen;
                    termPos = Indexer.Preprocessing.termPos(tokens[i], tokens);

                    // Añadir nodo al diccionario SIN datos globales (LUEGO)
                    dict.Add(tokens[i], 0, 0); 

                    // Añadir a la posting list del diccionario
                    dict.Add2Posting(tokens[i], docID, termCount, termFreq, termPos); // DOES not add tf_idf
                }

                datasetLine = reader.readLine();
                nDocs += 1;
            }

            docDict.save("docDict.txt"); // Does not require global variables
            reader.close();

        } catch (IOException e) {
            System.out.println(e);
        }

        /*dict.Print();
        System.out.println("\n\n");
        String token = "garfield";
        System.out.println("The token [ " + token + " ] is found in the following docIDs:");
        dict.getPostings(token).Print(); 
        */
        
        // Ahora datos globales
        dict.setGlobalStatistics(nDocs);


        dict.save("mainDictionary.txt");
        



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
