package Indexer;

public class PorterStemmer {
    // It takes Y after a consonant as a vowel. It has weird things with YYYY, but that is not a real word.

    public static int calcModule(String token){
        if (token.length() == 0) return 0;
        String temp = "";
        char lastAdded = ' ';
        for (int i = 0; i < token.length(); ++i){
            if (isVowel(token, i) && lastAdded != 'V'){
                temp += 'V';
                lastAdded = 'V';
            }
            else if (lastAdded != 'C'){
                temp += 'C';
                lastAdded = 'C';
            }
        }
        int m = (temp.charAt(0) == 'C') ? (temp.length()-1)/2 : temp.length()/2;
        return m;
    }

    public static boolean isVowel(String token, int pos){
        char letter = token.charAt(pos);
        if (letter=='a' || letter=='e' || letter=='i' || letter=='o' || letter=='u') return true;
        else if (letter=='y' && pos > 0 && !isVowel(token, pos-1)) return true;
        return false;
    }

    public static boolean endsWith(String token, String ending){  
        int tokenLen = token.length();
        int endLen = ending.length();

        if (tokenLen < endLen) return false;

        if (token.substring(tokenLen-endLen, tokenLen).equals(ending)) return true;
        return false;
    }

    // Ensures the word ends with a string and the beggining has a module grater than specified value. 
    public static boolean modGTAndEndsWith(String token, String ending, int minModule){
        if (token.length() <= ending.length()) return false;
        if (!endsWith(token, ending)) return false;
        if (calcModule(token.substring(0, token.length() - ending.length())) > minModule) return true;
        return false;
    }

    public static String substituteWith(String token, String ending, String newend){
        if (token.length() < ending.length()) return null;
        if (endsWith(token, ending))
            return token.substring(0, token.length() - ending.length()) + newend; // Usually rebundant
        return null;
    }

    public static boolean VowelIn(String token){
        for (int i = 0; i < token.length(); ++i){
            if (isVowel(token, i)) return true;
        }
        return false;
    }

    public static boolean EndDoubleCons(String token){
        int tokenLen = token.length();
        if (tokenLen < 2) return false;
        else if (token.charAt(tokenLen-1)==token.charAt(tokenLen-2) && !isVowel(token, tokenLen-1)) return true;
        return false;
    }
    
    public static boolean CVCClause(String token){
        if (token.length() < 3) return false;
        int tokenLen = token.length();
        if (!isVowel(token, tokenLen-3) && isVowel(token, tokenLen-2) && !isVowel(token, tokenLen-1)){
            if (!endsWith(token, "w") && !endsWith(token, "x") && !endsWith(token, "y")) return true;
        }
        return false;
    }


