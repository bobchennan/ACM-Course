/*
	ID:bobchenna1
	LANG:C++
	TASK:spin
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

bool a[5][361];
int v[5];
int c[361];

int main(){
	freopen("spin.in","r",stdin);
	freopen("spin.out","w",stdout);
	for(int i=0;i<5;++i){
		scanf("%d",v+i);
		int cnt=0;
		for(scanf("%d",&cnt);cnt--;){
			int x,y;
			scanf("%d%d",&x,&y);
			for(int j=x;j<=x+y;++j)
				a[i][j%360]=1;
		}
	}
	bool ans=0;
	for(int i=0;i<360;++i){
		bool ok=0;
		memset(c,0,sizeof(c));
		for(int j=0;j<5;++j){
			for(int k=0;k<360;++k)
				if(a[j][k])
					++c[(k+i*v[j])%360];
		}
		for(int j=0;j<360;++j)
			if(c[j]==5){
				ans=1;
				printf("%d\n",i);
				break;
			}
		if(ans)break;
	}
	if(!ans)puts("none");
	return 0;
}