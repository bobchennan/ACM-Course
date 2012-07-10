/*
	ID:bobchenna1
	LANG:C++
	TASK:agrinet
*/
#include <map>
#include <cmath>
#include <queue>
#include <cstdio>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

int n,g[101][101];
struct edge{
	int x,y,v;
	edge(){}
	bool operator <(const edge &b)const{
		return v<b.v;
	}
};
edge e[10003];
int cnt=0,fa[101];

int find(int x){
	return fa[x]==x?x:fa[x]=find(fa[x]); 
}

int main(){
	freopen("agrinet.in","r",stdin);
	freopen("agrinet.out","w",stdout);

	scanf("%d",&n);
	for(int i=0;i<n;++i)
		for(int j=0;j<n;++j){
			int x;
			scanf("%d",&x);
			if(x){
				++cnt;
				e[cnt].x=i;
				e[cnt].y=j;
				e[cnt].v=x;
			}
		}
	sort(e+1,e+cnt+1);
	for(int i=0;i<n;++i)fa[i]=i;
	int ans=0;
	for(int i=1;i<=cnt;++i){
		int x=e[i].x;
		int y=e[i].y;
		int v=e[i].v;
		if(find(x)!=find(y)){
			fa[find(x)]=find(y);
			ans+=v;
		}
	}
	printf("%d\n",ans);
	return 0;
}