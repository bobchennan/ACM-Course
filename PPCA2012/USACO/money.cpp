/*
	ID:bobchenna1
	LANG:C++
	TASK:money
*/
#include <cstdio>
#include <string>
#include <algorithm>
using namespace std;

long long f[10001];

int main(){
	FILE *in=fopen("money.in","r");
	FILE *out=fopen("money.out","w");

	int v,n;
	fscanf(in,"%d%d",&v,&n);
	f[0]=1;
	for(int i=1;i<=v;++i){
		int x;
		fscanf(in,"%d",&x);
		for(int i=0;i+x<=n;++i)
			f[i+x]+=f[i];
	}
	fprintf(out,"%lld\n",f[n]);
	return 0;
}