/*
	ID:bobchenna1
	LANG:C++
	TASK:stall4 
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
bool b[201][201];
int n,m,vx[201],lk[201];
bool find(int x){
	for(int y=1;y<=m;++y){
		if(!b[x][y])continue;
		if(vx[y])continue;
		vx[y]=1;
		if(!lk[y]||find(lk[y])){
			lk[y]=x;
			return 1;
		}
	}
	return 0;
}
int main(){
	freopen("stall4.in","r",stdin);
	freopen("stall4.out","w",stdout);
	scanf("%d%d",&n,&m);
	for(int i=1;i<=n;++i){
		int x,y;
		for(scanf("%d",&x);x--;){
			scanf("%d",&y);
			b[i][y]=1;
		}
	}
	int ans=0;
	for(int i=1;i<=n;++i){
		memset(vx,0,sizeof(vx));
		if(find(i))++ans;
	}
	printf("%d\n",ans);
	return 0;
}