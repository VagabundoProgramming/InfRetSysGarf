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
import DataStructures.MaxHeap;
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
        //b.loadFromIndex("Temp.txt");
        //b.print();
        //b.save("Temp.txt");

        MaxHeap m = new MaxHeap();

        m.add(1,1);
        m.add(9,9);
        m.add(7,7);
        m.add(4,4);

        System.out.println(m.size());
        m.print();
        //m.swap(1, 3);
        m.buildMaxHeap();
        m.print();
        
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        m.pop();
        System.out.print("Peeking: ");
        System.out.println(m.peekID());
        
    
    }
}
