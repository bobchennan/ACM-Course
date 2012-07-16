/*
	ID:bobchenna1
	LANG:C++
	TASK:frameup
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
int n,m,lx[31],rx[31],ly[31],ry[31],d[31],cnt;
char s[31][31],h[31];
bool app[26];
vector<int> e[26];
void DFS(int now){
	if(now==cnt){
		for(int i=0;i<now;++i)putchar(h[i]);
		puts("");
		return;
	}
	for(int i=0;i<26;++i){
		if(!app[i])continue;
		if(d[i]==0){
			app[i]=0;
			h[now]='A'+i;
			for(int j=0;j<e[i].size();++j)
				--d[e[i][j]];
			DFS(now+1);
			app[i]=1;
			for(int j=0;j<e[i].size();++j)
				++d[e[i][j]];
		}
	}
}
int main(){
	freopen("frameup.in","r",stdin);
	freopen("frameup.out","w",stdout);
	scanf("%d%d",&n,&m);
	for(int i=0;i<n;++i)scanf("%s",s[i]);
	for(int i=0;i<26;++i)
		lx[i]=ly[i]=(~0U>>1),rx[i]=ry[i]=-(~0U>>2);
	for(int i=0;i<n;++i)
		for(int j=0;j<m;++j){
			int v=s[i][j]-'A';
			if(v<0||v>=26)continue;
			app[v]=1;
			lx[v]=min(lx[v],i);
			rx[v]=max(rx[v],i);
			ly[v]=min(ly[v],j);
			ry[v]=max(ry[v],j);
		}
	for(int i=0;i<26;++i){
		if(!app[i])continue;
		for(int j=ly[i];j<=ry[i];++j){
			if(s[lx[i]][j]!='A'+i)
				e[i].push_back(s[lx[i]][j]-'A');
			if(s[rx[i]][j]!='A'+i)
				e[i].push_back(s[rx[i]][j]-'A');
		}
		for(int j=lx[i];j<=rx[i];++j){
			if(s[j][ly[i]]!='A'+i)
				e[i].push_back(s[j][ly[i]]-'A');
			if(s[j][ry[i]]!='A'+i)
				e[i].push_back(s[j][ry[i]]-'A');
		}
	}
	for(int i=0;i<26;++i)
		for(int j=0;j<e[i].size();++j)
			++d[e[i][j]];
	for(int i=0;i<26;++i)
		cnt+=app[i];
	DFS(0);
	return 0;
}