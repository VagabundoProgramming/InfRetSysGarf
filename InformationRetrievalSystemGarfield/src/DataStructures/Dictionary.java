package DataStructures;

public class Dictionary {
    private int minDegree;
    private DictionaryNode root;

    public Dictionary(int minDegree){
        this.minDegree = minDegree;
        this.root = new DictionaryNode(minDegree);
        this.root.setLeaf(true);
    }


// For Search and Add And delete we have not included the necessary extra data. 
// Quiza incluso hacer q no sea recursivo pq se me hace feo 
    public Object[] Search(String term){
        return this.root.Search(term);
    }

    public boolean Insert(String term){
        System.out.println("Adding: " + term);
        if (this.root.isFull()){
            System.out.println("Root node is full");
            DictionaryNode s = new DictionaryNode(this.minDegree);
            s.setChild(this.root, 0);
            this.root = s;
        }
        return this.root.insertOnNonFullNode(term);
    }

    public boolean Delete(){
        return false;
    }

    public void Print(){
        if (this.root == null) return;
        this.root.print();
    }

    public void FullPrint(){
        //like print but with the extra data
    }
}

