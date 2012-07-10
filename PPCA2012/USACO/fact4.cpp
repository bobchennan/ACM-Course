/*
	ID:bobchenna1
	LANG:C++
	TASK:fact4
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

int main(){
	freopen("fact4.in","r",stdin);
	freopen("fact4.out","w",stdout);
	int n,x=1,fi=0,er=0;scanf("%d",&n);
	for(int i=1;i<=n;++i){
		int t=i;
		while(t%5==0)fi++,t/=5;
		while(t%2==0)er++,t/=2;
		x=(x*t)%10;
	}
	for(int i=0;i<fi-er;++i)
		x=(x*5)%10;
	for(int i=0;i<er-fi;++i)
		x=(x<<1)%10;
	printf("%d\n",x);
	return 0;
}