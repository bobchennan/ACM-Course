/*
	ID:bobchenna1
	LANG:JAVA
	TASK:milk3
*/
import java.io.*;
import java.util.*;

class milk3{
	public static boolean[][][] f=new boolean[21][21][21];
	public static int A,B,C;	
	public static void dfs(int x,int y,int z){
		f[x][y][z]=true;
		int tmp=Math.min(x,B-y);
		if(!f[x-tmp][y+tmp][z])dfs(x-tmp,y+tmp,z);

		tmp=Math.min(A-x,y);
		if(!f[x+tmp][y-tmp][z])dfs(x+tmp,y-tmp,z);

		tmp=Math.min(x,C-z);
		if(!f[x-tmp][y][z+tmp])dfs(x-tmp,y,z+tmp);

		tmp=Math.min(A-x,z);
		if(!f[x+tmp][y][z-tmp])dfs(x+tmp,y,z-tmp);

		tmp=Math.min(B-y,z);
		if(!f[x][y+tmp][z-tmp])dfs(x,y+tmp,z-tmp);

		tmp=Math.min(y,C-z);
		if(!f[x][y-tmp][z+tmp])dfs(x,y-tmp,z+tmp);
	}

	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("milk3.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("milk3.out")));

		StringTokenizer st = new StringTokenizer(in.readLine());
		A=Integer.parseInt(st.nextToken());
		B=Integer.parseInt(st.nextToken());
		C=Integer.parseInt(st.nextToken());
		dfs(0,0,C);
		boolean[] ans=new boolean[21];
		for(int j=0;j<=B;++j)
			for(int k=0;k<=C;++k)
				if(f[0][j][k])
					ans[k]=true;
		for(int i=0,j=0;i<=C;++i){
			if(!ans[i])continue;
			if(j!=0)out.print(" ");
			++j;
			out.print(i);
		}
		out.println();
		out.close();
		System.exit(0);
	}
}