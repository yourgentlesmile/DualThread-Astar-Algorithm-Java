package ss;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;


public class Astar implements Runnable{
	int start_x=5;
	int start_y=0;
	int end_x=5;
	int end_y=9;
	int threadmark;
	int anotherthread;
	public Thread ano_thd;
	Datashare di;
	callbacks cbs;
	Map<Integer, _node> map;
	PriorityQueue<_node> openlist=new PriorityQueue<>(new Comparator<_node>() {
		@Override
		public int compare(_node o1, _node o2) {
			return o1.value>o2.value?1:-1;
		}
	});
	
	public Astar(int start_x, int start_y, int end_x, int end_y, Map<Integer, _node> anothermap,int threads,int anotherthread,Datashare di,int g[][],callbacks cbs) {
		super();
		this.start_x = start_x;
		this.start_y = start_y;
		this.end_x = end_x;
		this.end_y = end_y;
		this.map = anothermap;
		threadmark=threads;
		this.anotherthread=anotherthread;
		this.di=di;
		this.g=g;
		this.cbs=cbs;
	}
	int g[][];
	public int fun_g(int source_x,int source_y){
		return Math.abs(source_x-start_x)+Math.abs(source_y-start_y);
	}
	public int fun_h(int source_x,int source_y){
		return Math.abs(source_x-end_x)+Math.abs(source_y-end_y);
	}
	public int findpath(){
		_node nd=new _node(start_x, start_y, null, 999999,threadmark);
		nd.in_openlist=true;
		map.put((start_x<<8)+start_y, nd);
		openlist.add(nd);
		while (openlist.size()!=0) {
			int status=getneighbors();
			if(status!= -1){
				_node temp1=map.get(di.s);
				while (temp1.parent!=null) {
					g[temp1.x][temp1.y]=1000;
//					System.out.println(temp1.x+" "+temp1.y);
					temp1=temp1.parent;
					
				}
				cbs.readdata(threadmark);
				return status;
			}
		}
		return (0<<8)+0;

	}
	public int getneighbors(){
		try {
			_node current=openlist.peek();
//			System.out.println("picked point"+current.x+" "+current.y+" "+current.value);
			int cx=current.x;
			int cy=current.y;
			if(di.m==100) di.m=threadmark;
			synchronized (di) {
				
				if(cx-1>=0&&(cy-1)>=0) if(insertopenedlist(cx-1,cy-1,current,14)==-1){
					di.s=(cx<<8)+cy;
					di.t=((cx-1)<<8)+(cy-1);
					return ((cx)<<8)+cy;
				};
				if((cx-1)>=0) if(insertopenedlist(cx-1,cy,current,10)== -1){
					di.s=(cx<<8)+cy;
					di.t=((cx-1)<<8)+(cy);
					return ((cx)<<8)+cy;
				};
				if(cx-1>=0&&(cy+1)<10) if(insertopenedlist(cx-1,cy+1,current,14)== -1){
					di.s=(cx<<8)+cy;
					di.t=((cx-1)<<8)+(cy+1);
					return ((cx)<<8)+cy;
				};
				if((cy-1)>=0) if(insertopenedlist(cx,cy-1,current,10)== -1){
					di.s=(cx<<8)+cy;
					di.t=((cx)<<8)+(cy-1);
					return ((cx)<<8)+cy;
				};
				if((cy+1)<10) if(insertopenedlist(cx,cy+1,current,10)== -1){
					di.s=(cx<<8)+cy;
					di.t=((cx)<<8)+(cy+1);
					return ((cx)<<8)+cy;
				};
				if(cx+1<10&&(cy-1)>=0) if(insertopenedlist(cx+1,cy-1,current,14)== -1){
					di.s=(cx<<8)+cy;
					di.t=((cx+1)<<8)+(cy-1);
					return ((cx)<<8)+cy;
				};
				if(cx+1<10) if(insertopenedlist(cx+1,cy,current,10)== -1){
					di.s=(cx<<8)+cy;
					di.t=((cx+1)<<8)+(cy);
					return ((cx)<<8)+cy;
				};
				if(cx+1<10&&(cy+1)<10) if(insertopenedlist(cx+1,cy+1,current,14)== -1){
					di.s=(cx<<8)+cy;
					di.t=((cx+1)<<8)+(cy+1);
					return ((cx)<<8)+cy;
				};
				if(di.m==100) di.m=threadmark;
				else di.m=anotherthread;
				di.notifyAll();
			}
			openlist.remove(current);
			current.in_openlist=false;
			current.in_closedlist=true;
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}
	public int insertopenedlist(int cx,int cy,_node parentnode,int v) throws InterruptedException{
		if(di.m==100) di.m=threadmark;
		if(g[cx][cy]==1) return -2;
		
		if(!map.containsKey((cx<<8)+cy)){
			_node node=new _node(cx, cy, parentnode, fun_g(cx, cy)+fun_h(cx, cy),threadmark);
//				System.out.println("add point:"+cx+" "+cy+" "+parentnode.x+" "+parentnode.y+" "+fun_g(cx, cy)+" "+fun_h(cx, cy)+" "+threadmark);
			node.in_openlist=true;
			node.distance=parentnode.distance+v;
			map.put((cx<<8)+cy, node);
			openlist.add(node);
		}else{
			_node nd=map.get((cx<<8)+cy);

				if(nd.whichthread!=threadmark)
				{
					di.m=100;
					return -1;
				}
			
				if(nd.in_openlist==true){
					if(nd.distance>parentnode.distance+v){
						nd.distance=parentnode.distance+v;
						nd.parent=parentnode;
					}
				}
		}
		return -2;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		findpath();
	}


	
}
