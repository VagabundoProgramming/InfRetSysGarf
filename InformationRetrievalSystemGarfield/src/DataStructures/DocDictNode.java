package DataStructures;

public class DocDictNode {

    private int minimumDegree = 0;
	private int docID[];
	private DocDictNode children[];
	private boolean leaf = false;
	private int n = 0; // Fuckign hate you

    //Statistics
    private int docLen[];
    private int uTokens[];
    private int nVignettes[];
	
	public DocDictNode(int minimumDegree)
	{
		this.minimumDegree = minimumDegree;
		this.docID = new int[2*minimumDegree - 1];
		this.children = new DocDictNode[2*minimumDegree];

        this.docLen = new int[2*minimumDegree - 1];
        this.uTokens = new int[2*minimumDegree - 1];
        this.nVignettes = new int[2*minimumDegree - 1];

		
		for (int i = 0; i < 2*minimumDegree - 1; ++i){
			this.docID[i] = 0;
            this.docLen[i] = -1; // Important since some comics can just have 0 words
            this.uTokens[i] = -1;
            this.nVignettes[i] = -1;
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
	
	public void setLeaf()
	{
		this.leaf = true;
	}
	
	public void setChild(DocDictNode c, int i)
	{
		this.children[i] = c;
	}
	
	public Object[] search(int k)
	{
		
		// Object[0] = BNode
		// Object[1] = id
		
		Object[] searchResult = new Object[2];
		
		int i = 0;
		
		while (i < this.n && this.docID[i] < k)
			++i;
		
		if (i < this.n && this.docID[i] == k)
		{
			searchResult[0] = this;
			searchResult[1] = i;
			return searchResult;
		}
		else if (this.leaf == true)
				return null;
		else
			return this.children[i].search(k);
	}
	
	private void split(int i)
	{
		int j = 0;
		
		if (this.isFull()) return;
		if (i > this.n) return;
		if (this.children[i] == null) return;
		if (!this.children[i].isFull()) return;
		
		DocDictNode z = new DocDictNode(this.minimumDegree);
		DocDictNode y = this.children[i];
		z.leaf = y.leaf;
		z.n = this.minimumDegree - 1;
		
		for (j = 0; j < this.minimumDegree - 1; ++j){
			z.docID[j] = y.docID[j + this.minimumDegree];
			z.docLen[j] = y.docLen[j + this.minimumDegree];
			z.uTokens[j] = y.uTokens[j + this.minimumDegree];
			z.nVignettes[j] = y.nVignettes[j + this.minimumDegree];
		}
		for (j = 0; j < this.minimumDegree; ++j)
			z.children[j] = y.children[j + this.minimumDegree];
		
		y.n = this.minimumDegree - 1;
		
		for (j = this.n; j > i; --j)
			this.children[j+1] = this.children[j];
		this.children[j+1] = z;
			
		for (j = this.n-1; j > i-1; --j){
			this.docID[j+1] = this.docID[j];
			this.docLen[j+1] = this.docLen[j];
			this.uTokens[j+1] = this.uTokens[j];
			this.nVignettes[j+1] = this.nVignettes[j];
		}

		this.docID[j+1] = y.docID[this.minimumDegree - 1];
		this.docLen[j+1] = y.docLen[this.minimumDegree - 1];
		this.uTokens[j+1] = y.uTokens[this.minimumDegree - 1];
		this.nVignettes[j+1] = y.nVignettes[this.minimumDegree - 1];
		
		this.docID[i] = y.docID[this.minimumDegree - 1];
		this.docLen[i] = y.docLen[this.minimumDegree - 1];
		this.uTokens[i] = y.uTokens[this.minimumDegree - 1];
		this.nVignettes[i] = y.nVignettes[this.minimumDegree - 1];


		this.n = this.n + 1;
		
		for (j = this.minimumDegree; j < 2*this.minimumDegree-1; ++j){
			y.docID[j] = 0;
			y.children[j] = null;
			y.docLen[j] = -1;
            y.uTokens[j] = -1;
            y.nVignettes[j] = -1;
		}
		y.docID[this.minimumDegree - 1] = 0;
		// Quiza hace falta algo aqui, pero no estoy seguro
		y.children[j] = null;
	}
	
	public boolean insertOnNonFullNode(int ID, int docLen, int uTokens, int nVignettes)
	{
		if(this.isFull()) return false;
		
		int i = this.n - 1;
		
		if (this.isLeaf())
		{
			while (i >= 0 && ID < this.docID[i])
			{
				this.docID[i+1] = this.docID[i];
				this.docLen[i+1] = this.docLen[i];
				this.uTokens[i+1] = this.uTokens[i];
				this.nVignettes[i+1] = this.nVignettes[i];
				--i;
			}
			this.docID[i+1] = ID;
			this.docLen[i+1] = docLen;
			this.uTokens[i+1] = uTokens;
			this. nVignettes[i+1] = nVignettes;
			this.n = this.n + 1;
			return true;
		}
		else
		{
			while (i >= 0 && ID < this.docID[i])
				--i;
			
			i = i+1;
			
			if (this.children[i].isFull())
			{
				this.split(i);
				if (ID > this.docID[i]) ++i;
			}
			
			return this.children[i].insertOnNonFullNode(ID, docLen, uTokens, nVignettes);
		}
	}

	
	
	public void print()
	{
		int i = 0;
		for (i = 0; i < this.n; ++i)
		{
			if (this.children[i] != null)
				this.children[i].print();
			System.out.println(this.docID[i] + " " + this.docLen[i]  + " " + this.uTokens[i] + " " + this.nVignettes[i]);
		}
		if (this.children[i] != null)
			this.children[i].print();
	}

	public int getUTokens(int i){
		return this.uTokens[i];
	}


	public DocDictNode[] getChildren(){
		return this.children;
	}
	
	public DocDictNode getChildren(int i){
		return this.children[i];
	}

	public int[] getDocId(){
		return this.docID;
	}

	public int getDocId(int i){
		return this.docID[i];
	}

	public int[] getDocLen(){
		return this.docLen;
	}

	public int getDocLen(int i){
		return this.docLen[i];
	}

	public int[] getUToknes(){
		return this.uTokens;
	}

	public int getUToknes(int i){
		return this.uTokens[i];
	}

	public int[] getNVignettes(){
		return this.nVignettes;
	}

	public int getNVignettes(int i){
		return this.nVignettes[i];
	}
}
