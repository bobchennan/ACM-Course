/*
	ID:bobchenna1
	LANG:JAVA
	TASK:castle
*/
import java.io.*;
import java.util.*;

class castle{
	public static int[] dx=new int[]{1,-1,0,0};
	public static int[] dy=new int[]{0,0,1,-1};
	public static int[] count=new int[2501];
	public static int n,m,cnt;
	public static int[][] a,num;

	public static void DFS(int x,int y){
		if(num[x][y]!=0)return;
		num[x][y]=cnt;++count[cnt];
		int v=a[x][y];
		if((v&1)==0)
			DFS(x,y-1);
		if((v&2)==0)
			DFS(x-1,y);
		if((v&4)==0)
			DFS(x,y+1);
		if((v&8)==0)
			DFS(x+1,y);
	}

	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("castle.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("castle.out")));

		StringTokenizer st = new StringTokenizer(in.readLine());
		m=Integer.parseInt(st.nextToken());
		n=Integer.parseInt(st.nextToken());
		a=new int[51][51];
		num=new int[51][51];
		cnt=0;
		for(int i=0;i<n;++i){
			st=new StringTokenizer(in.readLine());
			for(int j=0;j<m;++j)
				a[i][j]=Integer.parseInt(st.nextToken());
		}
		for(int i=0;i<n;++i)
			for(int j=0;j<m;++j)
				if(num[i][j]==0){
					++cnt;
					DFS(i,j);
				}
		out.println(cnt);
		int ans=0;
		for(int i=1;i<=cnt;++i)
			ans=Math.max(ans,count[i]);
		out.println(ans);
		ans=0;
		int ax=0,ay=0;
		char ch='N';
		for(int i=0;i<n;++i)
			for(int j=0;j<m;++j){
				//north
				if(i!=0&&num[i-1][j]!=num[i][j]){
					if(count[num[i-1][j]]+count[num[i][j]]>ans||(count[num[i-1][j]]+count[num[i][j]]==ans&&(j<ay||(j==ay&&i>ax)))){
						ans=count[num[i-1][j]]+count[num[i][j]];
						ax=i;ay=j;
						ch='N';
					}
				}
				//south
				if(j!=m-1&&num[i][j]!=num[i][j+1]){
					if(count[num[i][j]]+count[num[i][j+1]]>ans||(count[num[i][j]]+count[num[i][j+1]]==ans&&(j<ay||(j==ay&&i>ax)))){
						ans=count[num[i][j]]+count[num[i][j+1]];
						ax=i;ay=j;
						ch='E';
					}
				}
				//out.println(ax+" "+ay+" "+ans);
			}
		out.println(ans);
		out.println((ax+1)+" "+(ay+1)+" "+ch);
		out.close();
		System.exit(0);
	}
}