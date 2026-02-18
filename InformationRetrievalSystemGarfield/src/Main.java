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
import Indexer.LiveEdit;
import Indexer.Preprocessing;
import Searcher.QueryProcessor;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!\n");

        //Preprocessing.rawDatasetPreprocess("garfield.txt", "garfieldProcessed.txt");

        Dictionary a = new Dictionary(Constants.treeMinDegree);
        DocDict b = new DocDict(Constants.treeMinDegree);
        
        a.loadFromDataset("garfieldProcessedMini.txt");;
        b.loadFromDataset("garfieldProcessedMini.txt");
        //a.loadFromIndex("dictMini.txt");
        //b.loadFromIndex("docMini.txt");


        LiveEdit c = new LiveEdit(a,b);
        //c.runTimeDel(780619); // ✅
        //c.runTimeDel(780620);
        c.runTimeAdd(177777, "Whew  garfield garfield garfield garfield, that was a close one - BE WARRY OF THE KNIVES!! - cheese"); //✅
        c.runTimeMod(780619, "Wow that was cute - I love cute");

        c.updateIndexes("docMini.txt", "dictMini.txt");
        
        //QueryProcessor processor = new QueryProcessor(a,b);

        //processor.Proccess("t:garfield and not and  t:mouse");
        
    
    }
}
