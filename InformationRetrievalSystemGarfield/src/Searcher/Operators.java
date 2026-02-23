package Searcher;

import java.util.ArrayList;
import java.util.Stack;

import DataStructures.DocDictNode;
import Indexer.LiveEdit;

public class Operators {
    

    private static boolean idIn(int ID, ArrayList<Integer> array){
        
        for (int i = 0; i < array.size(); ++i){
            if (array.get(i) == ID) 
                return true;
        }
        return false;
    }

    public static int getIndex(int docID, ArrayList<Integer> array){

        for (int out = 0; out < array.size(); ++out){
            if (array.get(out) == docID) return out;
        }
        return -1;
    }

    // NOT
    public static Object[] Not(ArrayList<Integer> docIds, LiveEdit liveEditObj){
        ArrayList<Integer> docIDsOut = new ArrayList<Integer>();
        ArrayList<Float> scoresOut = new ArrayList<Float>();

        Stack <DocDictNode> nodeStack = new Stack<DocDictNode>();
		Stack <Integer> pointerStack = new Stack<Integer>();
        int docIDtemp;

        // Check disk memory first
        nodeStack.add(liveEditObj.getLiveDocDict().getRoot());
		pointerStack.add(0);
		while (!nodeStack.isEmpty()) {
			DocDictNode curr = nodeStack.peek();
			int index = pointerStack.peek();
				
			if (index == liveEditObj.getLiveDocDict().getMinimumDegree() * 4 - 1){ // Explor deeper
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
                docIDtemp = curr.getDocId(index/2);
                if (!(idIn(docIDtemp, docIds)) && !(idIn(docIDtemp, docIDsOut))){
                    docIDsOut.add(docIDtemp);
                    scoresOut.add((float) 1);
                }

				pointerStack.pop();
				pointerStack.add(index+1);
			}
			else {
				pointerStack.pop();
				pointerStack.add(index+1);	
			}
        }

        nodeStack.add(liveEditObj.getCurrDocDict().getRoot());
		pointerStack.add(0);

        while (!nodeStack.isEmpty()) {
			DocDictNode curr = nodeStack.peek();
			int index = pointerStack.peek();
				
			if (index == liveEditObj.getCurrDocDict().getMinimumDegree() * 4 - 1){ // Explor deeper
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
                docIDtemp = curr.getDocId(index/2);
                if (!(idIn(docIDtemp, docIds)) && !(idIn(docIDtemp, docIDsOut) && !(liveEditObj.isDocDeleted(docIDtemp)))){
                    docIDsOut.add(docIDtemp);
                    scoresOut.add((float) 1);
                }

				pointerStack.pop();
				pointerStack.add(index+1);
			}
			else {
				pointerStack.pop();
				pointerStack.add(index+1);
					
			}
        }

        Object[] output = new Object[]{docIDsOut, scoresOut}; // Array of docIDs, Array of scores
        return output;
    }

    // AND
    public static Object[] And(Object[] input1, Object[] input2){
        ArrayList<Integer> docIDsOut = new ArrayList<Integer>();
        ArrayList<Float> scoresOut = new ArrayList<Float>();

        ArrayList<Integer> input1IDs; // Remember, this may not be sorted
        ArrayList<Float> input1Scores;
        ArrayList<Integer> input2IDs; 
        ArrayList<Float> input2Scores;
        int currID;

        input1IDs = (ArrayList<Integer>) input1[0];
        input1Scores = (ArrayList<Float>) input1[1];
        input2IDs = (ArrayList<Integer>) input2[0];
        input2Scores = (ArrayList<Float>) input2[1];

        /*for (int i = 0; i < input1IDs.size(); i++){
            System.out.println(input1IDs.get(i));
        }
        System.out.println("----------");
        for (int i = 0; i < input2IDs.size(); i++){
            System.out.println(input2IDs.get(i));
        } */

        for (int i = 0; i < input1IDs.size(); ++i){
            currID = input1IDs.get(i);
            
            if (idIn(currID, input2IDs)){
                docIDsOut.add(currID);
                int index = getIndex(currID, input2IDs);
                
                scoresOut.add(input1Scores.get(i) * input2Scores.get(index));
            }
        }

        Object[] output = new Object[]{docIDsOut, scoresOut}; // Array of docIDs, Array of scores
        return output;
    }

    // OR
    public static Object[] Or(Object[] input1, Object[] input2){
        ArrayList<Integer> docIDsOut = (ArrayList<Integer>) input1[0];
        ArrayList<Float> scoresOut = (ArrayList<Float>) input1[1];

        ArrayList<Integer> input2IDs = (ArrayList<Integer>) input2[0];
        ArrayList<Float> input2Scores = (ArrayList<Float>) input2[1];
        int tempIndex;
        int currID;

    
        for (int i = 0; i < input2IDs.size(); ++i){
            currID = input2IDs.get(i);
            
            if ((idIn(currID, docIDsOut))){
                tempIndex = getIndex(currID, docIDsOut);
                scoresOut.set(tempIndex, scoresOut.get(tempIndex) + input2Scores.get(i));
            } else {
                docIDsOut.add(currID);
                scoresOut.add(input2Scores.get(i));
            }
        }

        Object[] output = new Object[]{docIDsOut, scoresOut}; // Array of docIDs, Array of scores
        return output;
    }

}
