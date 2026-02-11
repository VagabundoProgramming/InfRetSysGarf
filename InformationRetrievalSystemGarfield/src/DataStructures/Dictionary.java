package DataStructures;

import java.util.ArrayList;

public class Dictionary {
	
	private int minimumDegree = 0;
	private DictionaryNode root = null;
	private int nTerms = 0;
	
	public Dictionary(int minimumDegree)
	{
		this.minimumDegree = minimumDegree;
		this.root = new DictionaryNode(minimumDegree);
		this.root.setLeaf(true);
	}
	
	public Object[] Search(String term)
	{
		//System.out.println("Searching for term [ " + term + " ]");
		return this.root.Search(term);
	}

	public PostingList getPostings(String term){
		return this.root.getPostings(term);
	}
	
	public boolean Add(String term, float docFreq, float idf)
	{
        if (this.Search(term) != null) return false;

		nTerms += 1;

		if (this.root.isFull())
		{
			DictionaryNode s = new DictionaryNode(this.minimumDegree);
			s.setChild(this.root, 0);
			this.root = s;
		}
		return this.root.InsertOnNonFullNode(term, docFreq, idf);
	}

	public void Add2Posting(String term, int docId, int termCount, float termFreq, ArrayList<Integer> termPos){
		Object [] searchResult = this.root.Search(term);
		if (searchResult == null) return;
		
		int index = (int) searchResult[1];
        DictionaryNode tempNode = (DictionaryNode) searchResult[0];
        
		tempNode.getPostings(index).add(docId, termCount, termFreq, termPos);
		return;
	}

	
	
	public void Print()
	{
		if (this.root == null) return;
		this.root.print();
	}

	public int getnTerms() {
		return this.nTerms;
	}

}
