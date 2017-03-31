package ss;



public class _node{
	public int x;
	public int y;
	public _node parent=null;
	public int value;
	public boolean in_openlist=false;
	public boolean in_closedlist=false;
	public int distance=0;
	public int whichthread=0;
	public _node(int x, int y, _node parent, int value,int thread) {
		super();
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.value = value;
		whichthread=thread;
	}
	
}