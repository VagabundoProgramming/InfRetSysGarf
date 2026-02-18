package Searcher;

import java.util.ArrayList;

import DataStructures.Dictionary;
import DataStructures.DocDict;
import DataStructures.PostingList;
import DataStructures.PostingListNode;
import Indexer.PorterStemmer;
import Indexer.Preprocessing;

public class Search {
    
    Dictionary termDict;
    DocDict docDict;


    public Search (Dictionary termDict, DocDict docDict){
        this.termDict = termDict;
        this.docDict = docDict;
    }



    public Object[] searchTerm(String token){
        ArrayList<Integer> docIDs = new ArrayList<Integer>();
        ArrayList<Float> scores = new ArrayList<Float>();

        String processedToken = PorterStemmer.Stem(Preprocessing.wordSquishing(token));

        PostingList postings = termDict.getPostings(processedToken);
        if (postings == null)
            return null;

       
        PostingListNode temp = postings.getRoot();
        while (temp != null){
            docIDs.add(temp.getDocId());
            scores.add(temp.getTf_idf());
            temp.getNext();
        }

        Object[] output = new Object[]{docIDs, scores};

        return output;
    }


    // Date

    // Day


    // Multiple words one after the other. 
}
