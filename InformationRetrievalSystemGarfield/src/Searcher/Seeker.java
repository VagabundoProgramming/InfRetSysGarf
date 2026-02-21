package Searcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

import DataStructures.DocDictNode;
import DataStructures.PostingList;
import DataStructures.PostingListNode;
import Indexer.LiveEdit;
import Indexer.PorterStemmer;
import Indexer.Preprocessing;

public class Seeker {
    
    LiveEdit liveEditObj;
    public Seeker (LiveEdit liveEdit){
        this.liveEditObj = liveEdit;
    }

    public void updateLiveEdit (LiveEdit liveEditObj) {
        this.liveEditObj = liveEditObj;
        return;
    }


    // Individual term search t:term
    public Object[] searchTerm(String token){
        ArrayList<Integer> docIDs = new ArrayList<Integer>();
        ArrayList<Float> scores = new ArrayList<Float>();

        String processedToken = PorterStemmer.Stem(Preprocessing.wordSquishing(token));

        PostingList postingsMem = this.liveEditObj.getCurrDictionary().getPostings(processedToken);
        PostingList postingsDisk = this.liveEditObj.getLiveDictionary().getPostings(processedToken);
        PostingListNode temp;

        if (postingsDisk != null) {
            temp = postingsDisk.getRoot();
            while (temp != null){
                docIDs.add(temp.getDocId());
                scores.add(temp.getTf_idf()); // I use tf_idf as a score
                temp = temp.getNext();
            }
        }

        if (postingsMem != null) {
            temp = postingsMem.getRoot();
            while (temp != null){
                docIDs.add(temp.getDocId());
                scores.add(temp.getTf_idf()); 
                
                temp = temp.getNext();
            }
        }

        Object[] output = new Object[]{docIDs, scores}; // Array of docIDs, Array of scores
        return output;
    }


    // Search by Specific Date d:DD/MM/YYYY
    private boolean isLeapYear(int year){
        if (year%400 == 0) return true;
        if (year%100 == 0) return false;
        if (year%4 == 0) return true;
        return false;
    }

    private boolean ensureDateFormat(String date){ // Format: DD/MM/YYYY
        if (date.length() != 10) return false;

        int day;
        int month;
        int year;
        try{
            if (date.substring(0,2).equals("**"))
                day = 1;
            else
                day = Integer.parseInt(date.substring(0, 2));
            
            if (date.substring(3,5).equals("**"))
                month = 1;
            else
                month = Integer.parseInt(date.substring(3, 5));
            
            if (date.substring(6).equals("****"))
                year = 1;
            else
                year = Integer.parseInt(date.substring(6));

        } catch (NumberFormatException e) {
            System.out.println(e);
            System.out.println("The date format was not correct, ensure it is DD/MM/YYYY");
            return false;
        }

        if (1 > month || month > 12) return false;

        if (month%2 == 1){
            if (1 > day || day > 31) return false;
        } else if (month == 2){
            if (isLeapYear(year)){
                if (1 > day || day > 29) 
                    return false;
            } else
                if (1 > day || day > 28) 
                    return false;
        } else
            if (1 > day || day > 30) return false;
        
        return true;
    }

    private int[] extractDateFromId(int docID){
        int output[] = new int[3]; // DD/MM/YYYY # Some logic to decide if it is 19YY or 20YY

        // Doc Id is maximum 6 digitis due to it being the date of the publication. 
        // Therefore we can assign the first two digits to year, two for month, and two for day
        // This actually creates errors if you add documents after 2178 or before 1978 (this will take like 50 years to be an issue)

        output[2] = docID/10000; // YY
        output[1] = (docID - output[2]*10000) / 100; // MM
        output[0] = docID - output[2]*10000 - output[1]*100; //DD

        if (output[2] >= 78)
            output[2] = 1900 + output[2];
        else
            output[2] = 2000 + output[2];

        return output;
    }

