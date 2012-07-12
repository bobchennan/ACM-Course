/*
	ID:bobchenna1
	LANG:C++
	TASK:camelot
*/
#include <set>
#include <map>
#include <cmath>
#include <queue>
#include <climits>
#include <cstdio>
#include <vector>
#include <string>
#include <cstring>
#include <algorithm>
using namespace std;
int dx[]={1,1,-1,-1,2,2,-2,-2};
int dy[]={2,-2,2,-2,1,-1,1,-1};
int n,m;
int kx,ky;
long long d[31][27],d1[31][27],tmp[31][27],tmp2[31][27];
queue< pair<int,int> > Q;
bool in[31][27],can[31][27];
bool inBoard(int tx,int ty){
	if(tx<1||tx>n)return 0;
	if(ty<1||ty>m)return 0;
	return 1;
}
void calc(int x,int y){
	memset(tmp,44,sizeof(tmp));
	memset(in,0,sizeof(in));
	Q.push(make_pair(x,y));
	in[x][y]=1;tmp[x][y]=0;
	while(!Q.empty()){
		int xx=Q.front().first;
		int yy=Q.front().second;
		Q.pop();
		for(int i=0;i<8;++i){
			int tx=dx[i]+xx;
			int ty=dy[i]+yy;
			if(inBoard(tx,ty)&&!in[tx][ty]){
				tmp[tx][ty]=tmp[xx][yy]+1;
				Q.push(make_pair(tx,ty));
				in[tx][ty]=1;
			}
		}
	}
	memset(tmp2,44,sizeof(tmp2));
	for(int i=1;i<=n;++i)
		for(int j=1;j<=m;++j){
			if(!in[i][j]){
				can[i][j]=0;
				continue;
			}
			d[i][j]+=tmp[i][j];
			int ddx=abs(i-kx);
			int ddy=abs(j-ky);
			int h=max(ddx,ddy);
			if((h&1)==(tmp[i][j]&1)){
				tmp2[i][j]=max(1LL*h,tmp[i][j]);
				Q.push(make_pair(i,j));
				in[i][j]=1;
			}
			else{
				tmp2[i][j]=max(1LL*h+1,tmp[i][j]);
				Q.push(make_pair(i,j));
				in[i][j]=1;
			}
		}
	while(!Q.empty()){
		int xx=Q.front().first;
		int yy=Q.front().second;
		Q.pop();in[xx][yy]=0;
		for(int i=0;i<8;++i){
			int tx=dx[i]+xx;
			int ty=dy[i]+yy;
			if(!inBoard(tx,ty)||!can[tx][ty])continue;
			if(tmp2[xx][yy]+1<tmp2[tx][ty]){
				tmp[tx][ty]=tmp[xx][yy]+1;
				if(!in[tx][ty]){
					in[tx][ty]=1;
					Q.push(make_pair(tx,ty));
				}
			}
		}
	}
	for(int i=1;i<=n;++i)
		for(int j=1;j<=m;++j)
			if(can[i][j])
				d1[i][j]=min(d1[i][j],tmp2[i][j]-tmp[i][j]);
}
int main(){
	freopen("camelot.in","r",stdin);
	freopen("camelot.out","w",stdout);
	scanf("%d%d%*c",&n,&m);
	char ch;
	scanf("%c%d%*c",&ch,&kx);
	ky=ch-'A'+1;
	char ch2;
	int x1,y1;
	for(int i=1;i<=n;++i)
		for(int j=1;j<=m;++j){
			int ddx=abs(kx-i);
			int ddy=abs(ky-j);
			d1[i][j]=max(ddx,ddy);
		}
	memset(can,1,sizeof(can));
	while(scanf("%c%d%*c",&ch,&x1)!=EOF){
		y1=ch-'A'+1;	
		calc(x1,y1);
	}
	long long ans=(~0U>>1);
	for(int i=1;i<=n;++i)
		for(int j=1;j<=m;++j)
			if(can[i][j])
			ans=min(ans,d[i][j]+d1[i][j]);
	printf("%lld\n",ans);
	return 0;
}