/*
	ID:bobchenna1
	LANG:C++
	TASK:ttwo
*/
#include <cstdio>
#include <string>
#include <algorithm>
using namespace std;

char s[11][11];
int dy[]={0,1,0,-1};
int dx[]={-1,0,1,0};

bool check(int x,int y){
	if(x<0||x>=10)return 0;
	if(y<0||y>=10)return 0;
	if(s[x][y]=='*')return 0;
	return 1;
}

int main(){
	FILE *in=fopen("ttwo.in","r");
	FILE *out=fopen("ttwo.out","w");
	for(int i=0;i<10;++i)
		fscanf(in,"%s",s[i]);
	int p=0,x1,y1;
	int q=0,x2,y2;
	for(int i=0;i<10;++i)
		for(int j=0;j<10;++j)
			if(s[i][j]=='F')
				x1=i,y1=j,s[i][j]='.';
			else if(s[i][j]=='C')x2=i,y2=j,s[i][j]='.';
	int time;
	for(time=0;time<=1000000;++time){
		if(x1==x2&&y1==y2)break;
		if(check(x1+dx[p],y1+dy[p]))
			x1+=dx[p],y1+=dy[p];
		else p=(p+1)&3;
		if(check(x2+dx[q],y2+dy[q]))
			x2+=dx[q],y2+=dy[q];
		else q=(q+1)&3;
	}
	if(time<=1000000)fprintf(out,"%d\n",time);
	else fprintf(out,"%d\n",0);
	return 0;
}