package Indexer;

public class WordPreprocessing {

    public static Object[] Raw2Pairs(String[] rawText){
        int rawLenght = rawText.length;
        int [] docIdArr = new int[rawLenght];

        for (int i = 0; i < rawLenght; ++i){
           docIdArr[i] = Integer.parseInt(rawText[i].substring(2, 8)); 
           rawText[i] = rawText[i].substring(8);
        }
        /*Object[] output = new Object[2];
        output[0] = docIdArr;
        output[1] = rawText;

        System.out.println("Hey" + output[0]);
        */
        return new Object[] {docIdArr, rawText, 1};
    }

}
