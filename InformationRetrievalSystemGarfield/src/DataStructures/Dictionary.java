package DataStructures;

public class Dictionary {
	
	private int minimumDegree = 0;
	private DictionaryNode root = null;
	
	public Dictionary(int minimumDegree)
	{
		this.minimumDegree = minimumDegree;
		this.root = new DictionaryNode(minimumDegree);
		this.root.setLeaf(true);
	}
	
	public Object[] Search(String k)
	{
		return this.root.Search(k);
	}
	
	public boolean Add(String k)
	{
        if (this.Search(k) != null) return false;

		if (this.root.isFull())
		{
			DictionaryNode s = new DictionaryNode(this.minimumDegree);
			s.setChild(this.root, 0);
			this.root = s;
		}
		return this.root.InsertOnNonFullNode(k);
	}
	
	public void Print()
	{
		if (this.root == null) return;
		this.root.print();
	}

}
