/*
	ID:bobchenna1
	LANG:JAVA
	TASK:milk
*/
import java.io.*;
import java.util.*;

class milk{
	public static void main(String [] args )throws IOException{
		BufferedReader f = new BufferedReader(new FileReader("milk.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("milk.out")));

		int[] price,num;
		price=new int[5001];
		num=new int[5001];
		StringTokenizer st=new StringTokenizer(f.readLine());
		int m=Integer.parseInt(st.nextToken());
		int n=Integer.parseInt(st.nextToken());
		for(int i=0;i<n;++i){
			st=new StringTokenizer(f.readLine());
			price[i]=Integer.parseInt(st.nextToken());
			num[i]=Integer.parseInt(st.nextToken());
		}

		int l=0,r=1000;
		while(l!=r){
			int mid=(l+r)>>1;
			int count=0;
			for(int i=0;i<n;++i)
				if(price[i]<=mid)
					count+=num[i];
			if(count>=m)r=mid;
			else l=mid+1;
		}
		int ans=0,cnt=0;
		for(int i=0;i<n;++i)
			if(price[i]<=l){
				ans+=price[i]*num[i];
				cnt+=num[i];
			}
		out.println(ans-l*(cnt-m));

		out.close();
		System.exit(0);
	}
}