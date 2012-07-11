/*
	ID:bobchenna1
	LANG:C++
	TASK:butter
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
int n,p,c,tot[801],d[801];
vector< pair<int,int> > e[801];
queue<int> Q;
bool in[801];
int SPFA(int u){
	memset(d,44,sizeof(d));
	d[u]=0;Q.push(u);in[u]=1;
	while(!Q.empty()){
		int x=Q.front();Q.pop();in[x]=0;
		for(int i=0;i<e[x].size();++i){
			int y=e[x][i].first;
			int v=e[x][i].second;
			if(d[x]+v<d[y]){
				d[y]=d[x]+v;
				if(!in[y]){
					in[y]=1;
					Q.push(y);
				}
			}
		}
	}
	int ret=0;
	for(int i=1;i<=p;++i)ret+=tot[i]*d[i];
	return ret;
}
int main(){
	freopen("butter.in","r",stdin);
	freopen("butter.out","w",stdout);
	scanf("%d%d%d",&n,&p,&c);
	for(int i=1;i<=n;++i){
		int x;
		scanf("%d",&x);
		++tot[x];
	}
	for(;c--;){
		int x,y,z;
		scanf("%d%d%d",&x,&y,&z);
		e[x].push_back(make_pair(y,z));
		e[y].push_back(make_pair(x,z));
	}
	int ans=(~0U>>1);
	for(int i=1;i<=p;++i)
		ans=min(ans,SPFA(i));
	printf("%d\n",ans);
	return 0;
}