/*
	ID:bobchenna1
	LANG:JAVA
	TASK:frac1
*/
import java.io.*;
import java.util.*;

class frac1{
	public static ArrayList<Integer> x,y,z;
	public static int gcd(int x,int y){
		if(y==0)return x;
		else return gcd(y,x%y);
	}
	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("frac1.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("frac1.out")));

		int n=Integer.parseInt(in.readLine());
		x=new ArrayList<Integer>();
		y=new ArrayList<Integer>();
		z=new ArrayList<Integer>();
		out.println("0/1");
		for(int i=1;i<=n;++i)
			for(int j=1;j<=i;++j)
				if(gcd(i,j)==1){
					x.add(j);
					y.add(i);
					z.add(x.size()-1);
				}
		Comparator<Object> xcnx=new Comparator<Object>(){
			public int compare(Object o1,Object o2){
			Integer a=(Integer)o1;
			Integer b=(Integer)o2;
			if(x.get(a)*y.get(b)<x.get(b)*y.get(a))
				return -1;
			else return 1;
			}
		};
		Collections.sort(z,xcnx);
		for(int i=0;i<z.size();++i)
			out.println(x.get(z.get(i))+"/"+y.get(z.get(i)));
		out.close();
		System.exit(0);
	}
}