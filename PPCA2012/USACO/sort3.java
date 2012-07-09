/*
	ID:bobchenna1
	LANG:JAVA
	TASK:sort3
*/
import java.io.*;
import java.util.*;

class sort3{
	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("sort3.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("sort3.out")));

		int n=Integer.parseInt(in.readLine());
		int[] a=new int[1001];
		int[] b=new int[1001];
		for(int i=0;i<n;++i){
			a[i]=Integer.parseInt(in.readLine());
			b[i]=a[i];
		}
		Arrays.sort(b,0,n);

		int[][] c=new int[4][4];
		for(int i=0;i<n;++i)
			c[b[i]][a[i]]++;

		out.println(c[2][1]+c[3][1]+(c[2][1]>c[1][2]?c[2][3]+c[2][1]-c[1][2]:c[3][2]+c[1][2]-c[2][1]));
		//  c21+c31+(c21>c12?c23+c21-c12:c32+c12-c21)
		out.close();
		System.exit(0);
	}
}