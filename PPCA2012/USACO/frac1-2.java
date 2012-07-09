 /*
ID: bobchenna1
LANG: JAVA
TASK: frac1
*/
import java.util.*;
import java.io.*;
class frac1 {
	public static PrintWriter out;
	public static int n;
	public static void dfs(int x1,int y1,int x2,int y2){
		int m1=x1+x2;
		int m2=y1+y2;
		if(m1>n||m2>n)return;
		dfs(x1,y1,m1,m2);
		out.println(m1+"/"+m2);
		dfs(m1,m2,x2,y2);
	}
	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("frac1.in"));
		out = new PrintWriter(new BufferedWriter(new FileWriter("frac1.out")));

		n=Integer.parseInt(in.readLine());
		out.println("0/1");
		dfs(0,1,1,1);
		out.println("1/1");
		out.close();
		System.exit(0);
	}
}