package Searcher;

import java.util.ArrayList;

import DataStructures.MaxHeap;

public class QueryProcessor {
    
    
    public QueryProcessor(){
        //maxHeap = new MaxHeap(); // Creo q no hara falta
        //searcher = seekerObj; // We will use it to search
    }

    // Revises that the query makes sense and edits it so the following code is easier.
    public String[] reviseEditQuery(String[] arguments){
        ArrayList<String> preOutput = new ArrayList<String>();
        int prev = 0;  // 0 means comparator (& or |)  1 means a search 
        
        //String comparators[] = {"|","&"}  // Does nothing, is just to remember which are the current supported comparators (or, and)

        for (int i = 0; i < arguments.length; ++i){
            //System.out.println(prev);
            //System.out.println(arguments[i]);

            if (i == 0){
                if (arguments[i].equals("|") || arguments[i].equals("&")){
                    System.out.println("First query element cannot be a comparator. [ " + arguments[i] + " ]");
                    return null;
                }
                preOutput.add(arguments[i]);
                prev = 1;
            }
            
            if (i != 0){
                if (!(arguments[i].equals("|") || arguments[i].equals("&")) && prev == 1){ // Additional and
                    preOutput.add("&");
                }
                if ((arguments[i].equals("|") || arguments[i].equals("&")) && prev == 0){
                    System.out.println("Comparator followed by another comparator, please remove one of them");
                    return null;
                }

                if (arguments[i].equals("|") || arguments[i].equals("&")) prev = 0;
                else prev = 1;
                preOutput.add(arguments[i]);
            }
        }

        String output[] = new String[preOutput.size()];
        for (int i = 0; i < preOutput.size(); ++i){
            output[i] = preOutput.get(i);
        }
        return output;
    }

    

    public Object[] getIDandScores(String subQuery, Seeker seeker){
        Object output[] = null;

        boolean applyNot = false;
        int index = 0;

        if(subQuery.substring(0,1).equals("-")){
            applyNot = true;
            index +=1;
        }
        if (subQuery.substring(index, index+2).equals("t:"))
            output = seeker.searchTerm(subQuery.substring(index+2));

        else if (subQuery.subSequence(index, index+2).equals("d:"))
            output = seeker.searchDate(subQuery.substring(index+2));

        else if (subQuery.subSequence(index, index+2).equals("w:")){
            output = seeker.searchDay(subQuery.substring(index+2));
        }
        else if (subQuery.subSequence(index, index+2).equals("s:"))
            output = seeker.searchString(subQuery.substring(index+2));

        else
            output = seeker.searchTerm(subQuery.substring(index));

        if (output == null){
            System.out.println("Subquery [ " + subQuery + " ] could not be processed");
            return null;
        }

        if (applyNot){
            ArrayList<Integer> ids = (ArrayList<Integer>) output[0];
            output = Searcher.Operators.Not(ids, seeker.getLiveEditObj());
        }

        return output;
    }

    private String[] querrySplitter(String query){
        
        ArrayList<String> subQueries = new ArrayList<String>();
        String holder = "";
        boolean insideSub = false;
        
        query = query.toLowerCase() + " ";

        for (int i = 0; i < query.length(); ++i){
            if (query.charAt(i) == '\"'){
                if (insideSub)
                   insideSub = false;
                else
                    insideSub = true; 
            }

            if (!"".equals(holder) && !Character.isLetterOrDigit(query.charAt(i))  && query.charAt(i)!=':' && query.charAt(i)!='/' && query.charAt(i)!='*' && !insideSub){
                if (query.charAt(i) == '"')
                    holder += '"';

                subQueries.add(holder);
                holder = "";
            } 
            
            else if (query.charAt(i)!=' ') {
                holder+=query.charAt(i);
            } 
            else if (query.charAt(i)==' ' && insideSub)
                holder += ' ';
        }

        String output[] = new String[subQueries.size()];
        for (int i = 0; i < subQueries.size(); ++i){
            output[i] = subQueries.get(i);
        }
        return output;
    }

    // Query in the shape of "t:Oddie & d:**/**/2004 & -t:Jon"
    public MaxHeap Proccess(String query, Seeker seeker){
        ArrayList<Integer> tempIds; // = new ArrayList<Integer>();
        ArrayList<Float> tempScores; // = new ArrayList<Float>();
        Object outHolder[];
        Object holder[];

        String[] qSplited = querrySplitter(query);//query.split(" "); // NO es correcto
        String[] qProcessed = reviseEditQuery(qSplited);

        outHolder = getIDandScores(qProcessed[0], seeker);
        
        for (int i = 2; i < qProcessed.length; i+=2){
            holder = getIDandScores(qProcessed[i], seeker);
            
            if (qProcessed[i-1].equals("|"))
                outHolder = Searcher.Operators.Or(outHolder, holder);
            else if (qProcessed[i-1].equals("&"))
                outHolder = Searcher.Operators.And(outHolder, holder);
            else {
                System.out.println("Operator not recognised");
                return null;
            }
        }

        tempIds = (ArrayList<Integer>) outHolder[0];
        tempScores = (ArrayList<Float>) outHolder[1];

        MaxHeap maxHeap = new MaxHeap();
        for (int i = 0; i < tempIds.size(); i++){
            maxHeap.add(tempIds.get(i), tempScores.get(i));    
        }
        maxHeap.buildMaxHeap();

        return maxHeap;
    }


}