    public Object[] searchDate(String date){
        if (!ensureDateFormat(date)) return null;

        
        int day;
        int month;
        int year;
        int[] docIDdate;
       
        if (date.substring(0,2).equals("**"))
            day = -1;
        else
            day = Integer.parseInt(date.substring(0, 2));
        
        if (date.substring(3,5).equals("**"))
            month = -1;
        else
            month = Integer.parseInt(date.substring(3, 5));
        
        if (date.substring(6).equals("****"))
            year = -1;
        else
            year = Integer.parseInt(date.substring(6));

        ArrayList<Integer> docIDs = new ArrayList<Integer>();
        ArrayList<Float> scores = new ArrayList<Float>();

        Stack <DocDictNode> nodeStack = new Stack<DocDictNode>();
		Stack <Integer> pointerStack = new Stack<Integer>();

        // First search the disk 
		nodeStack.add(this.liveEditObj.getLiveDocDict().getRoot());
		pointerStack.add(0);
		while (!nodeStack.isEmpty()) {
			DocDictNode curr = nodeStack.peek();
			int index = pointerStack.peek();
				
			if (index == this.liveEditObj.getLiveDocDict().getMinimumDegree() * 4 - 1){ // Explor deeper
				nodeStack.pop();
				pointerStack.pop();
				if (!pointerStack.isEmpty()){
					int temp = pointerStack.pop();
					pointerStack.add(temp + 1);
				}
			}
			else if (index % 2 == 0 && curr.getChildren(index/2) != null){ // Explore deeper children
				nodeStack.add(curr.getChildren(index/2));
				pointerStack.add(0); 

			}
			else if (index % 2 == 1 && curr.getDocId(index/2) != 0) {// Do compute on keys // TENGO Q CHEKAER SI ESTA 
                docIDdate = this.extractDateFromId(curr.getDocId(index/2));

                if (day == docIDdate[0] || day == -1){
                    if (month == docIDdate[1] || month == -1){
                        if (year == docIDdate[2] || year == -1){
                            docIDs.add(curr.getDocId(index/2));
                            scores.add((float) 1);
                        }
                    }
                }                
				pointerStack.pop();
				pointerStack.add(index+1);
			}
			else {
				pointerStack.pop();
				pointerStack.add(index+1);	
			}
        }

        nodeStack.add(this.liveEditObj.getCurrDocDict().getRoot());
		pointerStack.add(0);

        while (!nodeStack.isEmpty()) {
			DocDictNode curr = nodeStack.peek();
			int index = pointerStack.peek();
				
			if (index == this.liveEditObj.getCurrDocDict().getMinimumDegree() * 4 - 1){ // Explor deeper
				nodeStack.pop();
				pointerStack.pop();
				if (!pointerStack.isEmpty()){
					int temp = pointerStack.pop();
					pointerStack.add(temp + 1);
				}
			}
			else if (index % 2 == 0 && curr.getChildren(index/2) != null){ // Explore deeper children
				nodeStack.add(curr.getChildren(index/2));
				pointerStack.add(0); 

			}
			else if (index % 2 == 1 && curr.getDocId(index/2) != 0) {// Do compute on keys // TENGO Q CHEKAER SI ESTA ELIMINADA  
                docIDdate = this.extractDateFromId(curr.getDocId(index/2));

                if (!(this.liveEditObj.isDocDeleted(curr.getDocId(index/2)))){
                    if (day == docIDdate[0] || day == -1){
                        if (month == docIDdate[1] || month == -1){
                            if (year == docIDdate[2] || year == -1){
                                docIDs.add(curr.getDocId(index/2));
                                scores.add((float) 1);
                            }
                        }
                    }
                }

				pointerStack.pop();
				pointerStack.add(index+1);
			}
			else {
				pointerStack.pop();
				pointerStack.add(index+1);
					
			}
        }

        Object[] output = new Object[]{docIDs, scores}; // Array of docIDs, Array of scores
        return output;
    }