    public static String Stem(String token){

        // Step 1a
        if (endsWith(token, "sses")) token = substituteWith(token, "sses", "ss");
        else if (endsWith(token, "ies")) token = substituteWith(token, "ies", "i");
        else if (endsWith(token, "ss")); // I know it has no effect
        else if (endsWith(token, "s")) token = substituteWith(token, "s", "");

        // Step 1b
        boolean verbable = false; // no se como llamar a eto
        if (calcModule(token) > 0 && endsWith(token, "eed")) token = substituteWith(token, "eed", "ee");
        if (endsWith(token, "ed") && VowelIn(token.substring(0, token.length()-2))){
            token = substituteWith(token, "ed", "");
            verbable = true;
        }
        if (endsWith(token, "ing") && VowelIn(token.substring(0, token.length()-3))){
            token = substituteWith(token, "ing", "");
            verbable = true;
        }
        if (verbable){
            if (endsWith(token, "at")) token = substituteWith(token, "at", "ate");
            else if (endsWith(token, "bl")) token = substituteWith(token, "bl", "ble");
            else if (endsWith(token, "iz")) token = substituteWith(token, "iz", "ize");
            else if (EndDoubleCons(token) && !(endsWith(token, "l") || endsWith(token, "s") || endsWith(token, "z"))){
                token = token.substring(0, token.length()-1);
            } 
            else if (calcModule(token)==1 && CVCClause(token)) token = token + "e";
        }

        // Step 1c
        if (token.length() > 1 && VowelIn(token.substring(0, token.length()-1)) && token.charAt(token.length()-1) == 'y') token = substituteWith(token, "y", "i");

        // Step 2
        if      (modGTAndEndsWith(token, "ational", 0)) token = substituteWith(token, "ational", "ate");
        else if (modGTAndEndsWith(token, "tional", 0))  token = substituteWith(token, "tional", "tion");
        else if (modGTAndEndsWith(token, "enci", 0))    token = substituteWith(token, "enci", "ence");
        else if (modGTAndEndsWith(token, "anci", 0))    token = substituteWith(token, "anci", "ance");
        else if (modGTAndEndsWith(token, "izer", 0))    token = substituteWith(token, "izer", "ize");
        else if (modGTAndEndsWith(token, "abli", 0))    token = substituteWith(token, "abli", "able");
        else if (modGTAndEndsWith(token, "alli", 0))    token = substituteWith(token, "alli", "al");
        else if (modGTAndEndsWith(token, "entli", 0))   token = substituteWith(token, "entli", "ent");
        else if (modGTAndEndsWith(token, "eli", 0))     token = substituteWith(token, "eli", "e");
        else if (modGTAndEndsWith(token, "ousli", 0))   token = substituteWith(token, "ousli", "ous");
        else if (modGTAndEndsWith(token, "ization", 0)) token = substituteWith(token, "ization", "ize");
        else if (modGTAndEndsWith(token, "ator", 0))    token = substituteWith(token, "ator", "ate");
        else if (modGTAndEndsWith(token, "alism", 0))   token = substituteWith(token, "alism", "al");
        else if (modGTAndEndsWith(token, "iveness", 0)) token = substituteWith(token, "iveness", "ive");
        else if (modGTAndEndsWith(token, "fulness", 0)) token = substituteWith(token, "fulness", "ful");
        else if (modGTAndEndsWith(token, "ousness", 0)) token = substituteWith(token, "ousness", "ous");
        else if (modGTAndEndsWith(token, "ality", 0))   token = substituteWith(token, "ality", "al");
        else if (modGTAndEndsWith(token, "iviti", 0))   token = substituteWith(token, "iviti", "ive");
        else if (modGTAndEndsWith(token, "biliti", 0))  token = substituteWith(token, "biliti", "ble");
        
        // Step 3
        if      (modGTAndEndsWith(token, "icate", 0)) token = substituteWith(token, "icate", "ic");
        else if (modGTAndEndsWith(token, "ative", 0)) token = substituteWith(token, "ative", "");
        else if (modGTAndEndsWith(token, "alize", 0)) token = substituteWith(token, "alize", "al");
        else if (modGTAndEndsWith(token, "iciti", 0)) token = substituteWith(token, "iciti", "ic");
        else if (modGTAndEndsWith(token, "ical", 0))  token = substituteWith(token, "ical", "ic");
        else if (modGTAndEndsWith(token, "ful", 0))   token = substituteWith(token, "ful", "");
        else if (modGTAndEndsWith(token, "ness", 0))  token = substituteWith(token, "ness", "");

        // Step 4
        if      (modGTAndEndsWith(token, "al", 1))    token = substituteWith(token, "al", "");
        else if (modGTAndEndsWith(token, "ance", 1))  token = substituteWith(token, "ance", "");
        else if (modGTAndEndsWith(token, "ence", 1))  token = substituteWith(token, "ence", "");
        else if (modGTAndEndsWith(token, "er", 1))    token = substituteWith(token, "er", "");
        else if (modGTAndEndsWith(token, "ic", 1))    token = substituteWith(token, "ic", "");
        else if (modGTAndEndsWith(token, "able", 1))  token = substituteWith(token, "able", "");
        else if (modGTAndEndsWith(token, "ible", 1))  token = substituteWith(token, "ible", "");
        else if (modGTAndEndsWith(token, "ant", 1))   token = substituteWith(token, "ant", "");
        else if (modGTAndEndsWith(token, "ement", 1)) token = substituteWith(token, "ement", "");
        else if (modGTAndEndsWith(token, "ment", 1))  token = substituteWith(token, "ment", "");
        else if (modGTAndEndsWith(token, "ent", 1))   token = substituteWith(token, "ent", "");
        else if (modGTAndEndsWith(token, "sion", 1))  token = substituteWith(token, "sion", "s"); // Simpler 
        else if (modGTAndEndsWith(token, "tion", 1))  token = substituteWith(token, "tion", "t"); // Than the real rule
        else if (modGTAndEndsWith(token, "ou", 1))    token = substituteWith(token, "ou", "");
        else if (modGTAndEndsWith(token, "ism", 1))   token = substituteWith(token, "ism", "");
        else if (modGTAndEndsWith(token, "ate", 1))   token = substituteWith(token, "ate", "");
        else if (modGTAndEndsWith(token, "iti", 1))   token = substituteWith(token, "iti", "");
        else if (modGTAndEndsWith(token, "ous", 1))   token = substituteWith(token, "ous", "");
        else if (modGTAndEndsWith(token, "ive", 1))   token = substituteWith(token, "ive", "");
        else if (modGTAndEndsWith(token, "ize", 1))   token = substituteWith(token, "ize", "");

        // Step 5a
        if      (modGTAndEndsWith(token, "e", 1)) token = substituteWith(token, "e", "");
        else if (calcModule(token) == 1 && CVCClause(token) && endsWith(token, "e")) token = substituteWith(token, "e", "");

        // Step 5b
        if (modGTAndEndsWith(token, "ll", 1)) token = substituteWith(token, "ll", "l");

        return token;
    }

}
