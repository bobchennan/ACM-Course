/*
	ID:bobchenna1
	LANG:C++
	TASK:holstein
*/
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

int n,m,a[31],b[31][31],c[31];

int count(int v){
		int ret=0;
		for(;v!=0;){
			if((v&1)==1)++ret;
			v>>=1;
		}
		return ret;
	}

int main(){
	FILE *in=fopen("holstein.in","r");
	FILE *out=fopen("holstein.out","w");
	fscanf(in,"%d",&n);
	for(int i=0;i<n;++i)
		fscanf(in,"%d",a+i);
	fscanf(in,"%d",&m);
	for(int i=0;i<m;++i)
		for(int j=0;j<n;++j)
			fscanf(in,"%d",b[i]+j);
		int best=0,ans=100;
		for(int i=0;i<(1<<m);++i){
			for(int k=0;k<n;++k)c[k]=0;
			for(int j=0;j<m;++j)
				if((i&(1<<j))!=0)
					for(int k=0;k<n;++k)
						c[k]+=b[j][k];
			bool ok=true;
			for(int k=0;k<n;++k)
				if(c[k]<a[k]){
					ok=false;
					break;
				}
			if(ok==false)continue;
			int tmp=count(i);
			if(tmp<ans){
				ans=tmp;best=i;
			}
		}
		fprintf(out,"%d",ans);
		for(int i=0;i<m;++i)
			if((best&(1<<i))!=0)
				fprintf(out," %d",i+1);
		fprintf(out,"\n");
		return 0;
}