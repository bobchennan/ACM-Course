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
const double Pi= acos(-1);
const double eps=1e-6;
int n,rx,ry,belong[40001];
pair<int,int> p[203];
double ll[203],rr[203];
vector<double> lisan;
bool app[201];
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
double dis(pair<int,int> a,pair<int,int> b){
	return sqrt((a.first-b.first)*(a.first-b.first)+
			(a.second-b.second)*(a.second-b.second));
}
double calc(pair<int,int> a,pair<int,int> b,pair<int,int> c){
	if(dis(b,c)<eps)return dis(a,b);
	if(dot(b,c,a)<0)return dis(b,a);
	if(dot(c,b,a)<0)return dis(c,a);
	double h=fabs(cross(a,b,c))/dis(b,c);
	return h;
}
void calc(int id,double l,double r){
	for(int i=0;i<lisan.size()-1;++i){
		if(belong[i])continue;
		double ml=lisan[i];
		double mr=lisan[i+1];
		if(l<ml+eps&&mr<r+eps)
			belong[i]=id;
	}
}
int main(){
	freopen("fence4.in","r",stdin);
	freopen("fence4.out","w",stdout);
	scanf("%d%d%d",&n,&rx,&ry);
	for(int i=1;i<=n;++i)scanf("%d%d",&p[i].first,&p[i].second);
	p[n+1]=p[1];
	bool ok=1;
	for(int i=1;i<=n&&ok;++i)
		for(int j=i+2;j<=n&&ok;++j)
			if((i!=1||j!=n)&&cross(p[i],p[i+1],p[j],p[j+1]))
				ok=0;
	if(!ok){
		puts("NOFENCE");
		return 0;
	}
	lisan.push_back(0);
	lisan.push_back(2*Pi);
	for(int i=1;i<=n;++i){
		double l1=atan2(p[i].second-ry,p[i].first-rx);
		double r1=atan2(p[i+1].second-ry,p[i+1].first-rx);
		if(l1<-eps)l1+=2*Pi;
		if(r1<-eps)r1+=2*Pi;
		if(l1>r1)swap(l1,r1);
		if(r1-l1>Pi)swap(l1,r1);
		ll[i]=l1,rr[i]=r1;
		lisan.push_back(l1);
		lisan.push_back(r1);
	}
	sort(lisan.begin(),lisan.end());
	lisan.erase(unique(lisan.begin(),lisan.end()),lisan.end());
	for(int i=1;i<=n;++i){
		int j=i;
		if(ll[j]>rr[j])calc(j,ll[j],2*Pi),calc(j,0,rr[j]);
		else calc(j,ll[j],rr[j]);
	}
	for(int j=0;j<lisan.size()-1;++j)
		if(lisan[j+1]-lisan[j]>eps)
			app[belong[j]]=1;
	int ans=0;
	for(int i=1;i<=n;++i)
		ans+=app[i];
	printf("%d\n",ans);
	for(int i=1;i<n-1;++i)
		if(app[i])
			printf("%d %d %d %d\n",p[i].first,p[i].second,p[i+1].first,p[i+1].second);
	if(app[n]){
		if(app[n-1])
			printf("%d %d %d %d\n",p[1].first,p[1].second,p[n].first,p[n].second);
		printf("%d %d %d %d\n",p[n-1].first,p[n-1].second,p[n].first,p[n].second);
	}
	else if(app[n-1])
		printf("%d %d %d %d\n",p[n-1].first,p[n-1].second,p[n].first,p[n].second);
	return 0;
}