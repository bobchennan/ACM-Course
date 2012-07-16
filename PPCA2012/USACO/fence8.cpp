/*
	ID:bobchenna1
	LANG:C++
	TASK:fence8
*/
#include <set>
#include <map>
#include <cmath>
#include <queue>
#include <cstdio>
#include <vector>
#include <string>
#include <climits>
#include <cstring>
#include <algorithm>
using namespace std;
int n,a[51],m,b[1025],tot,sum[51],bsum;
bool used[1025];
bool search(int);
bool search2(int now,int p){
	bool ok=0,ret=0;
	for(int i=p;i&&!ret;--i){
		if(used[i])continue;
		if(a[now]<b[i])continue;
		if(i+1<=tot&&b[i]==b[i+1]&&!used[i+1])continue;
		ok=1;
		a[now]-=b[i];
		bsum-=b[i];
		used[i]=1;
		if(a[now]==0){
			ret|=search(now-1);
			a[now]+=b[i];
			used[i]=0;
			bsum+=b[i];
			break;
		}
		if(search2(now,i-1))ret=1;
		a[now]+=b[i];
		used[i]=0;
		bsum+=b[i];
	}
	if(!ok)return search(now-1);
	return ret;
}
bool search(int now){
	if(bsum==0)return 1;
	if(sum[now]<bsum)return 0;
	if(now<=0)return 0;
	if(search2(now,tot))return 1;
	return 0;
}
bool check(int u){
	tot=u;bsum=0;
	for(int i=1;i<=tot;++i)bsum+=b[i];
	if(search(n))
		return 1;
	else return 0;
}
int main(){
	freopen("fence8.in","r",stdin);
	freopen("fence8.out","w",stdout);
	scanf("%d",&n);
	for(int i=1;i<=n;++i)scanf("%d",a+i);
	sort(a+1,a+n+1);
	for(int i=1;i<=n;++i)sum[i]=sum[i-1]+a[i];
	scanf("%d",&m);
	for(int i=1;i<=m;++i)scanf("%d",b+i);
	sort(b+1,b+m+1);
	while(b[m]>a[n])--m;
	int l=1,r=m;
	for(int i=30;i<=m;i+=30)
		if(!check(i)){
			r=i-1;
			break;
		}
	if(r)
	while(l!=r){
		int mid=l+r+1>>1;
		if(check(mid))l=mid;
		else r=mid-1;
	}
	printf("%d\n",r);
	return 0;
}