import DataStructures.DictionaryNode;
import DataStructures.DocDict;
import DataStructures.DocDictNode;
import DataStructures.PostingList;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!\n");

        //subMain.GroundIndexer();
        DocDict a = new DocDict(2);
        a.load("docDict.txt");
        /*
        a.insert(1, 1, 1, 1);
        a.insert(2, 3, 77, 6);
       
        a.insert(3, 3, 3, 3);
        a.insert(4,4,4,4);
        a.insert(5, 5, 5, 5);
        a.insert(6,6,6,6);
        a.insert(7, 7, 7, 7);
        a.insert(8, 8, 8, 8);
        a.insert(9,9,9,9);
        a.insert(11,11,11,11);
        a.insert(22,22,22,22);
        */
        a.print();
        System.out.println();
        //Object b[] = a.search(2);
        //int n = (int) b[1];
        //DocDictNode node = (DocDictNode) b[0];
        //System.out.println("\n"+node.getUTokens(n));
        
        //a.save("docDict.txt");

    }
}
