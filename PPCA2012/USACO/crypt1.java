/*
	ID:bobchenna1
	LANG:JAVA
	TASK:crypt1
*/
import java.io.*;
import java.util.*;

class crypt1{
	public static boolean check(boolean[] b,int v){
		while(v!=0){
			int x=v%10;
			if(!b[x])return false;
			v/=10;
		}
		return true;
	}

	public static void main(String[] args)throws IOException{
		BufferedReader f = new BufferedReader(new FileReader("crypt1.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("crypt1.out")));

		boolean[] b=new boolean[11];
		for(int i=0;i<10;++i)b[i]=false; 
		int n=Integer.parseInt(f.readLine());
		StringTokenizer st=new StringTokenizer(f.readLine());
		for(int i=1;i<=n;++i){
			int x=Integer.parseInt(st.nextToken());
			b[x]=true;
		}

		int ans=0;
		for(int i=100;i<=999;++i)
			if(check(b,i))
				for(int j=10;j<=99;++j)
					if(check(b,j)){
						if(i*(j%10)>999)continue;
						if(i*(j/10)>999)continue;
						if(check(b,i*(j%10))==false)continue;
						if(check(b,i*(j/10))==false)continue;
						if(check(b,i*j)==false)continue;
						if(i*j>=1000&&i*j<=9999){
							//out.println(i);
							//out.println(j);
							++ans;
						}
					}
		out.println(ans);
		out.close();
		System.exit(0);
	}
}