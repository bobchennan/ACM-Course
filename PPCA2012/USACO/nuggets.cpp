/*
	ID:bobchenna1
	LANG:C++
	TASK:nuggets
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

int n,m,a[11],d[271];
vector< pair<int,int> > e[271];
bool in[271];
queue<int> Q;

void addEdge(int x,int y,int v){
	e[x].push_back(make_pair(y,v));
}

int main(){
	freopen("nuggets.in","r",stdin);
	freopen("nuggets.out","w",stdout);
	scanf("%d",&n);
	for(int i=1;i<=n;++i)scanf("%d",a+i);
	m=a[1];
	for(int i=1;i<=n;++i)m=min(m,a[i]);
	for(int i=1;i<=n;++i)
		for(int j=0;j<m;++j)
			addEdge(j,(j+a[i])%m,(j+a[i])/m);
	memset(d,44,sizeof(d));
	Q.push(0);in[0]=1;d[0]=0;
	while(!Q.empty()){
		int x=Q.front();Q.pop();in[x]=0;
		for(int i=0;i<e[x].size();++i){
			int y=e[x][i].first;
			int v=e[x][i].second;
			if(d[x]+v<d[y]){
				d[y]=d[x]+v;
				if(!in[y]){
					Q.push(y);
					in[y]=1;
				}
			}
		}
	}
	bool zero=0;
	for(int i=1;i<m;++i)
		if(!d[i]||d[i]>10000000)
			zero=1;
	if(zero)puts("0");
	else{
		int ans=0;
		for(int i=1;i<m;++i)
			ans=max(ans,(d[i]-1)*m+i);
		printf("%d\n",ans);
	}
	return 0;
}