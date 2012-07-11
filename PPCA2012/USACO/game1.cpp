/*
	ID:bobchenna1
	LANG:C++
	TASK:game1
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
int n,a[101],s[101],f[101][101];
int calc(int l,int r){
	if(l==r)return a[l];
	if(f[l][r]!=-1)return f[l][r];
	int &v=f[l][r];
	v=s[r]-s[l-1]-min(calc(l+1,r),calc(l,r-1));
	return v;
}
int main(){
	freopen("game1.in","r",stdin);
	freopen("game1.out","w",stdout);
	scanf("%d",&n);
	for(int i=1;i<=n;++i)scanf("%d",a+i);
	for(int i=1;i<=n;++i)s[i]=a[i]+s[i-1];
	memset(f,-1,sizeof(f));
	int tmp=calc(1,n);
	printf("%d %d\n",tmp,s[n]-tmp);
	return 0;
}