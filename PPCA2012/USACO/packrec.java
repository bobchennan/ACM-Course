/*
	ID:bobchenna1
	LANG:JAVA
	TASK:packrec
*/
import java.io.*;
import java.util.*;

class packrec{
	public static void calc1(int[] xx,int[] yy,ArrayList<Integer> list){
		int y=0;
		for(int i=0;i<4;++i)
			y=Math.max(y,yy[i]);
		int x=0;
		for(int i=0;i<4;++i)
			x+=xx[i];
		if(x*y<list.get(0)){
			list.clear();
			list.add(x*y);
			list.add(Math.min(x,y));
		}
		else if(x*y==list.get(0)){
			if(list.indexOf(Math.min(x,y))==-1)list.add(Math.min(x,y));
		}
	}

	public static void calc2(int[] xx,int[] yy,ArrayList<Integer> list){
		int y=0;
		for(int i=0;i<3;++i)
			y=Math.max(y,yy[i]);
		y+=xx[3];
		int x=0;
		for(int i=0;i<3;++i)
			x+=xx[i];
		x=Math.max(x,yy[3]);
		if(x*y<list.get(0)){
			list.clear();
			list.add(x*y);
			list.add(Math.min(x,y));
		}
		else if(x*y==list.get(0)){
			if(list.indexOf(Math.min(x,y))==-1)list.add(Math.min(x,y));
		}
	}

	public static void calc3(int[] xx,int[] yy,ArrayList<Integer> list){
		int y=Math.max(xx[0]+xx[1],yy[3])+xx[2];
		int x=Math.max(Math.max(yy[0],yy[1])+xx[3],yy[2]);
		if(x*y<list.get(0)){
			list.clear();
			list.add(x*y);
			list.add(Math.min(x,y));
		}
		else if(x*y==list.get(0)){
			if(list.indexOf(Math.min(x,y))==-1)list.add(Math.min(x,y));
		}
	}

	public static void calc4(int[] xx,int[] yy,ArrayList<Integer> list){
		int y=xx[0]+Math.max(xx[1],xx[3])+xx[2];
		int x=Math.max(yy[0],Math.max(yy[1]+yy[3],yy[2]));
		if(x*y<list.get(0)){
			list.clear();
			list.add(x*y);
			list.add(Math.min(x,y));
		}
		else if(x*y==list.get(0)){
			if(list.indexOf(Math.min(x,y))==-1)list.add(Math.min(x,y));
		}
	}

	public static void calc5(int[] xx,int[] yy,ArrayList<Integer> list){
		int y=Math.max(xx[0],xx[3])+xx[1]+xx[2];
		int x=Math.max(Math.max(yy[0]+yy[3],yy[1]),yy[2]);
		if(x*y<list.get(0)){
			list.clear();
			list.add(x*y);
			list.add(Math.min(x,y));
		}
		else if(x*y==list.get(0)){
			if(list.indexOf(Math.min(x,y))==-1)list.add(Math.min(x,y));
		}
	}

	public static void calc6(int[] xx,int[] yy,ArrayList<Integer> list){
		if(yy[2]>yy[3]||xx[0]>xx[2])return;
		int y=Math.max(xx[0]+yy[1],xx[2]+xx[3]);
		int x=Math.max(yy[0]+yy[2],xx[1]+yy[3]);
		if(x*y<list.get(0)){
			list.clear();
			list.add(x*y);
			list.add(Math.min(x,y));
		}
		else if(x*y==list.get(0)){
			if(list.indexOf(Math.min(x,y))==-1)list.add(Math.min(x,y));
		}
	}

	public static void main(String[] args)throws IOException{
		BufferedReader f = new BufferedReader(new FileReader("packrec.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("packrec.out")));

		int[] x=new int[4],y=new int[4];
		for(int i=0;i<4;++i){
			StringTokenizer st=new StringTokenizer(f.readLine());
			x[i]=Integer.parseInt(st.nextToken());
			y[i]=Integer.parseInt(st.nextToken());
			if(x[i]>y[i]){
				int tmp=x[i];
				x[i]=y[i];
				y[i]=tmp;
			}
		}

		ArrayList<Integer> list=new ArrayList<Integer>();
		list.add(1000000000);

		for(int i=0;i<4;++i)
			for(int j=0;j<4;++j)
				if(j!=i)
					for(int k=0;k<4;++k)
						if(k!=i&&k!=j)
							for(int l=0;l<4;++l)
								if(l!=i&&l!=j&&l!=k)
									for(int a=0;a<2;++a)
										for(int b=0;b<2;++b)
											for(int c=0;c<2;++c)
												for(int d=0;d<2;++d){
													int[] xx=new int[]{x[i],x[j],x[k],x[l]};
													int[] yy=new int[]{y[i],y[j],y[k],y[l]};
													if(a==1){xx[0]=y[i];yy[0]=x[i];}
													if(b==1){xx[1]=y[j];yy[1]=x[j];}
													if(c==1){xx[2]=y[k];yy[2]=x[k];}
													if(d==1){xx[3]=y[l];yy[3]=x[l];}
													calc1(xx,yy,list);

													calc2(xx,yy,list);

													calc3(xx,yy,list);

													calc4(xx,yy,list);

													calc5(xx,yy,list);

													calc6(xx,yy,list);

												}
		int area=list.get(0);
		out.println(area);
		list.remove(0);
		Collections.sort(list);
		Iterator it=list.iterator();
		while(it.hasNext()){
			int v=((Integer)it.next()).intValue();
			out.println(v+" "+area/v);
		}
		out.close();
		System.exit(0);
	}
}