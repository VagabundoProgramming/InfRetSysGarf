package Indexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList; 


public class Preprocessing {

    public static Object[] Raw2Pairs(String[] rawLine){
        int rawLenght = rawLine.length;
        int [] docIdArr = new int[rawLenght];

        for (int i = 0; i < rawLenght; ++i){
           docIdArr[i] = Integer.parseInt(rawLine[i].substring(2, 8)); 
           rawLine[i] = rawLine[i].substring(12).toLowerCase();
        }
       
        return new Object[] {docIdArr, rawLine};
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
            //if (!"".equals(holder) && isLetter(rawText.charAt(c) == ' ' || rawText.charAt(c) == '-')){
            if (!"".equals(holder) && !Character.isLetter(rawText.charAt(c))){
                tokens.add(holder);
                holder = "";
            } else if (Character.isLetter(rawText.charAt(c))) holder+=rawText.charAt(c);            
        }
        String[] output = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); ++i) output[i] = tokens.get(i);
        
        return output;
    }


    public static void rawDatasetPreprocess(String rawFilename, String curatedFilename){
        // Deletes duplicate lines due to long text
        try (BufferedReader reader = new BufferedReader(new FileReader(Config.Constants.mainpath + "//Dataset//" + rawFilename))){
            try (FileWriter writer = new FileWriter(Config.Constants.mainpath + "//Dataset/" + curatedFilename)){

                String temp; // CURR LINE
                String lineHolder = null; // sum oof multiple lines of needed
                String prevRefID = null;
                String currID;

                temp = reader.readLine();
                if (temp != null) currID = temp.substring(0, 9); 
                else return;
                
                while (temp != null){
                    if (prevRefID != null && prevRefID.equals(currID)){ // IF equal, join curr + prev
                        temp = lineHolder + temp.substring(11);
                    } 
                    else if (lineHolder!= null){
                        writer.write(lineHolder + "\n");
                    
                    } 
                    lineHolder = temp;
                    prevRefID = currID;
                    
                    temp = reader.readLine();
                    if (temp != null) currID = temp.substring(0,9);
                }

                } catch (IOException e) {
                    System.out.println(e);
                }


        } catch (IOException e){
            System.out.println(e);
        }
        return;
    }

    public static int nUTokens (String [] tokens){
        ArrayList<String> uTokens = new ArrayList<>();
        
        for (int i = 0; i < tokens.length; ++i) {
            if (!(uTokens.contains(tokens[i]))) uTokens.add(tokens[i]);
        }

        return uTokens.size();
    }

    public static int nVignettes(String rawtext){
        int output = 1;

        for (int i = 0; i < rawtext.length() - 4 ; ++i){
            if (rawtext.substring(i, i+3).equals(" - ")) output += 1;
        }

        return output;
    }

    public static int termCount(String token, String[] tokens){
        int output = 0;

        for (int i = 0; i < tokens.length; ++i){
            if (token.equals(tokens[i])) output += 1;
        }
        return output;
    }

    public static ArrayList<Integer> termPos(String token, String[] tokens){
        ArrayList<Integer> output = new ArrayList<Integer>();

        for (int i = 0; i < tokens.length; ++i){
            if (token.equals(tokens[i])) output.add(i);
        }
        return output;
    }

    /*public static void createMainDictIndex(String datafile, String indexfile){
        try (BufferedReader reader = new BufferedReader(new FileReader(Config.Constants.mainpath + "//Dataset//" + datafile))){
            try (FileWriter writer = new FileWriter(Config.Constants.mainpath + "//InformationRetrievalSystemGarfield//indexes//" + indexfile)){
                // COment
                a


            } catch (IOException e) {
                System.out.println(e);
            }
        } catch (IOException e){
            System.out.println(e);
        } 
        return;
    }*/
}


    

    

