/*
	ID:bobchenna1
	LANG:C++
	TASK:humble
*/
#include <set>
#include <cmath>
#include <queue>
#include <climits>
#include <cstdio>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

int k,n,a[101];
set<int> h;

int main(){
	freopen("humble.in","r",stdin);
	freopen("humble.out","w",stdout);
	scanf("%d%d",&k,&n);
	for(int i=1;i<=k;++i)scanf("%d",a+i);
	for(int i=1;i<=k;++i)h.insert(a[i]);
	for(n--;n;--n){
		int x=*h.begin();
		h.erase(h.begin());
		for(int i=1;i<=k;++i){
			if((h.size()>=n&&x*a[i]>*(--h.end()))||x>INT_MAX/a[i])break;
			h.insert(x*a[i]);
		}
	}
	printf("%d\n",*(h.begin()));
	return 0;
}