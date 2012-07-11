/*
	ID:bobchenna1
	LANG:C++
	TASK:fence
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
int d[1025],h[20001];;
vector<int> e[1025];
bool cmp(int x,int y){
	return x>y;
}
void DFS(int u){
	
	while(e[u].size()){
		int x=*(--e[u].end());
		e[u].erase(--e[u].end());
		for(vector<int>::iterator it=e[x].begin();it!=e[x].end();++it){
			if(*it!=u)continue;
			e[x].erase(it);
			break;
		}
		DFS(x);
	}
	h[++h[0]]=u;
}
int main(){
	freopen("fence.in","r",stdin);
	freopen("fence.out","w",stdout);
	int f;
	for(scanf("%d",&f);f--;){
		int x,y;
		scanf("%d%d",&x,&y);
		++d[x];++d[y];
		e[x].push_back(y);
		e[y].push_back(x);
	}
	for(int i=1;i<=500;++i)
		if(e[i].size())
			sort(e[i].begin(),e[i].end(),cmp);
	bool ok=0;
	for(int i=1;i<=500;++i)
		if(d[i]&1){
			ok=1;
			DFS(i);
			break;
		}
	if(!ok)DFS(1);
	for(int i=h[0];i;--i)
		printf("%d\n",h[i]);
	return 0;
}