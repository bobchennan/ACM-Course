/*
	ID:bobchenna1
	LANG:C++
	TASK:race3
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
int n;
vector<int> e[51];
bool vis[51],one[51],two[51],red[51];
bool DFS(int u){
	if(u==n-1)return 0;
	vis[u]=1;
	bool ret=1;
	for(int i=0;i<e[u].size();++i){
		int v=e[u][i];
		if(vis[v])continue;
		if(!DFS(v))ret=0;
	}
	return ret;
}
int main(){
	freopen("race3.in","r",stdin);
	freopen("race3.out","w",stdout);
	for(n=0;;++n){
		int x;
		for(;;){
			scanf("%d",&x);
			if(x==-1)break;
			if(x==-2)break;
			e[n].push_back(x);
		}
		if(x==-1){
			break;
		}
	}
	for(int i=1;i<n-1;++i){
		memset(vis,0,sizeof(vis));
		vis[i]=1;
		if(DFS(0))one[i]=1;
	}
	int cnt=0;
	for(int i=0;i<n;++i)cnt+=one[i];
	printf("%d",cnt);
	for(int i=0;i<n;++i)
		if(one[i])
			printf(" %d",i);
	puts("");

	for(int i=1;i<n-1;++i){
		if(!one[i])continue;
		memset(vis,0,sizeof(vis));
		vis[i]=1;
		DFS(0);
		memcpy(red,vis,sizeof(vis));

		memset(vis,0,sizeof(vis));
		DFS(i);
		int cnt=0;
		for(int j=0;j<n;++j)
			if(red[j]&&vis[j])
				++cnt;
		if(cnt==1)two[i]=1;
	}
	cnt=0;
	for(int i=1;i<n-1;++i)cnt+=two[i];
	printf("%d",cnt);
	for(int i=1;i<n-1;++i)
		if(two[i])
			printf(" %d",i);
	puts("");
	return 0;
}