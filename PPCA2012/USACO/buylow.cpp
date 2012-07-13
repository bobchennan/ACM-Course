/*
	ID:bobchenna1
	LANG:C++
	TASK:buylow
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
const int M=100000000;
map<int,int> h;
int nt[5001];
int n,a[5001],f[5001];
int g[5001][101],ans[101],tmp[101];
void myplus(int *x,int *y){
	int v=0;
	for(int i=1;i<=max(x[0],y[0]);++i){
		if(i<=x[0])v+=x[i];
		if(i<=y[0])v+=y[i];
		tmp[i]=v%M;
		v/=M;
	}
	tmp[0]=max(x[0],y[0]);
	while(v)
		tmp[++tmp[0]]=v%M,v/=M;
	memcpy(x,tmp,sizeof(tmp));
}
int main(){
	freopen("buylow.in","r",stdin);
	freopen("buylow.out","w",stdout);
	scanf("%d",&n);for(int i=1;i<=n;++i)scanf("%d",a+i);
	for(int i=n;i;--i){
		if(h.find(a[i])==h.end())
			nt[i]=0;
		else nt[i]=h[a[i]];
		h[a[i]]=i;
	}
	g[0][0]=g[0][1]=1;
	for(int i=1;i<=n;++i)
		for(int j=0;j<i;++j)
			if(g[j]&&(j==0||a[j]>a[i])){
				if(nt[j]!=0&&nt[j]<i)continue;
				if(f[j]+1==f[i]){
					myplus(g[i],g[j]);
				}
				else if(f[j]+1>f[i]){
					f[i]=f[j]+1;
					memcpy(g[i],g[j],sizeof(g[j]));
				}
			}
	int mv=0;
	for(int i=1;i<=n;++i){
		if(nt[i])continue;
		if(f[i]>mv){
			mv=f[i];
			memcpy(ans,g[i],sizeof(g[i]));
		}
		else if(f[i]==mv)
			myplus(ans,g[i]);
			
	}
	printf("%d ",mv);
	for(int i=ans[0];i;--i)
		if(i==ans[0])printf("%d",ans[i]);
		else printf("%08d",ans[i]);
	puts("");
	return 0;
}