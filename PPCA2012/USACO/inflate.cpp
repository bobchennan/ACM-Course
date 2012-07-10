/*
	ID:bobchenna1
	LANG:C++
	TASK:inflate
*/
#include <map>
#include <cmath>
#include <queue>
#include <cstdio>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

int m,n,f[10001];

int main(){
	freopen("inflate.in","r",stdin);
	freopen("inflate.out","w",stdout);
	scanf("%d%d",&m,&n);
	f[0]=0;
	for(int i=1;i<=n;++i){
		int x,y;
		scanf("%d%d",&x,&y);
		for(int j=0;j+y<=m;++j)
			f[j+y]=max(f[j+y],f[j]+x);
	}
	printf("%d\n",f[m]);
	return 0;
}