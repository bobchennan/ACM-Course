/*
	ID:bobchenna1
	LANG:C++
	TASK:runround
*/
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

int s[11],t[11];
bool check(int n){
	int len=0,app=0;
	while(n){
		s[len++]=n%10;
		if(n%10==0)return 0;
		if(app&(1<<(n%10)))return 0;
		app|=(1<<(n%10));
		n/=10;
	}
	for(int i=0;i<len;++i)
		t[i]=s[len-1-i];
	int cnt=0,p=0;
	for(int i=0;i<len;++i){
		if(cnt&(1<<p))return 0;
		cnt|=(1<<p);
		p=(p+t[p])%len;
	}
	return p==0;
}

int main(){
	FILE *in=fopen("runround.in","r");
	FILE *out=fopen("runround.out","w");

	int n;
	fscanf(in,"%d",&n);
	for(++n;;++n)
		if(check(n))
			break;
	fprintf(out,"%d\n",n);
	return 0;
}