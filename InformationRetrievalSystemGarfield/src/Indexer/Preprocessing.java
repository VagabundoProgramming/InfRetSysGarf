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

    // Some words like long can be written as loooong, to avoid muiltiple of these instances, we squish words
    public static String wordSquishing(String token){
        if (token.length() < 2) return token;
        String last2 = token.substring(0, 2);
        String output = last2;
        for (int i = 2; i < token.length(); ++i){
            
            if (!(last2.charAt(0) == last2.charAt(1) &&  token.charAt(i) == last2.charAt(0))) output += token.charAt(i);
            last2 = last2.substring(1) + token.charAt(i);
            
        } // DEGREE == 2
        return output; // Quiza juntarlas hasta 1 letra maxima para evitar este error, no creo q pase nada por probar
        // I could also try phonetic indexing // Or multiple even, I will stick with this for tthe moment
    }

    // This tokenizer does not care for vignette separation
    public static String[] generalTokenizeStrings(String rawText){ // Takes one string, mostly because i cannot figure Arrays of arraylists
        ArrayList<String> tokens = new ArrayList<>();
        
        String holder = "";
        for(int c = 0; c < rawText.length(); ++c){
            if (!"".equals(holder) && (rawText.charAt(c) == ' ' || rawText.charAt(c) == '-')){
                tokens.add(holder);
                holder = "";
            } else if (Character.isLetter(rawText.charAt(c))) holder+=rawText.charAt(c);            
        }
        String[] output = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); ++i) output[i] = tokens.get(i);
        
        return output;
    }
}
    

    

