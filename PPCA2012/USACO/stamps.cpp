/*
	ID:bobchenna1
	LANG:C++
	TASK:stamps
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
int f[2000003],a[201];
int main(){
	freopen("stamps.in","r",stdin);
	freopen("stamps.out","w",stdout);
	int n,k;
	scanf("%d%d",&k,&n);
	for(int i=1;i<=n;++i)scanf("%d",a+i);
	memset(f,44,sizeof(f));
	f[0]=0;
	for(int i=0;;++i){
		if(f[i]>k){
			printf("%d\n",i-1);
			break;
		}
		for(int j=1;j<=n&&i+a[j]<=2000000;++j)
			f[i+a[j]]=min(f[i+a[j]],f[i]+1);
	}
	return 0;
}