/*
	ID:bobchenna1
	LANG:C++
	TASK:fence6
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
map<int,vector<int> > s1,s2;
map<int,int> len;
int n,ans=(~0U>>1),h[201];
void search(int now,int v){
	if(h[0]&&now==h[1]){
		ans=min(ans,v);
		return;
	}
	if(v>=ans)return;
	h[++h[0]]=now;
	bool ok=0;
	for(int i=0;i<s1[now].size();++i)
		if(s1[now][i]==h[h[0]-1]){
			ok=1;
			break;
		}
	if(ok)
		for(int i=0;i<s2[now].size();++i)
			search(s2[now][i],v+len[s2[now][i]]);
	else for(int i=0;i<s1[now].size();++i)
			search(s1[now][i],v+len[s1[now][i]]);
	--h[0];
}
int main(){
	freopen("fence6.in","r",stdin);
	freopen("fence6.out","w",stdout);
	scanf("%d",&n);
	for(int i=1;i<=n;++i){
		int r=0,x,y,tmp;
		scanf("%d",&r);
		scanf("%d%d%d",&len[r],&x,&y);
		for(;x--;){
			scanf("%d",&tmp);
			s1[r].push_back(tmp);
		}
		for(;y--;){
			scanf("%d",&tmp);
			s2[r].push_back(tmp);
		}
	}
	for(map<int,int>::iterator it=len.begin();it!=len.end();++it)
		search(it->first,0);
	printf("%d\n",ans);
	return 0;
}