    // Search by day of the week w:1 (1 is monday - 7 is sunday)
    public int getDayNumber(int docID) {
        int[] parsedDate;
        parsedDate = this.extractDateFromId(docID);
        String dateStr = Integer.toString(parsedDate[0]) + "/" + Integer.toString(parsedDate[1]) + "/" + Integer.toString(parsedDate[2]); 

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date date = formatter.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            System.out.println(e);
            return 0;
        }
    }

    public Object[] searchDay(String day){
        int dayWeek = Integer.parseInt(day);
        if (0 > dayWeek || dayWeek > 8) return null;
        if (dayWeek == 7) dayWeek = 1; // To align with Calendar.DAY_OF_WEEK
        else dayWeek += 1; 

        //System.out.println(getDayNumber(260222));

        ArrayList<Integer> docIDs = new ArrayList<Integer>();
        ArrayList<Float> scores = new ArrayList<Float>();
        int docWeekday;

        Stack <DocDictNode> nodeStack = new Stack<DocDictNode>();
		Stack <Integer> pointerStack = new Stack<Integer>();

        // First search the disk 
        System.out.println("Searching in disk memory");
		nodeStack.add(this.liveEditObj.getLiveDocDict().getRoot());
		pointerStack.add(0);
		while (!nodeStack.isEmpty()) {
			DocDictNode curr = nodeStack.peek();
			int index = pointerStack.peek();
				
			if (index == this.liveEditObj.getLiveDocDict().getMinimumDegree() * 4 - 1){ // Explor deeper
				nodeStack.pop();
				pointerStack.pop();
				if (!pointerStack.isEmpty()){
					int temp = pointerStack.pop();
					pointerStack.add(temp + 1);
				}
			}
			else if (index % 2 == 0 && curr.getChildren(index/2) != null){ // Explore deeper children
				nodeStack.add(curr.getChildren(index/2));
				pointerStack.add(0); 

			}
			else if (index % 2 == 1 && curr.getDocId(index/2) != 0) {// Do compute on keys // TENGO Q CHEKAER SI ESTA 
                docWeekday = getDayNumber(curr.getDocId(index/2));

                if (docWeekday == dayWeek){
                    docIDs.add(curr.getDocId(index/2));
                    scores.add((float) 1);    
                }
                
				pointerStack.pop();
				pointerStack.add(index+1);
			}
			else {
				pointerStack.pop();
				pointerStack.add(index+1);
					
			}
        }

        System.out.println("Searching in memory");
        nodeStack.add(this.liveEditObj.getCurrDocDict().getRoot());
		pointerStack.add(0);

        while (!nodeStack.isEmpty()) {
			DocDictNode curr = nodeStack.peek();
			int index = pointerStack.peek();
				
			if (index == this.liveEditObj.getCurrDocDict().getMinimumDegree() * 4 - 1){ // Explor deeper
				nodeStack.pop();
				pointerStack.pop();
				if (!pointerStack.isEmpty()){
					int temp = pointerStack.pop();
					pointerStack.add(temp + 1);
				}
			}
			else if (index % 2 == 0 && curr.getChildren(index/2) != null){ // Explore deeper children
				nodeStack.add(curr.getChildren(index/2));
				pointerStack.add(0); 

			}
			else if (index % 2 == 1 && curr.getDocId(index/2) != 0) {// Do compute on keys // TENGO Q CHEKAER SI ESTA ELIMINADA
                docWeekday = getDayNumber(curr.getDocId(index/2));

                if (docWeekday == dayWeek && !this.liveEditObj.isDocDeleted(curr.getDocId(index/2))){
                    docIDs.add(curr.getDocId(index/2));
                    scores.add((float) 1);    
                }

				pointerStack.pop();
				pointerStack.add(index+1);
			}
			else {
				pointerStack.pop();
				pointerStack.add(index+1);
					
			}
        }

        Object[] output = new Object[]{docIDs, scores}; // Array of docIDs, Array of scores
        return output;
    }


    // Multiple words one after the other.
    public boolean isFull(PostingList[] array){ 
        for (int i = 0; i < array.length; ++i){
            if (array[i] == null) return false;
        }
        return true;
    }
    public boolean isFull(PostingListNode[] array){ 
        for (int i = 0; i < array.length; ++i){
            if (array[i] == null) return false;
        }
        return true;
    }

    public boolean containsN(ArrayList<Integer> array, int index){
        for (int i = 0; i < array.size(); ++i){
            if (array.get(i) == index) return true;
        }

        return false;
    }

    public boolean sameDocId(PostingListNode[] array){
        if (array.length == 1) return true;
        
        for (int i = 0; i < array.length-1; ++i){
            if (array[i].getDocId() != array[i+1].getDocId()) return false;
        }
        
        return true;
    }

    

    public Object[] searchString(String query){ // s:"some tokens here"
        String[] tokens = Preprocessing.generalTokenizeStrings(query.substring(1, query.length()-1));
        int len = tokens.length; 
        PostingList[] postingsMem = new PostingList[len];
        //PostingList[] postingsDisk = new PostingList[len];
        PostingListNode[] postings = new PostingListNode[len];
        ArrayList[] termPos = new ArrayList[len]; // Not the most 
        boolean passes;
        int startingIndex;

        ArrayList<Integer> docIDs = new ArrayList<Integer>();
        ArrayList<Float> scores = new ArrayList<Float>();

        for (int i = 0; i < len; i++){
            tokens[i] = Indexer.PorterStemmer.Stem(Indexer.Preprocessing.wordSquishing(tokens[i]));
            postingsMem[i] = this.liveEditObj.getCurrDictionary().getPostings(tokens[i]); 
            //postingsDisk[i] = this.liveEditObj.getCurrDictionary().getPostings(tokens[i]);
            termPos[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < tokens.length; ++i)
            System.out.println(tokens[i]);


        int minID;
        int minIndex;
        boolean checks;
        int refIndex;    

        // Check first the disk if it has postings for all the words
        if (isFull(postingsMem)){
            for(int i = 0; i < postingsMem.length; ++i)
                postings[i] = postingsMem[i].getRoot();

            
            while (isFull(postings)){
                if (sameDocId(postings) && !this.liveEditObj.isDocDeleted(postings[0].getDocId())){
                    checks = true;
                    for (int j = 0; j < postings[0].getTermCount(); ++j){
                        refIndex = postings[0].getTermPos().get(j);
                        for (int k = 1; k < len; ++k){
                            if (!(containsN(postings[k].getTermPos(), refIndex + k))) checks = false;
                        }
                    }

                    if (checks){
                        docIDs.add(postings[0].getDocId());
                        scores.add((float) 1);
                    }

                    // Calculate if they match
                        // Add them to the score and docIDS
                }

                // This altought, not very efficiently, allows for advancing through the postings.
                minID = postings[0].getDocId();
                minIndex = 0;
                for (int j = 1; j < postings.length; ++j){
                    if (minID > postings[j].getDocId()){
                        minID = postings[j].getDocId();
                        minIndex = j;
                    }
                }
                postings[minIndex] = postings[minIndex].getNext(); 
            }
        }
        
        Object[] output = new Object[]{docIDs, scores}; // Array of docIDs, Array of scores
        return output;
            
            
            /* 
            
                if (!sameDocId(positings)){

                }

            int min;
            int minInd;
            while (isFull(positings)){



                for (int i = 0; i < len; ++i)
                    termPos[i] = positings[i].getTermPos();

                    System.out.println("Postings");
                    for (int i = 0; i < len; ++i){
                        for (int j = 0; j < termPos[i].size(); ++j){
                            System.out.print(termPos[i].get(j));
                            System.out.print(" ");
                        }
                        System.out.println("");
                    }
                    

                if (sameDocId(positings)){
                    for (int i = 0; i < termPos[0].size(); ++i){
                        passes = true;
                        startingIndex = (int) termPos[0].get(i);
                        for (int j = 1; j < len; ++j){
                            if (!(containsN(termPos[j], startingIndex+j))) passes = false;
                        }

                        if (passes){
                            docIDs = new ArrayList<Integer>();
                            scores = new ArrayList<Float>();


                            // Add stuff to the output
                        }
                    }
                }

                //min = postings;
                for (int i = 1; i < len; ++i){
                    
                }


                // Advance the lowest one?
            
            
            }

            
            //postingsDisk[0].getRoot();
        }




        for (int i = 0; i < tokens.length;++i){
            System.out.println(tokens[i]);
            
        }
            */


    }

}


