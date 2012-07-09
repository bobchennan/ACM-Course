/*
	ID:bobchenna1
	LANG:JAVA
	TASK:sprime
*/
import java.io.*;
import java.util.*;

class sprime{
	public static int len;
	public static PrintWriter out; 
	public static boolean check(int v){
		for(int i=2;i<=(int)Math.sqrt(v);++i)
			if(v%i==0)
				return false;
		return true;
	}

	public static void tr(int now,int v){
		if(now==len){
			out.println(v);
			return;
		}
		if(check(v*10+1))tr(now+1,v*10+1);
		if(check(v*10+3))tr(now+1,v*10+3);
		if(check(v*10+7))tr(now+1,v*10+7);
		if(check(v*10+9))tr(now+1,v*10+9);
	}

	public static void main(String[] args)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("sprime.in"));
		out = new PrintWriter(new BufferedWriter(new FileWriter("sprime.out")));
		len=Integer.parseInt(in.readLine());
		tr(1,2);tr(1,3);tr(1,5);tr(1,7);
		out.close();
		System.exit(0);
	}
}