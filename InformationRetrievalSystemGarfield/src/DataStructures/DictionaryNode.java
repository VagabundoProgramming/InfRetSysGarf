package DataStructures;

public class DictionaryNode {
	
	private int minimumDegree = 0;
	private String terms[];
	private DictionaryNode children[];
	private boolean leaf = false;
	private int n = 0;

	// Statistics
	private int docCount[];     //N docs that contain x term, updated automatically
	private float docFreq[];    // Global
	private float idf[]; 		// Global
	private PostingList postings[];
	
	//private int termCount[];
	//private float termFreq[];
	//private float tf_idf[];
	//ArrayList<Integer>[] termPos; // = (ArrayList<Integer>[]) new ArrayList[];
        

	
	public DictionaryNode(int minimumDegree)
	{
		this.minimumDegree = minimumDegree;
		this.terms = new String[2*minimumDegree - 1];
		this.children = new DictionaryNode[2*minimumDegree];

		this.docCount = new int[2*minimumDegree - 1];
		this.docFreq = new float[2*minimumDegree - 1];
		this.idf = new float[2*minimumDegree - 1];
		this.postings = new PostingList[2*minimumDegree - 1];
		
		
		for (int i = 0; i < 2*minimumDegree - 1; ++i){
			this.terms[i] = "";
			this.docCount[i] = 0;
			this.docFreq[i] = 0;
			this.idf[i] = 0;
			this.postings[i] = new PostingList(); // DIRIA Q OK
		}
		for (int i = 0; i < 2*minimumDegree; ++i)
			this.children[i] = null;
	}
	
	public boolean isLeaf()
	{
		return this.leaf;
	}
	
	public boolean isFull()
	{
		if (n == 2*minimumDegree-1) return true;
		return false;
	}
	
	public void setLeaf(boolean leafState)
	{
		this.leaf = leafState;
	}
	
	public void setChild(DictionaryNode c, int i)
	{
		this.children[i] = c;
	}
	
	public Object[] Search(String k)
	{
		//for (int i = 0; i < 2*this.minimumDegree-1; ++i) System.out.print(this.docID[i] + " / ");
		//System.out.println("\n---------------------");
		// Object[0] = BNode
		// Object[1] = id
		
		Object[] searchResult = new Object[2];
		
		int i = 0;
		
		while (i < this.n && HelperFunctions.StringComp.LT(this.terms[i], k)) // AQUI
			++i;
		
		if (i < this.n && this.terms[i].equals(k))
		{
			searchResult[0] = this;
			searchResult[1] = i;
			return searchResult;
		}
		else if (this.leaf == true)
				return null;
		else
			return this.children[i].Search(k);
	}

	public PostingList getPostings(String token){
		int i = 0;

		while (i < this.n && HelperFunctions.StringComp.LT(this.terms[i], token)) // AQUI
			++i;
		
		if (i < this.n && this.terms[i].equals(token))
		{
			return this.postings[i];
		}
		else if (this.leaf == true)
				return null;
		else
			return this.children[i].getPostings(token);

	}
	
	private void Split(int i)
	{
		int j = 0;
		
		if (this.isFull()) return;
		if (i > this.n) return;
		if (this.children[i] == null) return;
		if (!this.children[i].isFull()) return;
		
		DictionaryNode z = new DictionaryNode(this.minimumDegree);
		DictionaryNode y = this.children[i];
		z.leaf = y.leaf;
		z.n = this.minimumDegree - 1;
		
		for (j = 0; j < this.minimumDegree - 1; ++j){
			z.terms[j] = y.terms[j + this.minimumDegree];
			z.docCount[j] = y.docCount[j + this.minimumDegree];
			z.docFreq[j] = y.docFreq[j + this.minimumDegree];
			z.idf[j] = y.idf[j + this.minimumDegree];
			z.postings[j] = y.postings[j + this.minimumDegree];
		}
		for (j = 0; j < this.minimumDegree; ++j)
			z.children[j] = y.children[j + this.minimumDegree];
		
		y.n = this.minimumDegree - 1;
		
		for (j = this.n; j > i; --j)
			this.children[j+1] = this.children[j];
		this.children[j+1] = z;
			
		for (j = this.n-1; j > i-1; --j){
			this.terms[j+1] = this.terms[j];
			this.docCount[j+1] = this.docCount[j];
			this.docFreq[j+1] = this.docFreq[j];
			this.idf[j+1] = this.idf[j];
			this.postings[j+1] = this.postings[j];
		}
		this.terms[j+1] = y.terms[this.minimumDegree - 1];
		this.docCount[j+1] = y.docCount[this.minimumDegree - 1];
		this.docFreq[j+1] = y.docFreq[this.minimumDegree - 1];
		this.idf[j+1] = y.idf[this.minimumDegree - 1];
		this.postings[j+1] = y.postings[this.minimumDegree - 1];

		this.terms[i] = y.terms[this.minimumDegree - 1];
		this.docCount[i] = y.docCount[this.minimumDegree - 1];
		this.docFreq[i] = y.docFreq[this.minimumDegree - 1];
		this.idf[i] = y.idf[this.minimumDegree - 1];
		this.postings[i] = y.postings[this.minimumDegree - 1];


		this.n = this.n + 1;
		
		for (j = this.minimumDegree; j < 2*this.minimumDegree-1; ++j){
			y.terms[j] = "";
			y.children[j] = null;
			y.docCount[j] = 0;
			y.docFreq[j] = 0;
			y.idf[j] = 0;
			y.postings[j] = new PostingList();
		}
		y.terms[this.minimumDegree - 1] = "";
		y.children[j] = null;
	}
	
