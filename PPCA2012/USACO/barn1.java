/*
	ID:bobchenna1
	LANG:JAVA
	TASK:barn1
*/
import java.io.*;
import java.util.*;

class barn1{
	public static void main(String[] args)throws IOException{
		BufferedReader f = new BufferedReader(new FileReader("barn1.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("barn1.out")));

		StringTokenizer st=new StringTokenizer(f.readLine());
		int m=Integer.parseInt(st.nextToken());
		int s=Integer.parseInt(st.nextToken());
		int c=Integer.parseInt(st.nextToken());
		int[] a=new int[c];
		for(int i=0;i<c;++i)
			a[i]=Integer.parseInt(f.readLine());
		Arrays.sort(a);
		int[][] dp;
		dp=new int[201][51];
		for(int i=1;i<201;++i)
			for(int j=0;j<51;++j)
				dp[i][j]=100000000;
		for(int i=1;i<=c;++i)
			for(int j=1;j<=m;++j){
				if(i>1)dp[i][j]=Math.min(dp[i-1][j-1]+1,dp[i-1][j]+a[i-1]-a[i-2]);
				else dp[i][j]=dp[i-1][j-1]+1;

				dp[i][j]=Math.min(dp[i][j],dp[i][j-1]);
			}
		out.println(dp[c][m]);
		out.close();

		System.exit(0);
	}
}