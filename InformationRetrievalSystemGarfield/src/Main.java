
import java.util.Scanner;
import Config.Constants;
import DataStructures.DocDict;
import DataStructures.MaxHeap;
import DataStructures.Dictionary;
import Indexer.LiveEdit;
import Indexer.Preprocessing;
import Searcher.QueryProcessor;
import Searcher.Seeker;

public class Main {
    public static void main(String[] args) {

        boolean exit = false;
        boolean exitSec = false;
        Scanner scanner = new Scanner(System.in);
        
        int option;
        int temp;
        int docID;
        String temp1String;
        String temp2String;
        MaxHeap maxHeap;

        Dictionary dict = new Dictionary(Constants.treeMinDegree);
        DocDict docDict = new DocDict(Constants.treeMinDegree);
        LiveEdit liveEdit = new LiveEdit(dict, docDict);
        Seeker seeker = new Seeker(liveEdit);
        QueryProcessor qProcessor = new QueryProcessor();

        dict.loadFromDataset("garfieldProcessed.txt");
        docDict.loadFromDataset("garfieldProcessed.txt");

        System.out.println("  ________                   _____ .___ __________   _________\n" + //
                           " /  _____/ _____   _______ _/ ____\\|   |\\______   \\ /   _____/\n" + //
                           "/   \\  ___ \\__  \\  \\_  __ \\\\   __\\ |   | |       _/ \\_____  \\ \n" + //
                           "\\    \\_\\  \\ / __ \\_ |  | \\/ |  |   |   | |    |   \\ /        \\\n" + //
                           " \\______  /(____  / |__|    |__|   |___| |____|_  //_______  /\n" + //
                           "        \\/      \\/                              \\/         \\/ \n");
        System.out.println("\nWelcome to GarfIRS - Your profesional software for text-based garfield comic retrieval\n");

        while (!exit){
            System.out.println("Options:");
            System.out.println(" 1. Load/Save Dataset \n" +
                               " 2. Modify Dataset in Disk \n" + 
                               " 3. Query \n" +
                               " 4. Query Syntax \n" + 
                               " 5. Exit \n");
            System.out.println("Choose option:");
            
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                // Load/Save Dataset
                case 1:
                    while(!exitSec){
                        System.out.println("Options:");
                        System.out.println(" 1. Preprocess Raw Data \n" +
                                           " 2. Load from Preprocessed Dataset \n" +
                                           " 3. Load from Indexes \n" +
                                           " 4. Save \n" + 
                                           " 5. Exit \n");
                        option = scanner.nextInt();
                        scanner.nextLine();

                        switch (option) {
                            case 1:
                                System.out.println("Introduce the name of the dataset (The file must be in the folder \"indexes\")");
                                waitABit();
                                temp1String = scanner.nextLine();
                                System.out.println("Introduce the name of the preprocessed dataset (Will be saved in \"indexes\")");
                                waitABit();
                                temp2String = scanner.nextLine(); 
                                Preprocessing.rawDatasetPreprocess(temp1String, temp2String);

                                System.out.println("Raw dataset Preprocessed, press enter to continue");
                                waitABit();
                                scanner.nextLine();

                                break;
                        
                            case 2:
                                System.out.println("Introduce the name of the preprocessed dataset:");
                                waitABit();
                                temp1String = scanner.nextLine();
                                dict.loadFromDataset(temp1String);
                                docDict.loadFromDataset(temp1String);

                                System.out.println("Dataset loaded onto memory, press enter to continue");
                                waitABit();
                                scanner.nextLine();

                                break;

                            case 3:
                                System.out.println("Introduce the filename for the term dictionary index:");
                                waitABit();
                                temp1String = scanner.nextLine();
                                System.out.println("Introduce the filename for the document dictionary index:");
                                waitABit();
                                temp2String = scanner.nextLine();

                                dict.loadFromIndex(temp1String);
                                docDict.loadFromDataset(temp2String);

                                System.out.println("Indexes loaded onto memory, press enter to continue");
                                waitABit();
                                scanner.nextLine();

                                break;

                            case 4:
                                System.out.println("Introduce a filename to save the term dictionary index:");
                                waitABit();
                                temp1String = scanner.nextLine();
                                System.out.println("Introduce a filename to save the document dictionary index:");
                                waitABit();
                                temp2String = scanner.nextLine();

                                dict.save(temp1String);
                                docDict.save(temp2String);

                                System.out.println("Indexes saved, press enter to continue");
                                waitABit();
                                scanner.nextLine();

                                break;

                            case 5:
                                exitSec = true;
                                System.out.println("Returning to main menu\n\n");
                                break;

                            default:
                                System.out.println("\n     Inapropiate Input\n");
                                break;
                        }
                    
                    }

                    exitSec = false;
                    break;
                    
                // Modify Dataset in Disk
                case 2:
                    while(!exitSec){
                        System.out.println("Options:");
                        System.out.println(" 1. Add a Document \n" +
                                           " 2. Modify a Document \n" +
                                           " 3. Delete a Document \n" +
                                           " 4. Save Changes to Files + \n" + 
                                           " 5. Exit \n");
                        option = scanner.nextInt();
                        scanner.nextLine();

                        switch (option) {
                            case 1:
                                System.out.println("Write the date of the document in the following format: DD/MM/YYYY");
                                waitABit();
                                temp1String = scanner.nextLine();
                                System.out.println("Write the contents of the comic:");
                                waitABit();
                                temp2String = scanner.nextLine();

                                docID = Integer.parseInt(temp1String.substring(0, 2) + temp1String.substring(3,5) + temp1String.substring(6));

                                liveEdit.runTimeAdd(docID, temp2String);
                                
                                System.out.println("Document added in disk, press enter to continue");
                                waitABit();
                                scanner.nextLine();
                                break;
                            
                            case 2:
                                System.out.println("Write the date of the document to modify in the following format: DD/MM/YYYY");
                                temp1String = scanner.nextLine();
                                System.out.println("Write the new contents of the comic:");
                                temp2String = scanner.nextLine();

                                docID = Integer.parseInt(temp1String.substring(0, 2) + temp1String.substring(3,5) + temp1String.substring(6));

                                liveEdit.runTimeMod(docID, temp2String);
                                
                                System.out.println("Document modified in disk, press enter to continue");
                                waitABit();
                                scanner.nextLine();
                                break;

                            case 3:
                                System.out.println("Write the date of the document to delete in the following format: DD/MM/YYYY");
                                waitABit();
                                temp1String = scanner.nextLine();

                                docID = Integer.parseInt(temp1String.substring(0, 2) + temp1String.substring(3,5) + temp1String.substring(6));

                                liveEdit.runTimeDel(docID);

                                System.out.println("Document deleted in disk, press enter to continue");
                                waitABit();
                                scanner.nextLine();
                                break;

                            case 4:
                                System.out.println("Write the filename for the term dictionary:");
                                waitABit();
                                temp1String = scanner.nextLine();
                                System.out.println("Write the filename for the document dictionary:");
                                waitABit();
                                temp2String = scanner.nextLine();

                                liveEdit.updateIndexes(temp1String, temp2String);

                                System.out.println("Files saved, press enter to continue");
                                waitABit();
                                scanner.nextLine();
                                break;
                            
                            case 5:
                                exitSec = true;
                                System.out.println("Returning to main menu\n\n");
                                break;

                            default:
                                System.out.println("\n     Inapropiate Input\n");
                                break;
                        }
                    }
                    seeker.updateLiveEdit(liveEdit);
                                
                    exitSec = false;
                    break;
                    
                
                // Query
                case 3:
                    System.out.println("Write the query (follow the guidelines):");
                    waitABit();
                    temp1String = scanner.nextLine();

                    maxHeap = qProcessor.Proccess(temp1String, seeker);
                    System.out.println("\nFound a total of " + maxHeap.size() + " results.");

                    while(!exitSec){
                        temp = Math.min(maxHeap.size(), 10);
                    
                        for (int i = 0; i < temp; ++i){
                            docID = maxHeap.peekID();

                            System.out.println(docID + "  " + liveEdit.getDocText(docID));
                            maxHeap.pop();
                        } 

                        if (maxHeap.size() > 0){
                            System.out.println("Input \"y\" to display the next 10, input anything else to return back");
                            waitABit();
                            temp1String = scanner.nextLine();

                            if (!temp1String.equals("y"))
                                exitSec = true;
                        }
                        else{
                            System.out.println("All results have been displayed, press enter to return");
                            waitABit();
                            scanner.nextLine();
                            exitSec = true;
                        }
                    
                    }
                    
                    exitSec = false;
                    break;

                // Query Info
                case 4:
                    System.out.println("\n - This are the guidelines to use the query search -  \n" + 
                                       " You can use 4 terms and single words which are treated as individual tokens \n" + 
                                       " 1. Search for individual tokens (t:token), words in your query that do not fall under other categories fall under this one \n" + 
                                       " 2. Search for dates (d:**/**/****), leave the asterisc to enable wildcards \n" +
                                       " 3. Day of the week (w:1) 1 is monday while 7 is sunday \n" +
                                       " 4. Strings of tokens (s:\"some text here\" it will search for the exact phrase you add, do not forget these (\"\") \n\n" +
                                       " You can add \"-\" before the previous operations to signal a NOT operation\n" + 
                                       " Also, you can use & for AND and | for OR between the tokens, if none are added it assigns AND \n\n" + 
                                       " Queries are read from left to right \n\n" + 
                                       " Example: Comics from 1984 or 1985 that include the string \" garfield is \" and is published on friday \n" +
                                       "    [ d:**/**/1984 | d:**/**/1985 & s:\"garfield is\" & w:5 ] (idk if there are some, do check it)");

                    System.out.println("\nWhenver you want, press enter to return");
                    waitABit();
                    scanner.nextLine();
                    break;
                
                case 5:
                    exit = true;
                    break;
                
                default:
                    System.out.println("\n     Inapropiate Input\n");
                    break;
            }


        }
        scanner.close();

        System.out.println("\n Thanks for checking GarfIRS! \n");
    }

    private static void waitABit(){
        try {
            Thread.sleep(500);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return;
    }
}
