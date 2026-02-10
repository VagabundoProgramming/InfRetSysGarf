public class subMain {
    // Moslty to avoid having to change operations for testing

    public static void GroundIndexer(){
        
        DataStructures.Dictionary b = new DataStructures.Dictionary(4);
        
        int start = 17120;
        int finish = 17138;
        

        //System.out.println(Indexer.PorterStemmer.Stem("ate"));
         

        String[] temp = Indexer.DataReader.ExtractLinesRaw(start, finish); //Reads lines from start-finish of the dataset and returns them as an array of lists
        Object[] temp2 = Indexer.Preprocessing.Raw2Pairs(temp); // Given an array of raw lines it resutns the doc id array and the text array
        
        int[] docIds = (int[]) temp2[0]; // Importante Acordarse de esto
        String[] texts = (String[]) temp2[1];
        
        /*for(int i = 0; i < docIds.length; ++i){
            System.out.println(docIds[i] + " | " + texts[i]);
        }*/
       
        System.out.println("Retrieved a total of " + docIds.length + " documents.");

        String tempStem;
        for (int i = 0; i < docIds.length; ++i){
            String[] tempTok;
            tempTok = Indexer.Preprocessing.generalTokenizeStrings(texts[i]); // Mirar de hacerlo a tronco para q tire con multiples docs a la vez?
            System.out.print("Tokens doc " + docIds[i] + " : ");     
            for (int j = 0; j < tempTok.length; ++j){
                tempStem = Indexer.PorterStemmer.Stem(tempTok[j]);
                //System.out.print(tempStem + " / ");
                System.out.println(tempStem);
                tempStem = Indexer.Preprocessing.wordSquishing(tempStem);
                b.Add(Indexer.PorterStemmer.Stem(tempStem));
            } 
            System.out.println();
        }
        System.out.println("\n\n\n\n\n\n\n");
        System.out.println(b.Search("here"));
        b.Print();
        
        return;
    }
}
