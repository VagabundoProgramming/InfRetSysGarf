package DataStructures;

import java.util.ArrayList;

//////////////////////////////////////
///  Remember, the root is 1 here  ///
//////////////////////////////////////

public class MaxHeap {
    
    ArrayList<Integer> docIDs;
    ArrayList<Float> score;

    public MaxHeap (){
        docIDs = new ArrayList<Integer>();
        score = new ArrayList<Float>();

        docIDs.add(null); // just to be there and make math easier
        score.add(null);
        
    }

    public void add(int docId, float score){
        this.docIDs.add(docId);
        this.score.add(score);
        buildMaxHeap();
    }

    public int peekID(){
        if (this.size() > 0)
            return this.docIDs.get(1);
        return -1;
    }
    public float peekScore(){
        if (this.size() > 0)
            return this.score.get(1);
        return -1;
    }

    public void pop() {
        for (int i = 1; i < size() ; ++i){
            this.docIDs.set(i, this.docIDs.get(i+1));
            this.score.set(i, this.score.get(i+1));
        }
        if (this.size() > 0){
            this.docIDs.remove(size());
            this.score.remove(size());
            buildMaxHeap();
        }
        return;
    }


    public int parent(int i) {
        if (i <= 0){
            System.out.println("Index must be an integer of value 1 or above.");
            return -1;
        }
        return i / 2;
    }

    public int left(int i) {
        if (i <= 0){
            System.out.println("Index must be an integer of value 1 or above.");
            return -1;
        }
        return 2 * i;
    }
    public int right(int i) {
        if (i <= 0){
            System.out.println("Index must be an integer of value 1 or above.");
            return -1;
        }
        return 2 * i + 1;
    }

    public int size(){
        return this.docIDs.size() - 1; // Because we ignore the null value. 
    }

    public void swap(int posA, int posB){
        if (!(posA > 0 && posB > 0)) return;

        int tempInt = this.docIDs.get(posA);
        float tempFloat = this.score.get(posA);
            
        this.docIDs.set(posA, this.docIDs.get(posB));
        this.score.set(posA, this.score.get(posB));
        this.docIDs.set(posB, tempInt);
        this.score.set(posB, tempFloat);
        return;
    }

    public void maxHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int largest;

        if (l <= size() && this.score.get(l) > this.score.get(i))
            largest = l;
        else largest = i;
        if (r <= size() && this.score.get(r) > this.score.get(largest))
            largest = r;
        if (largest != i){
            swap(i, largest);
            maxHeapify(largest);
        }
        return;
    }

    public void buildMaxHeap(){
        for (int x = size() / 2; x > 0; --x){
            maxHeapify(x);
        }
        return;
    }

    public void print() {
        int i;
        System.out.print("DocIDS: ");
        for (i = 1; i < size() + 1; i++) {
            System.out.print(this.docIDs.get(i) + ", ");
        }
        System.out.print("\nScores: ");
        for (i = 1; i < size() + 1; i++) {
            System.out.print(this.score.get(i) + ", ");
        }
        System.out.println();

    }

    public ArrayList<Integer> getDocIDs() {
        return this.docIDs;
    }
    public ArrayList<Float> getScore() {
        return this.score;
    }
}
        
