/*
	ID:bobchenna1
	LANG:C++
	TASK:concom
*/
#include <queue>
#include <cstdio>
#include <cstring>
#include <vector>
#include <algorithm>
using namespace std;
int n,c[101];
vector< pair<int,int> > h[101];
bool ok[101];
queue<int> Q;
int main(){
	FILE *in=fopen("concom.in","r");
	FILE *out=fopen("concom.out","w");
	for(fscanf(in,"%d",&n);n--;){
		int x,y,z;
		fscanf(in,"%d%d%d",&x,&y,&z);
		h[x].push_back(make_pair(y,z));
	}
	for(int i=1;i<=100;++i){
		Q.push(i);
		memset(ok,0,sizeof(ok));
		memset(c,0,sizeof(c));
		while(!Q.empty()){
			int x=Q.front();Q.pop();
			for(int i=0;i<h[x].size();++i){
				c[h[x][i].first]+=h[x][i].second;
				if(c[h[x][i].first]>=50&&!ok[h[x][i].first]){
					ok[h[x][i].first]=1;
					Q.push(h[x][i].first);
				}
			}
		}
		for(int j=1;j<=100;++j)
			if(i!=j&&ok[j])
				fprintf(out,"%d %d\n",i,j);
	}
	return 0;
}