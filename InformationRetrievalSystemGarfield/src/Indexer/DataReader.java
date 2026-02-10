package Indexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataReader {

    public static String[] ExtractLinesRaw(int start, int end){
        if (start > end) return null;
        int totalLines = end-start;
        String output[] = new String[totalLines];

        try{
            BufferedReader reader = new BufferedReader(new FileReader("Dataset\\garfield.txt"));
            
            for(int i = 0; i < start; ++i) reader.readLine();
            for(int i = 0; i < totalLines; ++i) output[i] = reader.readLine();

            reader.close();
            

        }catch(IOException e){
            e.printStackTrace();
        }

        for (int i = 0; i < totalLines; ++i){
            if (output[i] == null){
                String[] temp =  new String[i];
                for (int j = 0; j < i; ++j){
                    temp[j] = output[j];
                }
                output = temp;
                break;
            }
        }

        return output;
    
    }


      
}
