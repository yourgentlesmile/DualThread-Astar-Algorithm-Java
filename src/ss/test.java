package ss;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class test implements callbacks{
	Thread thread;
	Thread thread1;
	Boolean _isalreadycalled=false;
	int g[][]=new int[][]{
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,1,1,1,0,0,0,0},
		{0,0,0,0,0,1,0,0,0,0},
		{0,0,0,0,0,1,0,0,0,0},
		{0,0,0,1,1,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0}
	};
	Map<Integer , _node> map=new ConcurrentHashMap<Integer, _node>();
	Datashare di=new Datashare(5);
	public void tst() throws InterruptedException, ExecutionException{

		g[5][0]=7;
		g[5][9]=8;
		Astar astar=new Astar(5,0,5,9,map,5,6,di,g,this);
		Astar bstar=new Astar(5,9,5,0,map,6,5,di,g,this);
		thread=new Thread(astar);
		thread1=new Thread(bstar);
		astar.ano_thd=thread1;
		bstar.ano_thd=thread;
		thread.start();
		thread1.start();	
		
	}

	@Override
	public void readdata(int mark) {
		synchronized (_isalreadycalled) {
			if(mark==5) 
				thread.interrupt();
			else 
				thread1.interrupt();
			if(_isalreadycalled){
				for (int i = 0; i < g.length; i++) {
					for (int j = 0; j < g.length; j++) {
						if(g[i][j]==1000) System.out.print("* ");
						else System.out.print(g[i][j]+" ");
					}
					System.out.println();
				}
			}
			_isalreadycalled=true;
		}
	}

}
