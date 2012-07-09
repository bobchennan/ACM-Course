/*
	ID:bobchenna1
	LANG:C++
	TASK:preface
*/
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

char s[]={'I','V','X','L','C','D','M'};
int cnt[7];

void calc(int v){
	int qian=0;
	int bai=0;
	int shi=0;
	int ge=0;
	if(v>=1000)
		qian=v/1000;
	if(v>=100)
		bai=v/100%10;
	if(v>=10)
		shi=v/10%10;
	ge=v%10;
	switch(qian){
		case 1:cnt[6]++;break;
		case 2:cnt[6]+=2;break;
		case 3:cnt[6]+=3;break;
	}
	switch(bai){
		case 1:cnt[4]++;break;
		case 2:cnt[4]+=2;break;
		case 3:cnt[4]+=3;break;
		case 4:cnt[5]++,cnt[4]++;break;
		case 5:cnt[5]++;break;
		case 6:cnt[5]++;cnt[4]++;break;
		case 7:cnt[5]++;cnt[4]+=2;break;
		case 8:cnt[5]++;cnt[4]+=3;break;
		case 9:cnt[4]++;cnt[6]++;break;
	}
	switch(shi){
		case 1:cnt[2]++;break;
		case 2:cnt[2]+=2;break;
		case 3:cnt[2]+=3;break;
		case 4:cnt[2]++;cnt[3]++;break;
		case 5:cnt[3]++;break;
		case 6:cnt[3]++;cnt[2]++;break;
		case 7:cnt[3]++;cnt[2]+=2;break;
		case 8:cnt[3]++;cnt[2]+=3;break;
		case 9:cnt[2]++;cnt[4]++;break;
	}
	switch(ge){
		case 1:cnt[0]++;break;
		case 2:cnt[0]+=2;break;
		case 3:cnt[0]+=3;break;
		case 4:cnt[0]++;cnt[1]++;break;
		case 5:cnt[1]++;break;
		case 6:cnt[1]++;cnt[0]++;break;
		case 7:cnt[1]++;cnt[0]+=2;break;
		case 8:cnt[1]++;cnt[0]+=3;break;
		case 9:cnt[0]++;cnt[2]++;break;
	}
}

int main(){
	FILE *in=fopen("preface.in","r");
	FILE *out=fopen("preface.out","w");
	int n;
	fscanf(in,"%d",&n);
	for(int i=0;i<7;++i)cnt[i]=0;
	for(int i=1;i<=n;++i)
		calc(i);
	for(int i=0;i<7;++i)
		if(cnt[i])
			fprintf(out,"%c %d\n",s[i],cnt[i]);
	return 0;
}