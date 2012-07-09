/*
	ID:bobchenna1
	LANG:JAVA
	TASK:numtri
*/
import java.io.*;
import java.util.*;

class numtri{
	public static void main(String[] args)throws IOException{
		int R;
		int[][] f=new int[2][1001];
		BufferedReader in = new BufferedReader(new FileReader("numtri.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("numtri.out")));

		R=Integer.parseInt(in.readLine());
		int now=1;
		for(int i=1;i<=R;++i){
			StringTokenizer st = new StringTokenizer(in.readLine());
			for(int j=1;j<=i;++j){
				int x=Integer.parseInt(st.nextToken());
				if(j==1)f[now][j]=f[(now^1)][j]+x;
				else if(j==i)f[now][j]=f[(now^1)][j-1]+x;
				else f[now][j]=Math.max(f[(now^1)][j-1],f[(now^1)][j])+x;
			}
			now^=1;
		}

		int ans=0;
		for(int i=1;i<=R;++i)
			ans=Math.max(ans,f[now^1][i]);
		out.println(ans);
		out.close();
		System.exit(0);
	}
}