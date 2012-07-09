/*
	ID:bobchenna1
	LANG:C++
	TASK:lamps
*/
#include <set>
#include <cstdio>
#include <string>
#include <algorithm>
using namespace std;

int n,c,liang[101],guan[101];
set<string> ans;

int main(){
	FILE *in=fopen("lamps.in","r");
	FILE *out=fopen("lamps.out","w");
	fscanf(in,"%d",&n);
	fscanf(in,"%d",&c);
	for(;;){
		int x;fscanf(in,"%d",&x);
		if(x==-1)break;
		liang[++liang[0]]=x;
	}
	for(;;){
		int x;fscanf(in,"%d",&x);
		if(x==-1)break;
		guan[++guan[0]]=x;
	}

	for(int i=0;i<2;++i)
		for(int j=0;j<2;++j)
			for(int k=0;k<2;++k)
				for(int l=0;l<2;++l){
					if(i+j+k+l>c)continue;
					if(((i+j+k+l)&1)!=(c&1))continue;
					string s="";
					for(int r=0;r<n;++r)s+='1';
					if(i)for(int r=0;r<n;++r)s[r]='1'-s[r]+'0';
					if(j)for(int r=0;r<n;r+=2)s[r]='1'-s[r]+'0';
					if(k)for(int r=1;r<n;r+=2)s[r]='1'-s[r]+'0';
					if(l)for(int r=0;3*r<n;++r)s[3*r]='1'-s[3*r]+'0';
					bool ok=1;
					for(int r=1;r<=guan[0];++r)
						ok&=s[guan[r]-1]=='0';
					for(int r=1;r<=liang[0];++r)
						ok&=s[liang[r]-1]=='1';
					if(ok)ans.insert(s);
				}
	if(!ans.size())fprintf(out,"IMPOSSIBLE\n");
	for(set<string>::iterator it=ans.begin();it!=ans.end();++it)
		fprintf(out,"%s\n",(*it).c_str());
	return 0;
}