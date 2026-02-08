package HelperFunctions;

public class StringComp {

    public static boolean GT(String string1, String string2){ // String1 > String2
        
        for (int i = 0; i < Math.min(string1.length(), string2.length()) ; ++i){
            if (string1.charAt(i) > string2.charAt(i)) return true;
            else if (string1.charAt(i) < string2.charAt(i)) return false;
        }
        if (string1.length() > string2.length()) return true;
        return false;
    }

    public static boolean LT(String string1, String string2){ // String1 < String2
        
        for (int i = 0; i < Math.min(string1.length(), string2.length()) ; ++i){
            if (string1.charAt(i) < string2.charAt(i)) return true;
            else if (string1.charAt(i) > string2.charAt(i)) return false;
        }
        if (string1.length() < string2.length()) return true;
        return false;
    }
}
