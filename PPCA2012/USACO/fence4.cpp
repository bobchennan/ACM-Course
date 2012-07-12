/*
	ID:bobchenna1
	LANG:C++
	TASK:fence4
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
int n,rx,ry;
pair<int,int> p[203];
inline int sig(long long v){
	return v?v>0?1:-1:0;
}
long long cross(pair<int,int> o,pair<int,int> a,pair<int,int> b){
	return 1LL*(a.first-o.first)*(b.second-o.second)-
		1LL*(b.first-o.first)*(a.second-o.second);
}
long long dot(pair<int,int> o,pair<int,int> a,pair<int,int> b){
	return 1LL*(a.first-o.first)*(b.first-o.first)+
		1LL*(a.second-o.second)*(b.second-o.second);
}
int btw(pair<int,int> x,pair<int,int> a,pair<int,int> b){
	return sig(dot(x,a,b));
}
bool cross(pair<int,int> a,pair<int,int> b,pair<int,int> c,pair<int,int> d){
	int d1=sig(cross(a,b,c));
	int d2=sig(cross(a,b,d));
	int d3=sig(cross(c,d,a));
	int d4=sig(cross(c,d,b));
	if((d1^d2)==-2&&(d3^d4)==-2)return 1;
	if(d1==0&&btw(c,a,b)<=0)return 1;
	if(d2==0&&btw(d,a,b)<=0)return 1;
	if(d3==0&&btw(a,c,d)<=0)return 1;
	if(d4==0&&btw(b,c,d)<=0)return 1;
	return 0;
}
int main(){
	freopen("fence4.in","r",stdin);
	freopen("fence4.out","w",stdout);
	scanf("%d%d%d",&n,&rx,&ry);
	for(int i=1;i<=n;++i)scanf("%d%d",&p[i].first,&p[i].second);
	p[n+1]=p[1];
	bool ok=1;
	for(int i=1;i<=n&&ok;++i)
		for(int j=i+1;j<=n&&ok;++j)
			if(cross(p[i],p[i+1],p[j],p[j+1]))
				ok=0;
	if(!ok){
		puts("NOFENCE");
		return 0;
	}
	return 0;
}