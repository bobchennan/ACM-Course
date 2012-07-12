/*
	ID:bobchenna1
	LANG:C++
	TASK:ditch
*/
	#include<cstdio>
	#include<algorithm>
	using namespace std;
    #define maxn 5011
    #define maxm 100001
    #define oo (~0U>>1)
    using namespace std;
    int n,m,ed[maxn],sz=1,h[maxn],cnt[maxn];
    struct edge
    {
    int y,v,nt;
    }e[maxm];
    int aug(int u,int t)
    {
    if(u==n)return t;
    int l=t,minh=n+1;
    for(int i=ed[u];i;i=e[i].nt)
    {
    if(e[i].v<1)continue;
    int d=e[i].y;
    if(h[d]+1==h[u])
    {
    int dd=min(t,e[i].v);
    dd=aug(d,dd);
    e[i].v-=dd,e[i^1].v+=dd,t-=dd;
    if(h[1]>n)return l-t;
    if(!t)break;
    }
    minh=min(minh,h[d]);
    }
    if(l==t)
    {
    cnt[h[u]]--;
    if(!cnt[h[u]])h[1]=n+1;
    h[u]=minh+1;
    cnt[h[u]]++;
    }
    return l-t;
    }
    int main()
    {
    	freopen("ditch.in","r",stdin);
    	freopen("ditch.out","w",stdout);
    scanf("%d%d",&m,&n);
    for(int i=1;i<=m;i++)
    {
    int x,y,z;
    scanf("%d%d%d",&x,&y,&z);
    if(x==y)continue;
    ++sz,e[sz].y=y,e[sz].v=z,e[sz].nt=ed[x],ed[x]=sz;
    ++sz,e[sz].y=x,e[sz].v=0,e[sz].nt=ed[y],ed[y]=sz;
    }
    cnt[0]=n;
    int ans=0;
    while(h[1]<=n)
    ans+=aug(1,oo);
    printf("%d\n",ans);
    return 0;
    }
