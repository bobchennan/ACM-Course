/*
	ID:bobchenna1
	LANG:JAVA
	TASK:pprime
*/
import java.io.*;
import java.util.*;

class pprime{
	public static int[] ten=new int[6];
	public static boolean check(int v){
		for(int i=2;i<=(int)Math.sqrt(v);++i)
			if(v%i==0)
				return false;
		return true;
	}

	public static int leng(int v){
		if(v==0)return 0;
		int ret=0;
		for(;v!=0;){
			++ret;
			v/=10;
		}
		return ret;
	}

	public static int reverse(int v){
		int ret=0;
		for(;v!=0;){
			ret=ret*10+v%10;
			v/=10;
		}
		return ret;
	}

	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("pprime.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("pprime.out")));

		ten[0]=1;
		ten[1]=10;
		ten[2]=100;
		ten[3]=1000;
		ten[4]=10000;
		ten[5]=100000;
		int a,b;
		StringTokenizer st = new StringTokenizer(in.readLine());
		a=Integer.parseInt(st.nextToken());
		b=Integer.parseInt(st.nextToken());
		ArrayList<Integer> ans=new ArrayList<Integer>();

		for(int i=1;i<10000;++i){
			int len=leng(i);
			int tmp=i*ten[len]+reverse(i);
			if(tmp>=a&&tmp<=b&&check(tmp))
				ans.add(tmp);
			tmp=i*ten[len-1]+reverse(i/10);
			if(tmp>=a&&tmp<=b&&check(tmp))
				ans.add(tmp);
			if(tmp>b)break;
		}
		Collections.sort(ans);
		for(int i=0;i<ans.size();++i)
			out.println(ans.get(i));
		out.close();
		System.exit(0);
	}
}