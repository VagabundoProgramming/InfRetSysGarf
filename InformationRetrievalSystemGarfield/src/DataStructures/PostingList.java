package DataStructures;

public class PostingList {
    private PostingListNode root = null;

    public PostingList() {
        return;
    }

    public PostingListNode SearchKey(int docId){
        if (this.root == null) return null;
        PostingListNode temp = this.root;

        while(temp.getDocId() != docId && temp.getNext() != null){
            temp = temp.getNext();
        }
        if (temp.getDocId() == docId) return temp;
        return null;
    }

    // !! Falta poner los datos obtenidos extras super guays
    public void AddDoc (int docId){
        if (this.root == null){
            this.root = new PostingListNode(docId);
            return;
        }
        if (this.SearchKey(docId) != null) return;

        if (docId < this.root.getDocId()){
            PostingListNode temp = this.root;
            this.root = new PostingListNode(docId);
            this.root.setNext(temp);
            return;
        }

        PostingListNode temp = this.root;
        while (temp.getNext() != null && temp.getNext().getDocId() < docId){
            temp = temp.getNext();
        }
        PostingListNode new_node = new PostingListNode(docId);
        new_node.setNext(temp.getNext());
        temp.setNext(new_node);

        return;
    }

    public void DeleteDoc(int docId){
        if (this.root == null) return;
        if (this.root.getDocId() == docId){
            this.root = this.root.getNext();
            return;
        }

        PostingListNode temp = this.root;
        while (temp.getNext() != null && temp.getNext().getDocId() < docId) temp = temp.getNext();
        if (temp.getNext() == null) return;
        
        if (temp.getDocId() == docId){
            temp.setNext(temp.getNext().getNext());
        }
    }

    public void Print(){
        if (this.root == null) return;

        PostingListNode temp = this.root;
        while (temp.getNext() != null){
			System.out.print(temp.getDocId() + ", ");
			temp = temp.getNext();
		} 
		System.out.print(temp.getDocId() + "\n");
        return;
    }
    
}
