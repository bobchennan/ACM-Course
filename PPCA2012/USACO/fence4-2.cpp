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
const int L=100;
int n,rx,ry;
pair<double,double> p[203];
bool app[201];
inline int sig(double v){
	return v?v>0?1:-1:0;
}
double dis(pair<double,double> a,pair<double,double> b){
	return sqrt((a.first-b.first)*(a.first-b.first)+
			(a.second-b.second)*(a.second-b.second));
}
double cross(pair<double,double> o,pair<double,double> a,pair<double,double> b){
	return 1LL*(a.first-o.first)*(b.second-o.second)-
		1LL*(b.first-o.first)*(a.second-o.second);
}
double dot(pair<double,double> o,pair<double,double> a,pair<double,double> b){
	return 1LL*(a.first-o.first)*(b.first-o.first)+
		1LL*(a.second-o.second)*(b.second-o.second);
}
int btw(pair<double,double> x,pair<double,double> a,pair<double,double> b){
	return sig(dot(x,a,b));
}
bool cross(pair<double,double> a,pair<double,double> b,pair<double,double> c,pair<double,double> d){
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
	for(int i=1;i<=n;++i)scanf("%lf%lf",&p[i].first,&p[i].second);
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
	for(int i=1;i<=n;++i){
		int cnt=0;
		for(int k=0;k<=L;++k){
			double d=dis(p[i],p[i+1]);
			pair<double,double> tmp=make_pair(p[i].first+(p[i+1].first-p[i].first)/L*k,
											p[i].second+(p[i+1].second-p[i].second)/L*k);
			bool ok=1;
			for(int j=1;j<=n;++j)
				if(j!=i&&cross(p[i],tmp,p[j],p[j+1])){
					ok=0;break;
				}
			if(ok)++cnt;
		}
		if(cnt>=10)app[i]=1;
	}
	int ans=0;
	for(int i=1;i<=n;++i)
		ans+=app[i];
	printf("%d\n",ans);
	for(int i=1;i<n-1;++i)
		if(app[i])
			printf("%.0f %.0f %.0f %.0f\n",p[i].first,p[i].second,p[i+1].first,p[i+1].second);
	if(app[n]){
		if(app[n-1])
			printf("%.0f %.0f %.0f %.0f\n",p[1].first,p[1].second,p[n].first,p[n].second);
		printf("%.0f %.0f %.0f %.0f\n",p[n-1].first,p[n-1].second,p[n].first,p[n].second);
	}
	else if(app[n-1])
		printf("%.0f %.0f %.0f %.0f\n",p[n-1].first,p[n-1].second,p[n].first,p[n].second);
	return 0;
}