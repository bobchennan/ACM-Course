/*
	ID:bobchenna1
	LANG:C++
	TASK:cowcycle
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

int n,m,f1,f2,r1,r2;
int a[5],b[11],aa[5],bb[11];
double c[101],d[101],ans=1e100;

void s2(int u,int v){
	if(u==m){
		if(b[m-1]*a[n-1]<3*b[0]*a[0])return;
		int cnt=0;
		for(int i=0;i<n;++i)
		 	for(int j=0;j<m;++j)
		 		c[++cnt]=a[i]/double(b[j]);
		sort(c+1,c+cnt+1);
		for(int i=1;i<cnt;++i)
			d[i]=c[i+1]-c[i];
		double s=0,ss=0;
		for(int i=1;i<cnt;++i){
			s+=d[i];
			ss+=d[i]*d[i];
		}
		double ret=(ss-s*s/(cnt-1))/(cnt-1);
		if(ret<ans){
			ans=ret;
			memcpy(aa,a,sizeof(a));
			memcpy(bb,b,sizeof(b));
		}
		return;
	}
	if(u&&r2*a[n-1]<3*b[0]*a[0])return;
	for(int i=v;i<=r2;++i){
		b[u]=i;
		s2(u+1,i+1);
	}
}

void s1(int u,int v){
	if(u==n){
		if(r2*a[n-1]<3*r1*a[0])return;
		s2(0,r1);
		return;
	}
	for(int i=v;i<=f2;++i){
		a[u]=i;
		s1(u+1,i+1);
	}
}

int main(){
	freopen("cowcycle.in","r",stdin);
	freopen("cowcycle.out","w",stdout);

	scanf("%d%d",&n,&m);
	scanf("%d%d%d%d",&f1,&f2,&r1,&r2);
	s1(0,f1);
	//printf("%.7f\n",ans);
	for(int i=0;i<n;++i){
		if(i)putchar(' ');
		printf("%d",aa[i]);
	}
	puts("");
	for(int i=0;i<m;++i){
		if(i)putchar(' ');
		printf("%d",bb[i]);
	}
	puts("");
	return 0;
}