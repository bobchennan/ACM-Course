/*
	ID:bobchenna1
	LANG:C++
	TASK:camelot
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
int n,m;
int kx,ky;
int d[31][27][31][27];
queue< pair<int,int> > Q;
void BFS(int r[][27],int x,int y){
	Q.push(x,y);
}
int main(){
	freopen("camelot.in","r",stdin);
	freopen("camelot.out","w",stdout);
	scanf("%d%d%*c",&n,&m);
	char ch;
	scanf("%c%d%*c",&ch,&kx);
	ky=ch-'A'+1;
	char ch2;
	for(int i=1;i<=n;++i)
		for(int j=1;j<=m;++j)
			BFS(d[i][j],i,j);
	return 0;
}