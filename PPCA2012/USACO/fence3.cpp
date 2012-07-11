/*
	ID:bobchenna1
	LANG:C++
	TASK:fence3
*/
#include <set>
#include <map>
#include <cmath>
#include <queue>
#include <ctime>
#include <cstdio>
#include <vector>
#include <string>
#include <cstdlib>
#include <climits>
#include <cstring>
#include <algorithm>
using namespace std;

const double eps=1e-8;
int n;
pair<double,double> x[151],y[151],h[11];

double dis(pair<double,double> a,pair<double,double> b){
	return sqrt((a.first-b.first)*(a.first-b.first)+(a.second-b.second)*(a.second-b.second));
}
double cross(pair<double,double> a,pair<double,double> b,pair<double,double> c){
	return (b.first-a.first)*(c.second-a.second)-(b.second-a.second)*(c.first-a.first);
}
double dot(pair<double,double> a,pair<double,double> b,pair<double,double> c){
	return (b.first-a.first)*(c.first-a.first)+(b.second-a.second)*(c.second-a.second);
}
double calc(pair<double,double> a,pair<double,double> b,pair<double,double> c){
	if(dis(b,c)<eps)return dis(a,b);
	if(dot(b,c,a)<0)return dis(b,a);
	if(dot(c,b,a)<0)return dis(c,a);
	double h=fabs(cross(a,b,c))/dis(b,c);
	return h;
}
double calc(pair<double,double> b){
	double ret=0;
	for(int i=1;i<=n;++i)
		ret+=calc(b,x[i],y[i]);
	return ret;
}

int main(){
	freopen("fence3.in","r",stdin);
	freopen("fence3.out","w",stdout);
	scanf("%d",&n);
	for(int i=1;i<=n;++i){
		int x1,y1,x2,y2;
		scanf("%d%d%d%d",&x1,&y1,&x2,&y2);
		x[i]=make_pair(x1,y1);
		y[i]=make_pair(x2,y2);
	}
	srand(unsigned(time(0)));
	for(int i=1;i<=10;++i){
		h[i].first=rand()/double(INT_MAX)*100;
		h[i].second=rand()/double(INT_MAX)*100;
	}
	double r=50;
	while(r>eps){
		for(int i=1;i<=10;++i){
			bool good=1;
			while(good){
				good=0;
				pair<double,double> best=h[i];
				pair<double,double> tmp=make_pair(h[i].first-r,h[i].second);
				if(calc(tmp)<calc(best))best=tmp,good=1;
				tmp=make_pair(h[i].first+r,h[i].second);
				if(calc(tmp)<calc(best))best=tmp,good=1;
				tmp=make_pair(h[i].first,h[i].second-r);
				if(calc(tmp)<calc(best))best=tmp,good=1;
				tmp=make_pair(h[i].first,h[i].second+r);
				if(calc(tmp)<calc(best))best=tmp,good=1;
				h[i]=best;
			}
		}
		r=r*0.8;
	}
	double ans=1e100;
	pair<double,double> ans1;
	for(int i=1;i<=10;++i)
		if(calc(h[i])<ans){
			ans=calc(h[i]);
			ans1=h[i];
		}
	calc(ans1);
	printf("%.1f %.1f %.1f\n",ans1.first,ans1.second,ans);
	return 0;
}