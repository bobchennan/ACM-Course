/*
	ID:bobchenna1
	LANG:C++
	TASK:lgame
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
int ch[]={2,5,4,4,1,6,5,5,1,7,6,3,5,2,3,5,7,2,1,2,4,6,6,7,5,7};
char s[11],t[11];
int cnt[26],tmp[26],tot=0,ans=0;
struct word{
	char s[17];
	int v,tt[26],id;
	word(){}
	bool operator <(const word &y)const{
		return v<y.v;
	}
}a[40001];
vector<string> aans;
void update(int idx){
	if(a[idx].v>ans){
		ans=a[idx].v;
		aans.clear();
		aans.push_back(string(a[idx].s));
	}
	else if(a[idx].v==ans)
		aans.push_back(string(a[idx].s));
}
int main(){
	freopen("lgame.in","r",stdin);
	scanf("%s",s);
	for(int i=0;i<strlen(s);++i)++cnt[s[i]-'a'];
	FILE *in=fopen("lgame.dict","r");
	while(fscanf(in,"%s",t)){
		if(t[0]=='.')break;
		memset(tmp,0,sizeof(tmp));
		for(char *i=t;*i;++i)
			++tmp[*i-'a'];
		bool ok=1;
		for(int i=0;i<26;++i)
			if(tmp[i]>cnt[i]){
				ok=0;
				break;
			}
		if(!ok)continue;
		++tot;
		memcpy(a[tot].s,t,sizeof(t));
		memcpy(a[tot].tt,tmp,sizeof(tmp));
		for(char *i=t;*i;++i)
			a[tot].v+=ch[(*i)-'a'];
		a[tot].id=tot;
		update(tot);
	}
	sort(a+1,a+tot+1);
	for(int i=tot-1;i;--i)
		for(int j=tot;j>i;--j){
			if(a[i].v+a[j].v<ans)break;
			bool ok=1;
			for(int k=0;k<26;++k)
				if(a[i].tt[k]+a[j].tt[k]>cnt[k]){
					ok=0;
					break;
				}
			if(!ok)continue;
			if(a[i].id<a[j].id){
				strcpy(a[tot+1].s,a[i].s);
				strcat(a[tot+1].s," ");
				strcat(a[tot+1].s,a[j].s);
			}
			else{
				strcpy(a[tot+1].s,a[j].s);
				strcat(a[tot+1].s," ");
				strcat(a[tot+1].s,a[i].s);
			}
			a[tot+1].v=a[i].v+a[j].v;
			update(tot+1);
		}
	freopen("lgame.out","w",stdout);
	printf("%d\n",ans);
	sort(aans.begin(),aans.end());
	for(int i=0;i<aans.size();++i)
		printf("%s\n",aans[i].c_str());
	return 0;
}