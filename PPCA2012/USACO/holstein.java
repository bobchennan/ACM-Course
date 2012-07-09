/*
	ID:bobchenna1
	LANG:JAVA
	TASK:holstein
*/
import java.util.*;
import java.io.*;
class holstein{
	public static int count(int v){
		int ret=0;
		for(;v!=0;){
			if((v&1)==1)++ret;
			v>>=1;
		}
		return ret;
	}
	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("holstein.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("holstein.out")));
		int n=Integer.parseInt(in.readLine());
		StringTokenizer st = new StringTokenizer(in.readLine());
		int[] a=new int[31];
		for(int i=0;i<n;++i)
			a[i]=Integer.parseInt(st.nextToken());
		int m=Integer.parseInt(in.readLine());
		int[][] b=new int[31][31];
		for(int i=0;i<m;++i){
			st = new StringTokenizer(in.readLine());
			for(int j=0;j<n;++j)
				b[i][j]=Integer.parseInt(st.nextToken());
		}
		int best=0,ans=100;
		for(int i=0;i<(1<<m);++i){
			int[] c=new int[31];
			for(int j=0;j<m;++j)
				if((i&(1<<j))!=0)
					for(int k=0;k<n;++k)
						c[k]+=b[j][k];
			boolean ok=true;
			for(int k=0;k<n;++k)
				if(c[k]<a[k]){
					ok=false;
					break;
				}
			if(ok==false)continue;
			int tmp=count(i);
			if(tmp<ans){
				ans=tmp;best=i;
			}
		}
		out.print(ans);
		for(int i=0;i<m;++i)
			if((best&(1<<i))!=0)
				out.print(" "+(i+1));
		out.println();
		out.close();
		System.exit(0);
	}
}
