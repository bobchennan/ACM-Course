/*
	ID:bobchenna1
	LANG:C++
	TASK:job
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
int n,m1,m2,a[31],b[31];
int h1[1001],h2[1001];
multiset< pair<int,int> > h;
int main(){
	freopen("job.in","r",stdin);
	freopen("job.out","w",stdout);
	scanf("%d",&n);
	scanf("%d%d",&m1,&m2);
	for(int i=1;i<=m1;++i)scanf("%d",a+i);
	for(int i=1;i<=m2;++i)scanf("%d",b+i);
	h.clear();
	for(int i=1;i<=m1;++i)h.insert(make_pair(0,a[i]));
	for(;h1[0]<n;){
		pair<int,int> now=*h.begin();
		h.erase(h.begin());
		if(now.first)h1[++h1[0]]=now.first;
		h.insert(make_pair(now.first+now.second,now.second));
	}
	h.clear();
	for(int i=1;i<=m2;++i)h.insert(make_pair(0,b[i]));
	for(;h2[0]<n;){
		pair<int,int> now=*h.begin();
		h.erase(h.begin());
		if(now.first)h2[++h2[0]]=now.first;
		h.insert(make_pair(now.first+now.second,now.second));
	}
	sort(h2+1,h2+n+1);
	int ans=0;
	for(int i=1;i<=n;++i)
		ans=max(ans,h1[i]+h2[n-i+1]);
	printf("%d %d\n",h1[n],ans);
	return 0;
}