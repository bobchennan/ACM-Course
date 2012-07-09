/*
	ID:bobchenna1
	LANG:JAVA
	TASK:checker
*/
import java.io.*;
import java.util.*;

class checker{
	public static int n;
	public static int[] h=new int[13];
	public static PrintWriter out;
	public static int rest=3;
	public static int ans=0;
	public static void search(int now,int shu,int left,int right){
		if(now==n){
			++ans;
			if(rest!=0){
				for(int i=0;i<n-1;++i)
					out.print(h[i]+1+" ");
				out.println(h[n-1]+1);
				--rest;
			}
			return;
		}
		for(int i=0;i<n;++i){
			if((shu&(1<<i))!=0||(left&(1<<i))!=0||(right&(1<<i))!=0)continue;
			h[now]=i;
			search(now+1,shu|(1<<i),(left|(1<<i))<<1,(right|(1<<i))>>1);
		}
	}

	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("checker.in"));
		out = new PrintWriter(new BufferedWriter(new FileWriter("checker.out")));
		n=Integer.parseInt(in.readLine());

		search(0,0,0,0);
		out.println(ans);
		out.close();
		System.exit(0);
	}
}