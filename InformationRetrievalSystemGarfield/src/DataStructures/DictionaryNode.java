package DataStructures;

public class DictionaryNode {
	
	private int minimumDegree = 0;
	private String keys[];
	private DictionaryNode children[];
	private boolean leaf = false;
	private int n = 0;
	
	public DictionaryNode(int minimumDegree)
	{
		this.minimumDegree = minimumDegree;
		this.keys = new String[2*minimumDegree - 1];
		this.children = new DictionaryNode[2*minimumDegree];
		
		for (int i = 0; i < 2*minimumDegree - 1; ++i)
			this.keys[i] = "";
		for (int i = 0; i < 2*minimumDegree; ++i)
			this.children[i] = null;
		/*System.out.println("Info about this node:");
		System.out.println("my minimum degree is " + this.minimumDegree);
		System.out.println("I can store " + this.keys.length + " keys");
		System.out.println("I can store " + this.children.length + " children"); */
		
	
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
		for (int i = 0; i < 2*this.minimumDegree-1; ++i) System.out.print(this.keys[i] + " / ");
		System.out.println("\n---------------------");
		// Object[0] = BNode
		// Object[1] = id
		
		Object[] searchResult = new Object[2];
		
		int i = 0;
		
		while (i < this.n && HelperFunctions.StringComp.LT(this.keys[i], k)) // AQUI
			++i;
		
		if (i < this.n && this.keys[i].equals(k))
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
		
		for (j = 0; j < this.minimumDegree - 1; ++j)
			z.keys[j] = y.keys[j + this.minimumDegree];
		
		for (j = 0; j < this.minimumDegree; ++j)
			z.children[j] = y.children[j + this.minimumDegree];
		
		y.n = this.minimumDegree - 1;
		
		for (j = this.n; j > i; --j)
			this.children[j+1] = this.children[j];
		this.children[j+1] = z;
			
		for (j = this.n-1; j > i-1; --j)
			this.keys[j+1] = this.keys[j];
		this.keys[j+1] = y.keys[this.minimumDegree - 1];
		
		this.n = this.n + 1;
		
		for (j = this.minimumDegree; j < 2*this.minimumDegree-1; ++j)
		{
			y.keys[j] = "";
			y.children[j] = null;
		}
		y.keys[this.minimumDegree - 1] = "";
		y.children[j] = null;
	}
	
	public boolean InsertOnNonFullNode(String k)
	{
		if(this.isFull()) return false;
		
		int i = this.n - 1;
		
		if (this.isLeaf())
		{
			while (i >= 0 && HelperFunctions.StringComp.LT(k, this.keys[i]))
			{
				this.keys[i+1] = this.keys[i];
				--i;
			}
			this.keys[i+1] = k;
			this.n = this.n + 1;
			return true;
		}
		else
		{
			while (i >= 0 && HelperFunctions.StringComp.LT(k, this.keys[i]))
				--i;
			
			i = i+1;
			
			if (this.children[i].isFull())
			{
				this.Split(i);
				if (HelperFunctions.StringComp.GT(k, this.keys[i])) ++i;
			}
			
			return this.children[i].InsertOnNonFullNode(k);
		}
	}
	
	public void print()
	{
		int i = 0;
		for (i = 0; i < this.n; ++i)
		{
			if (this.children[i] != null)
				this.children[i].print();
			System.out.print(this.keys[i] + " ");
		}
		if (this.children[i] != null)
			this.children[i].print();
	}

}
