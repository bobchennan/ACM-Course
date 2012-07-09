/*
	ID:bobchenna1
	LANG:JAVA
	TASK:ariprog
*/
import java.io.*;
import java.util.*;

class ariprog{
	public static void main(String[] args)throws IOException{
		BufferedReader f = new BufferedReader(new FileReader("ariprog.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ariprog.out")));
		int n=Integer.parseInt(f.readLine());
		int m=Integer.parseInt(f.readLine());
		boolean[] app=new boolean[250*250*2+1];
		ArrayList<Integer> aa=new ArrayList<Integer>();
		for(int i=0;i<=m;++i)
			for(int j=i;j<=m;++j){
				app[i*i+j*j]=true;
				aa.add(i*i+j*j);
			}
		Collections.sort(aa);
		ArrayList<Integer> a=new ArrayList<Integer>();
		for(int i=0;i<aa.size();++i)
			if(i==0||(int)aa.get(i)!=(int)aa.get(i-1)){
				a.add(aa.get(i));
			}

		int ans=0,MAX=m*m*2;
		for(int b=1;b<=MAX/(n-1);++b)
			for(int i=0;i<a.size()&&a.get(i)+b*(n-1)<=MAX;++i){
				boolean ok=true;
				int x=a.get(i);
				for(int l=1;l<n;++l){
					if(app[x+b*l]==false){
						ok=false;
						break;
					}
				}
				if(ok){
					out.println(x+" "+b);
					++ans;
				}
			}
		if(ans==0)out.println("NONE");
		out.close();
	}
}