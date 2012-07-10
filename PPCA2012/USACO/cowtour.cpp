/*
	ID:bobchenna1
	LANG:C++
	TASK:cowtour
*/
#include <cmath>
#include <cstdio>
#include <string>
#include <algorithm>
using namespace std;
int n,x[151],y[151];
double g[151][151],d[151];
inline long long dis(int i,int j){
	return 1LL*(x[i]-x[j])*(x[i]-x[j])+1LL*(y[i]-y[j])*(y[i]-y[j]);
}
int main(){
	FILE *in=fopen("cowtour.in","r");
	FILE *out=fopen("cowtour.out","w");
	int n;
	fscanf(in,"%d",&n);
	for(int i=0;i<n;++i)
		fscanf(in,"%d%d",x+i,y+i);
	for(int i=0;i<n;++i)
		for(int j=0;j<n;++j)
			g[i][j]=1e100;
	for(int i=0;i<n;++i)g[i][i]=0;
	for(int i=0;i<n;++i)
		for(int j=0;j<n;++j){
			char ch=' ';
			while(ch!='0'&&ch!='1')ch=fgetc(in);
			if(ch=='1')
				g[i][j]=sqrt(dis(i,j));
		}
	for(int i=0;i<n;++i)
		for(int j=0;j<n;++j)
			for(int k=0;k<n;++k)
				g[j][k]=min(g[j][k],g[j][i]+g[i][k]);
	double ans=1e99;
	for(int i=0;i<n;++i)
		for(int j=0;j<n;++j)
			if(g[i][j]<1e99)
				d[i]=max(d[i],g[i][j]);
	for(int i=0;i<n;++i)
		for(int j=0;j<n;++j)
			if(g[i][j]>1e99)
				ans=min(ans,sqrt(dis(i,j))+d[i]+d[j]);
	for(int i=0;i<n;++i)
		for(int j=0;j<n;++j)
			if(g[i][j]<1e99)
				ans=max(ans,g[i][j]);
	fprintf(out,"%.6f\n",ans);
	return 0;
}