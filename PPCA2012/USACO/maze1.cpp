/*
	ID:bobchenna1
	LANG:C++
	TASK:maze1
*/
#include <queue>
#include <cstdio>
#include <string>
#include <algorithm>
using namespace std;
int n,m,t[301][301];
char s[301][301];
queue< pair<int,int> > Q;
int dy[]={0,1,0,-1};
int dx[]={-1,0,1,0};
bool check(int x,int y){
	if(x<0||x>=n)return 0;
	if(y<0||y>=m)return 0;
	if(s[x][y]=='+'||s[x][y]=='|'||s[x][y]=='-')return 0;
	return 1;
}
int main(){
	FILE *in=fopen("maze1.in","r");
	FILE *out=fopen("maze1.out","w");
	fscanf(in,"%d%d%*c",&m,&n);
	n=2*n+1;
	m=2*m+1;
	for(int i=0;i<n;++i)
		fgets(s[i],1000,in);
	for(int i=0;i<n;++i){
		if(s[i][0]==' '){Q.push(make_pair(i,0));s[i][0]='+';}
		if(s[i][m-1]==' '){Q.push(make_pair(i,m-1));s[i][m-1]='+';}
	}
	for(int j=0;j<m;++j){
		if(s[0][j]==' '){Q.push(make_pair(0,j));s[0][j]='+';}
		if(s[n-1][j]==' '){Q.push(make_pair(n-1,j));s[n-1][j]='+';}
	}
	int ans=0;
	while(!Q.empty()){
		int x=Q.front().first;
		int y=Q.front().second;
		Q.pop();
		for(int i=0;i<4;++i)
			if(check(x+dx[i],y+dy[i])){
				if(((x+dx[i])&1)&&((y+dy[i])&1))t[x+dx[i]][y+dy[i]]=t[x][y]+1;
				else t[x+dx[i]][y+dy[i]]=t[x][y];
				s[x+dx[i]][y+dy[i]]='+';
				ans=max(ans,t[x+dx[i]][y+dy[i]]);
				Q.push(make_pair(x+dx[i],y+dy[i]));
			}
	}
	fprintf(out,"%d\n",ans);
	return 0;
}