	public boolean InsertOnNonFullNode(String term, float docFreq, float idf) // Creo q la tf_idf la tendre que actualizar luego de crear el nodo
	{
		if(this.isFull()) return false;
		
		int i = this.n - 1;
		
		if (this.isLeaf())
		{
			while (i >= 0 && HelperFunctions.StringComp.LT(term, this.terms[i]))
			{
				this.terms[i+1] = this.terms[i];
				this.docCount[i+1] = this.docCount[i];
				this.docFreq[i+1] = this.docFreq[i];
				this.idf[i+1] = this.idf[i];
				this.postings[i+1] = this.postings[i];
				--i;
			}
			this.terms[i+1] = term;
			this.docCount[i+1] = this.docCount[i+1] + 1;
			this.docFreq[i+1] = docFreq;
			this.idf[i+1] = idf;
			this.postings[i+1] = new PostingList();
			this.n = this.n + 1;
			return true;
		}
		else
		{
			while (i >= 0 && HelperFunctions.StringComp.LT(term, this.terms[i]))
				--i;
			
			i = i+1;
			
			if (this.children[i].isFull())
			{
				this.Split(i);
				if (HelperFunctions.StringComp.GT(term, this.terms[i])) ++i;
			}
			
			return this.children[i].InsertOnNonFullNode(term, docFreq, idf);
		}
	}
	
	public void print()
	{
		int i = 0;
		for (i = 0; i < this.n; ++i)
		{
			if (this.children[i] != null)
				this.children[i].print();
			System.out.print(this.terms[i] + " ");
		}
		if (this.children[i] != null)
			this.children[i].print();
	}

	public void addPosting(int i){
		this.postings[i].add(i, i, i, i, null);
		return;
	}

	public String[] getTerms() {
		return this.terms;
	}
	public String getTerms(int i) {
		return this.terms[i];
	}
	public void setTerms(String[] terms) {
		this.terms = terms;
	}
	public void setTerms(String term, int i) {
		this.terms[i] = term;
	}

	public DictionaryNode[] getChildren() {
		return children;
	}
	public DictionaryNode getChildren(int i) {
		return children[i];
	}
	public void setChildren(DictionaryNode[] children) {
		this.children = children;
	}
	public void setChildren(DictionaryNode children, int i) {
		this.children[i] = children;
	}

	public int[] getDocCount() {
		return this.docCount;
	}
	public int getDocCount (int i) {
		return this.docCount[i];
	}
	public void setDocCount(int[] docCount) {
		this.docCount = docCount;
	}
	public void setTermCount(int docCount, int i) {
		this.docCount[i] = docCount;
	}

	public float[] getDocFreq() {
		return this.docFreq;
	}
	public float getDocFreq(int i) {
		return this.docFreq[i];
	}
	public void setDocFreq(float[] docFreq) {
		this.docFreq = docFreq;
	}
	public void setDocFreq (float docFreq, int i){
		this.docFreq[i] = docFreq;
	}

	public float[] getIdf() {
		return this.idf;
	}
	public float getIdf(int i) {
		return this.idf[i];
	}
	public void setIdf(float[] idf) {
		this.idf = idf;
	}
	public void setIdf(float idf, int i) {
		this.idf[i] = idf;
	}

	public PostingList[] getPostings() {
		return this.postings;
	}
	public PostingList getPostings(int i) {
		return this.postings[i];
	}
	public void setPostings(PostingList[] postings) {
		this.postings = postings;
	}
	public void setTermPos(PostingList postings, int i) {
		this.postings[i] = postings;
	}

}
