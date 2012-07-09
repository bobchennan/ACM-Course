/*
	ID:bobchenna1
	LANG:C++
	TASK:prefix
*/
#include <set>
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

char s[201][11],t[200003];
bool f[200003];
int l[201];
int main(){
	FILE *in=fopen("prefix.in","r");
	FILE *out=fopen("prefix.out","w");
	int cnt=0;
	while(fscanf(in,"%s",s[++cnt])){
		if(s[cnt][0]=='.'){
			--cnt;
			break;
		}
		l[cnt]=strlen(s[cnt]);
	}
	int len=0;
	while(fscanf(in,"%s",t+len)!=EOF){
		int tmp=strlen(t+len);
		len+=tmp;
	}
	f[0]=1;
	int ans=0;
	for(int i=1;i<=len;++i){
		if(!f[i-1])continue;
		ans=max(ans,i-1);
		for(int j=1;j<=cnt;++j)
			if(i+l[j]-1<=len&&!f[i+l[j]-1]){
				bool ok=1;
				for(int r=0;r<l[j];++r)
					if(t[i-1+r]!=s[j][r]){
						ok=0;break;
					}
				if(ok)
					f[i+l[j]-1]=1;
			}
	}
	if(f[len])ans=len;
	fprintf(out,"%d\n",ans);
	return 0;
}