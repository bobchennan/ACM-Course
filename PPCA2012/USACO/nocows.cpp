/*
	ID:bobchenna1
	LANG:C++
	TASK:nocows
*/
#include <set>
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

int f[201][101][129];//number\height\last ceng
int C[201][201];

int main(){
	FILE *in=fopen("nocows.in","r");
	FILE *out=fopen("nocows.out","w");
	int n,k;
	fscanf(in,"%d%d",&n,&k);
	f[1][1][1]=1;
	for(int i=1;i<=n;++i)
		C[i][0]=1;
	C[0][0]=1;
	for(int i=1;i<=n;++i)
		for(int j=1;j<=i;++j)
			C[i][j]=(C[i-1][j-1]+C[i-1][j])%9901;
	for(int i=1;i<=n;++i)
		for(int j=1;j<=k;++j)
			for(int r=1;r<=min(i,128);++r)
				if(f[i][j][r]){
					for(int l=2;l+i<=n&&l<=2*r&&l<=128;l+=2){
						f[i+l][j+1][l]+=1LL*f[i][j][r]*C[r][l/2]%9901;
						f[i+l][j+1][l]%=9901;
					}
				}
	int ans=0;
	for(int i=1;i<=min(n,128);++i)
		ans=(ans+f[n][k][i])%9901;
	fprintf(out,"%d\n",ans);
	return 0;
}