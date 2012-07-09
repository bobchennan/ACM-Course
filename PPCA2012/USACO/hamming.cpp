/*
	ID:bobchenna1
	LANG:C++
	TASK:hamming
*/
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

int n,b,d,h[65];

int cnt(int x,int y){
	int ret=0;
	for(int i=1;i<=b;++i){
		if((x&1)!=(y&1))
			++ret;
		x>>=1,y>>=1;
	}
	return ret;
}

bool search(int now,int u){
	if(now==n){
		return true;
	}
	for(int i=u;i<=(1<<b);++i){
		bool ok=1;
		for(int j=0;j<now;++j){
			if(cnt(h[j],i)<d){
				ok=false;
				break;
			}
		}
		if(ok){
			h[now]=i;
			if(search(now+1,i+1))return true;
		}
	}
	return false;
}

int main(){
	FILE *in=fopen("hamming.in","r");
	FILE *out=fopen("hamming.out","w");
	fscanf(in,"%d%d%d",&n,&b,&d);
	search(0,0);
	for(int i=0;i<n;++i){
		fprintf(out,"%d",h[i]);
		if(i%10==9)fprintf(out,"\n");
		else if(i!=n-1)fprintf(out," ");
		else fprintf(out,"\n");
	}
	return 0;
}