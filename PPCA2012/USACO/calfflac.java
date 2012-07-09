/*
	ID:bobchenna1
	LANG:JAVA
	TASK:calfflac
*/
import java.io.*;
import java.util.*;

class calfflac{
	public static void main(String[] args)throws IOException{
		BufferedReader f = new BufferedReader(new FileReader("calfflac.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("calfflac.out")));

		String s=new String(),t;
		while((t=f.readLine())!=null){
			s=s+t;
			s=s+'\n';
		}

		int[] from=new int[100001];
		char[] g=new char[100001];
		g[0]='$';
		g[1]='#';
		int cnt=1;
		for(int i=0;i<s.length();++i){
			if((s.charAt(i)>='a'&&s.charAt(i)<='z')||(s.charAt(i)>='A'&&s.charAt(i)<='Z')){
				from[++cnt]=i;g[cnt]=s.charAt(i);
				from[++cnt]=0;g[cnt]='#';
			}
		}
		//out.println(s);
		//out.println(g);

		int[] p=new int[40001];
		int mx=0,id=0;

		for(int i=1;i<=cnt;++i){
			if(mx>i)p[i]=Math.min(p[2*id-i],mx-i);
			else p[i]=1;
			while(i+p[i]<=cnt&&Character.toUpperCase(g[i+p[i]])==Character.toUpperCase(g[i-p[i]]))p[i]++;
			if(i+p[i]>mx){
				mx=i+p[i];
				id=i;
			}
		}
		mx=0;
		for(int i=1;i<=cnt;++i){
			if(p[i]>mx){
				mx=p[i];
				id=i;
			}
		}
		int l=id-p[id]+1;
		int r=id+p[id]-1;
		if(from[l]==0)++l;
		if(from[r]==0)--r;
		cnt=0;
		for(int i=l;i<=r;++i)
			if(g[i]!='#'&&g[i]!='$')
				++cnt;
		out.println(cnt);
		out.println(s.substring(from[l],from[r]+1));
		out.close();
		System.exit(0);
	}
}