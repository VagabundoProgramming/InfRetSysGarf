package Searcher;

import java.util.ArrayList;

import DataStructures.MaxHeap;
import Indexer.Preprocessing;

public class QueryProcessor {
    
    MaxHeap maxHeap;
    Seeker searcher;

    public QueryProcessor(Seeker seekerObj){
        maxHeap = new MaxHeap();
        searcher = seekerObj; // We will use it to search
    }


    public boolean reviseQuery(ArrayList<Object> pQuery, ArrayList<Object> pScores){

        int bracketCounter[] = {0,0};
        Object temp = null;

        for (int i = 0; i < pQuery.size(); ++i){
            temp = pQuery.get(i);

            if (temp instanceof String){
                System.out.println(temp);

                if (temp.equals("(")) bracketCounter[0] += 1;
                else if (temp.equals(")")){
                    bracketCounter[1] += 1;
                    if (bracketCounter[0] > bracketCounter[1]) 
                        return false;
                } 
                else if (temp.equals("not")){
                    if (i+1 > pQuery.size()) return false;
                    if (pQuery.get(i+1) instanceof String && !(pQuery.get(i+1).equals("("))) return false;
                }
                else{
                    if (i-1 < 0) return false;
                    if (pQuery.get(i-1) instanceof String) return false;
                    if (i+1 > pQuery.size()) return false;
                    if (pQuery.get(i+1) instanceof String && !(pQuery.get(i+1).equals("not"))) return false;
                }

            }

        }

        if (bracketCounter[0] != bracketCounter[1]) return false;
        return true;
    }

    public void Proccess(String query){
        ArrayList<Object> pQuery = new ArrayList<Object>();
        ArrayList<Object> pScores = new ArrayList<Object>();
        String holder = "";
        String prev = null;
        char currChar;
        Object searchResult[];
        String token;
        // t:token
        // d:date
        // d:day
        // s:"string of tokens"

        query += " ";

        for (int queryPointer = 0; queryPointer < query.length(); ++queryPointer){
            currChar = query.charAt(queryPointer);

            System.out.println(holder);

            if (currChar == ' ' || currChar == '(' || currChar == ')' ){
                System.out.println("DO we get here?");
                if (currChar == '('){
                    if (prev == "postings"){

                    }
                    pQuery.add("(");
                    pScores.add("(");
                }
                else if (currChar == ')'){
                    pQuery.add(")");
                    pScores.add(")");
                }
                System.out.println(holder.substring(0,2));
                if (holder.length() > 2 && holder.substring(0, 2).equals("t:")){
                    
                    token = Preprocessing.PreprocessWord(holder.substring(2).toLowerCase());
                    System.out.print("Searching postings for { " + token + " }");
                    searchResult = searcher.searchTerm(token);
                    pQuery.add((ArrayList<Integer>) searchResult[0]);
                    pScores.add((ArrayList<Float>) searchResult[1]); // Im gonna use tf_idf for the moment
                }
                else if (holder.equals("and")){
                    pQuery.add("and");
                    pScores.add("and");
                }
                else if (holder.equals("or")){
                    pQuery.add("or");
                    pScores.add("or");    
                }
                else if (holder.equals("xor")){
                    pQuery.add("xor");
                    pScores.add("xor");
                }
                else if (holder.equals("nand")){
                    pQuery.add("nand");
                    pScores.add("nand");
                } 
                else if (holder.equals("not")){
                    pQuery.add("not");
                    pScores.add("not");
                    
                }
                holder = "";
            }
            
            holder += currChar;
            
        }

        // Delete extra nots
        Object temp = null;
        for (int i = 0; i < pQuery.size(); ++i){
            if (temp != null && temp.equals("not") && pQuery.get(i).equals("not")){
                pQuery.remove(i);
                pScores.remove(i);
                i--;
            }
        }

        if (!(reviseQuery(pQuery, pScores))){
            System.out.println("The query is not formated properly.");
            return;
        }


        

        return;
    }


}
