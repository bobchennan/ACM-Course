/*
	ID:bobchenna1
	LANG:C++
	TASK:subset
*/
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

long long f[1001];
int main(){
	FILE *in=fopen("subset.in","r");
	FILE *out=fopen("subset.out","w");
	int n;
	fscanf(in,"%d",&n);
	int sum=(n+1)*n/2;
	f[0]=1;
	for(int i=1;i<=n;++i)
		for(int j=sum;j>=i;--j)
			f[j]+=f[j-i];
	if(sum&1)fprintf(out,"0\n");
	else fprintf(out,"%lld\n",f[sum/2]/2);
	return 0;
}