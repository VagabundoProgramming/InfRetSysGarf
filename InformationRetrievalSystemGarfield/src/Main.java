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


        //Dictionary a = new Dictionary(8);
        //a.Print();
        //a.loadFromIndex("mainDictionary.txt");
        
        //a.loadFromDataset("garfieldProcessed.txt");
        //PostingList m = a.getPostings("you");
        //m.Print();

        //a.save("StupidFile.txt");
    
        //DocDict b = new DocDict(8);
        //b.loadFromDataset("garfieldProcessed.txt");
        //b.loadFromIndex("docDict.txt");


        //b.save("Temp.txt");
    }
}
