package Indexer;

import java.util.ArrayList; 


public class Preprocessing {

    public static Object[] Raw2Pairs(String[] rawLine){
        int rawLenght = rawLine.length;
        int [] docIdArr = new int[rawLenght];

        for (int i = 0; i < rawLenght; ++i){
           docIdArr[i] = Integer.parseInt(rawLine[i].substring(2, 8)); 
           rawLine[i] = rawLine[i].substring(12).toLowerCase();
        }
       
        return new Object[] {docIdArr, rawLine, 1};
    }

    // This tokenizer does not care for vignette separation
    public static String[] generalTokenizeStrings(String rawText){ // Takes one string, mostly because i cannot figure Arrays of arraylists
        ArrayList<String> tokens = new ArrayList<>();
        
        String holder = "";
        for(int c = 0; c < rawText.length(); ++c){
            if (!"".equals(holder) && rawText.charAt(c) == ' '){
                tokens.add(holder);
                holder = "";
            } else if (Character.isLetter(rawText.charAt(c))) holder+=rawText.charAt(c);            
        }
        String[] output = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); ++i) output[i] = tokens.get(i);
        
        return output;
    }
}
    

    

