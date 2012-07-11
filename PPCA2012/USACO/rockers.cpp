/*
	ID:bobchenna1
	LANG:C++
	TASK:rockers
*/
#include <set>
#include <map>
#include <cmath>
#include <queue>
#include <ctime>
#include <cstdio>
#include <vector>
#include <string>
#include <cstdlib>
#include <climits>
#include <cstring>
#include <algorithm>
using namespace std;
int N,T,M,a[21];
int f[23][23][23];
int main(){
	freopen("rockers.in","r",stdin);
	freopen("rockers.out","w",stdout);
	scanf("%d%d%d",&N,&T,&M);
	for(int i=1;i<=N;++i)scanf("%d",a+i);
	int ans=0;
	for(int i=1;i<=N;++i)
		for(int j=1;j<=M;++j){
			f[i][j][0]=max(f[i][j-1][T],f[i-1][j][0]);
			for(int k=1;k<=T;++k){
				f[i][j][k]=f[i-1][j][k];
				f[i][j][k]=max(f[i][j][k],f[i][j][k-1]);
				if(k>=a[i])f[i][j][k]=max(f[i][j][k],f[i-1][j][k-a[i]]+1);
			}
		}
	for(int i=0;i<=N+1;++i)
		for(int j=0;j<=M+1;++j)
			for(int k=0;k<=T+1;++k)
				ans=max(ans,f[i][j][k]);
	printf("%d\n",ans);
	return 0;
}