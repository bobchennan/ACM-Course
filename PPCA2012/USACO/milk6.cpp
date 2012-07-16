/*
	ID:bobchenna1
	LANG:C++
	TASK:milk6
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
int n,m,e[1001][3];
int ed[33],g[3007][3];
int h[1003],cnt[1003];
long long aug(int u,long long t){
	if(u==n)return t;
	long long l=t;int minh=n+1;
	for(int i=ed[u];i;i=g[i][2]){
		if(g[i][1]<1)continue;
		int v=g[i][0];
		if(h[v]+1==h[u]){
			long long tmp=min(t,1LL*g[i][1]);
			tmp=aug(v,tmp);
			g[i][1]-=tmp,g[i^1][1]+=tmp,t-=tmp;
			if(h[1]>n)return l-t;
			if(!t)break;
		}
		minh=min(minh,h[v]);
	}
	if(l==t){
		--cnt[h[u]];
		if(!cnt[h[u]])h[1]=n+1;
		h[u]=minh+1;
		++cnt[h[u]];
	}
	return l-t;
}
int main(){
	freopen("milk6.in","r",stdin);
	freopen("milk6.out","w",stdout);
	scanf("%d%d",&n,&m);
	for(int i=1;i<=m;++i)
		scanf("%d%d%d",e[i]+0,e[i]+1,e[i]+2);
	for(int i=1;i<=m;++i){
		g[2*i][0]=e[i][1];
		g[2*i][1]=e[i][2]*1001+1;
		g[2*i][2]=ed[e[i][0]];
		ed[e[i][0]]=2*i;

		g[2*i+1][0]=e[i][0];
		g[2*i+1][1]=0;
		g[2*i+1][2]=ed[e[i][1]];
		ed[e[i][1]]=2*i+1;
	}
	cnt[0]=n;
	long long ans=0;
	while(h[1]<=n)
		ans+=aug(1,1LL<<62);
	printf("%d %d\n",int(ans/1001),int(ans%1001));
	for(int _=1;_<=m;++_){
		memset(ed,0,sizeof(ed));
		for(int i=1;i<=m;++i){
			if(i==_||!e[i][2])continue;
			g[2*i][0]=e[i][1];
			g[2*i][1]=e[i][2]*1001+1;
			g[2*i][2]=ed[e[i][0]];
			ed[e[i][0]]=2*i;

			g[2*i+1][0]=e[i][0];
			g[2*i+1][1]=0;
			g[2*i+1][2]=ed[e[i][1]];
			ed[e[i][1]]=2*i+1;
		}

		memset(h,0,sizeof(h));
		cnt[0]=n;
		long long ans2=0;
		while(h[1]<=n)ans2+=aug(1,1LL<<62);
		if(ans-ans2==e[_][2]*1001+1){
			printf("%d\n",_);
			e[_][2]=0;
			ans=ans2;
		}
	}
	return 0;
}