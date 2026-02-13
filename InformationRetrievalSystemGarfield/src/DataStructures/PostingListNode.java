package DataStructures;

import java.util.ArrayList;

public class PostingListNode {
    private int docId;
    private PostingListNode next;

    // Statistics
    private int termCount;
    private float termFreq;
    private float tf_idf;
    ArrayList<Integer> termPos;
    

    // Falta añadir el resto de las estadisticas. 
    public PostingListNode(int docId, int termCount, float termFreq, float tf_idf, ArrayList<Integer> termPos){
        this.docId = docId;
        this.termCount = termCount;
        this.termFreq = termFreq;
        this.tf_idf = tf_idf;
        this.termPos = termPos;
        this.next = null;
    }

    public int getDocId(){
        return this.docId;
    }
    public void setDocId(int docId){
        this.docId = docId;
    }

    public int getTermCount() {
        return this.termCount;
    }
    public void setTermCount(int termCount) {
        this.termCount = termCount;
    }

    public float getTermFreq(){
        return this.termFreq;
    }
    public void setTermFreq(float termFreq){
        this.termFreq = termFreq;
    }

    public float getTf_idf(){
        return this.tf_idf;
    }
    public void setTf_idf(float tf_idf){
        this.tf_idf = tf_idf;
    }

    public ArrayList<Integer> getTermPos() {
        return this.termPos;
    }
    public void setTermPos(ArrayList<Integer> termPos) {
        this.termPos = termPos;
    }

    public PostingListNode getNext(){
        return this.next;
    }
    public void setNext(PostingListNode next){
        this.next = next;
    }

}
