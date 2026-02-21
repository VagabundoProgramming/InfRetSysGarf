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
import DataStructures.PostingListNode;
import DataStructures.Dictionary;
import DataStructures.DictionaryNode;
import Indexer.LiveEdit;
import Indexer.Preprocessing;
import Searcher.QueryProcessor;
import Searcher.Seeker;
import Searcher.Seeker;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!\n");

        //Preprocessing.rawDatasetPreprocess("garfield.txt", "garfieldProcessed.txt");

        Dictionary a = new Dictionary(Constants.treeMinDegree);
        DocDict b = new DocDict(Constants.treeMinDegree);
        
        //a.loadFromDataset("garfieldProcessedMini.txt");
        //b.loadFromDataset("garfieldProcessedMini.txt");
        a.loadFromIndex("dictMini.txt");
        b.loadFromIndex("docDictMini.txt");
        a.save("dictMini.txt");

        /*System.out.println("HERE:");
        PostingListNode temp = a.getPostings("garfield").getRoot();
        while (temp!= null){
            System.out.println((temp.getTf_idf()));
            temp = temp.getNext();
        }
        
        System.out.println("THERE:");*/


        //a.save("dictMini.txt");
        //b.save("docDictMini.txt");



        
        
        LiveEdit c = new LiveEdit(a,b);
        //c.runTimeDel(780619); // ✅
        //c.runTimeDel(780620);
        //c.runTimeAdd(177777, "Whew  garfield garfield garfield garfield, that was a close one - BE WARRY OF THE KNIVES!! - cheese"); //✅
        //c.runTimeMod(780619, "Wow that was cute - I love cute");
        //c.updateIndexes("docMini.txt", "dictMini.txt");

        Seeker seeker = new Seeker(c);

        c.runTimeAdd(780630, "me encanta el fortnite");
        //c.runTimeDel(780624);


        Object holder[];
        ArrayList<Integer> docIDsOut = new ArrayList<Integer>();
        ArrayList<Float> scores = new ArrayList<Float>();
        //holder = seeker.searchDate("24/06/1978");
        //holder = seeker.searchTerm("garfield");
        //holder = seeker.searchDay("1");
        holder = seeker.searchString("\"groveling is not\"");

        docIDsOut = (ArrayList<Integer>) holder[0];
        scores = (ArrayList<Float>) holder[1];

        seeker.updateLiveEdit(c);
        System.out.println("\nThese are the matches found:");
        for (int i = 0; i < docIDsOut.size(); i++){
            System.out.print(docIDsOut.get(i));
            System.out.print("  ");
            System.out.println(scores.get(i));
        }
        
        //QueryProcessor processor = new QueryProcessor(seeker);


        //processor.Proccess("t:garfield and not and  t:mouse");  

    }
}
