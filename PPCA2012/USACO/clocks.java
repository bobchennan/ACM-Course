/*
	ID:bobchenna1
	LANG:JAVA
	TASK:clocks
*/
import java.io.*;
import java.util.*;

public class clocks {
private static int length;
private static int[] clock;
private static int[][] moves;
private static int[] moveSequence = new int[9];
private static int[] res;

public static void main(String[] args) throws Exception {
BufferedReader in = new BufferedReader(new FileReader("clocks.in"));
PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
"clocks.out")), true);

StringTokenizer st = new StringTokenizer(in.readLine());
clock = new int[9];
for (int i = 0; i < clock.length; i++) {
if (!st.hasMoreTokens())
st = new StringTokenizer(in.readLine());
clock[i] = Integer.parseInt(st.nextToken());
}

moves = new int[][] { { 'A', 'B', 'D', 'E' }, { 'A', 'B', 'C' },
{ 'B', 'C', 'E', 'F' }, { 'A', 'D', 'G' },
{ 'B', 'D', 'E', 'F', 'H' }, { 'C', 'F', 'I' },
{ 'D', 'E', 'G', 'H' }, { 'G', 'H', 'I' },
{ 'E', 'F', 'H', 'I' } };
for (int i = 0; i < moves.length; i++)
for (int j = 0; j < moves[i].length; j++)
moves[i][j] -= 'A';

length=28;
dfs(0, 0);


// output
StringBuilder sb = new StringBuilder();
for (int i = 0; i < res.length; i++) {
int times = res[i];
for (int j = 0; j < times; j++) {
sb.append(i + 1);
sb.append(" ");
}
}
sb.deleteCharAt(sb.length() - 1);
out.println(sb);
System.exit(0);
}

private static void dfs(int t, int cnt) {
if(cnt>=length)return;
if (t == moveSequence.length) {
	if (successful(clock)){
	length=cnt;
	res=moveSequence.clone();
	}
	} else
		for (int i = 3; i >=0; i--) {
			for(int j=0;j<i;++j)moveClock(clock,moves[t]);
			moveSequence[t] = i;
			dfs(t + 1, cnt+i);
			if(i!=0)for(int j=0;j<4-i;++j)moveClock(clock,moves[t]);
		}
}

private static int clockwise(int n) {
n += 3;
if (n == 15)
return 3;
else
return n;
}

private static void moveClock(int[] clock, int[] move) {
for (int i : move)
clock[i] = clockwise(clock[i]);
}

private static boolean successful(int[] clock) {
for (int i : clock) {
if (i != 12)
return false;
}
return true;
}
}