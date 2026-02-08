import DataStructures.Dictionary;
//import DataStructures.PostingList;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        //PostingList a = new PostingList();
  



        
        
        int start = 17130;
        int finish = 17150;
        String[] temp = Indexer.DataReader.ExtractLines(start, finish); //Reads lines from start-finish of the dataset and returns them as an array of lists
        Object[] temp2 = Indexer.WordPreprocessing.Raw2Pairs(temp); // Given an array of raw lines it resutns the doc id array and the text array

        int[] docIds = (int[]) temp2[0]; // Importante Acordarse de esto
        String[] texts = (String[]) temp2[1];
        
        for(int i = 0; i < docIds.length; ++i){
            System.out.println(docIds[i] + " | " + texts[i]);
        } 

        


    }
}
