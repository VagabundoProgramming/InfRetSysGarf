package DataStructures;

public class DictionaryNode {
    private int minDegree = 0;
    private String terms[]; //keys
    private DictionaryNode children[];
    private boolean leaf = false;

    // AQUI FALTARAN COSAS COMO LOS POSTINGS



    //private int n = 0; //tecnicamente es simplemente la lenght de children asi q quiza lo borre y ya

    public DictionaryNode(int minDegree){
        this.minDegree = minDegree;
        this.terms = new String[2*minDegree-1];
        this.children = new DictionaryNode[2*minDegree];

        for (int i = 0; i < 2 * minDegree - 1; ++i) this.terms[i] = "";
        for (int i = 0; i < 2 * minDegree; ++i) this.children[i] = null;
        System.out.println("Some info here:");
        System.out.println("This node can have up to " + this.nChildren()  + " childrens, also tested by: " + this.children.length);
        System.out.println("This node has " + this.);
    
    }

    public int nChildren(){
        return this.children.length;
    }

    public boolean isLeaf(){
        return this.leaf;
    }
    public void setLeaf(boolean leafStatus){
        this.leaf = leafStatus;
    }

    public boolean isFull(){
        return (this.children.length == 2 * minDegree - 1) ? true : false; // I like it despite not being redable
    }

    public DictionaryNode[] getChildren(){
        return this.children;
    }
    public DictionaryNode getIthChild(int i){
        if (i >= 0 && i <= this.nChildren()) return this.children[i]; 
        return null;
    }
    public void setChildren(DictionaryNode[] children){
        this.children = children;
    }
    public void setChild(DictionaryNode node, int i){
        if (i >= 0 && i <= this.nChildren()) this.children[i] = node;    
    }

    public Object[] Search(String term){
        Object[] searchResult = new Object[2];

        int i = 0;
        while (i < this.nChildren() && HelperFunctions.StringComp.GT(this.terms[i], term)){
            ++i;
        }
        if (i < this.nChildren() && this.terms[i] == term){
            searchResult[0] = this;
            searchResult[1] = i;
            return searchResult;
        }
        else if (this.leaf == true) return null;
        else return this.children[i].Search(term);
    }

    public void split(int i){
        if (this.isFull()) return;
        if (i > this.nChildren()) return;
        if (this.children[i] == null) return;
        if (!this.children[i].isFull()) return; 
        
        DictionaryNode z = new DictionaryNode(this.minDegree);
        DictionaryNode y = this.children[i];
        z.leaf = y.leaf;
        
        int j;
        for (j = 0; j < this.minDegree - 1; ++j) z.terms[j] = z.terms[j + this.minDegree];
        for (j = 0; j < this.minDegree; ++j) z.children[j] = y.children[j + this.minDegree];

        for (j = this.children.length; j > i; --j) this.children[j+1] = this.children[j];
        this.children[j+1] = z;

        for (j = this.children.length - 1; j > i - 1; --j) this.terms[j + 1] = this.terms[j];
        this.terms[j+1] = y.terms[this.minDegree - 1];

        for(j = this.minDegree; j < 2 * this.minDegree - 1; ++j){
            y.terms[j] = "";
            y.children[j] = null;
        }
        y.terms[this.minDegree - 1] = "";
        y.children[j] = null;
    }

    public boolean insertOnNonFullNode(String term){
        if (this.isFull()) return false;

        int i = this.children.length - 1;
        


        if (this.isLeaf()){
            System.out.println("This node is Leaf");
            System.out.println(this.terms.length + " " + i);
            while (i >= 0 && HelperFunctions.StringComp.LT(term, this.terms[i])){
                System.out.println("I think it ndoes not reach here");
                this.terms[i+1] = this.terms[i];
                --i;
            }
            this.terms[i+1] = term;
            return true; // :D
        }
        else{
            System.out.println("This node is not Leaf");
            while(i >= 0 && HelperFunctions.StringComp.LT(term, this.terms[i])) --i;;
            i = i++;
            if (this.children[i].isFull()){
                this.split(i);
                if (HelperFunctions.StringComp.GT(term, this.terms[i])) ++i;
            }
            return this.children[i].insertOnNonFullNode(term);
        }
    }


    public void print(){
        int i = 0;
        for (i = 0; i < this.children.length; ++i){
            this.children[i].print();
            System.out.print(this.terms[i] + " ");
        }
        if (this.children[i] != null) this.children[i].print();
    }
}


