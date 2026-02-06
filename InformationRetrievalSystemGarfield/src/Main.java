import DataStructures.Dictionary;
import DataStructures.PostingList;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        System.out.println(HelperFunctions.StringComp.GT("whatever", ""));


        //PostingList a = new PostingList();
        Dictionary b = new Dictionary(2);
        b.Insert("A");
        b.Insert("E");
        b.Insert("AB");
        b.Insert("B");
        b.Insert("K");
        b.Insert("J");
        b.Insert("A");

        b.Print();


    }
}
