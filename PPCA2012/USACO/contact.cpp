/*
	ID:bobchenna1
	LANG:C++
	TASK:contact
*/
#include <set>
#include <map>
#include <cmath>
#include <queue>
#include <climits>
#include <cstdio>
#include <vector>
#include <string>
#include <cstring>
#include <algorithm>
using namespace std;

int A,B,n;
char s[200003];
map<string,int> f;
map<int,vector<string> > h;

bool cmp(string x,string y){
	if(x.size()!=y.size())return x.size()<y.size();
	else return x<y;
}

int main(){
	freopen("contact.in","r",stdin);
	freopen("contact.out","w",stdout);
	scanf("%d%d%d",&A,&B,&n);
	int len=0;
	while(scanf("%s",s+len)!=EOF){
		int tmp=strlen(s+len);
		len+=tmp;
	}
	for(int i=0;i+A-1<len;++i){
		string t="";
		for(int j=0;j<A-1;++j)t+=s[i+j];
		for(int j=A;j<=B&&i+j-1<len;++j){
			t+=s[i+j-1];
			f[t]++;
		}
	}
	for(map<string,int>::iterator it=f.begin();it!=f.end();++it)
		h[it->second].push_back(it->first);
	for(map<int,vector<string> >::iterator it=--h.end();n--;--it){
		sort(it->second.begin(),it->second.end(),cmp);
		printf("%d\n",it->first);
		for(int j=0,k=1;j<it->second.size();++j,++k){
			printf("%s",(it->second)[j].c_str());
			if(j==it->second.size()-1||k%6==0)puts("");
			else putchar(' ');
		}
		if(it==h.begin())break;
	}
	return 0;
}