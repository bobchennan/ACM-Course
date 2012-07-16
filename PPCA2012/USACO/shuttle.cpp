/*
	ID:bobchenna1
	LANG:C++
	TASK:shuttle
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
int a[1001];
int main(){
	freopen("shuttle.in","r",stdin);
	freopen("shuttle.out","w",stdout);
	int n,p=1,cnt=0;
	scanf("%d",&n);
	for(int i=1;i<=n;++i){
		a[++a[0]]=-1*p;
		for(int j=1;j<=i;++j)
			a[++a[0]]=2*p;
		p*=-1;
	}
	for(int i=n-1;i>=0;--i){
		a[++a[0]]=1*p;
		for(int j=1;j<=i;++j)
			a[++a[0]]=2*p;
		p*=-1;
	}
	int x=n+1;
	for(int i=1;i<=a[0];++i){
		++cnt;
		printf("%d",x+=a[i]);
		if(i==a[0]||cnt==20){
			if(cnt==20)cnt=0;
			puts("");
		}
		else putchar(' ');
	}
	return 0;
}