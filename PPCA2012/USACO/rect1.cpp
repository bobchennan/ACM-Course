/*
	ID:bobchenna1
	LANG:C++
	TASK:rect1
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
int A,B,N;
struct rect{
	int x1,y1,x2,y2,v;
	rect(){}
	rect(int x1,int y1,int x2,int y2,int v):x1(x1),y1(y1),x2(x2),y2(y2),v(v){}
};
rect a[1001];
int cnt=0,area=0,ans[2501];
void DFS(int lx,int ly,int rx,int ry,int t)
{
    if(t==0)
        return ;
    if(a[t].x1>=rx||a[t].y1>=ry||a[t].x2<=lx||a[t].y2<=ly)
    {
        DFS(lx,ly,rx,ry,t-1);
    }
    else
    {
        int k1,k2,k3,k4;
        k1=max(lx,a[t].x1);
        k2=min(rx,a[t].x2);
        if(lx<k1)
            DFS(lx,ly,k1,ry,t-1);
        if(rx>k2)
            DFS(k2,ly,rx,ry,t-1);
 
         
        k3=max(ly,a[t].y1);
        k4=min(ry,a[t].y2);
        if(ly<k3)
            DFS(k1,ly,k2,k3,t-1);
        if(ry>k4)
            DFS(k1,k4,k2,ry,t-1);
        ans[a[t].v]+=abs(k2-k1)*abs(k4-k3);
        area-=abs(k2-k1)*abs(k4-k3);
    }
     
}
int main(){
	freopen("rect1.in","r",stdin);
	freopen("rect1.out","w",stdout);
	scanf("%d%d%d",&A,&B,&N);
	for(int i=1;i<=N;++i){
		int x1,y1,x2,y2,v;
		scanf("%d%d%d%d%d",&x1,&y1,&x2,&y2,&v);
		a[++cnt]=rect(x1,y1,x2,y2,v);
	}
	area=A*B;
	DFS(0,0,A,B,N);
	ans[1]+=area;
	for(int i=1;i<=2500;++i)
		if(ans[i])
			printf("%d %d\n",i,ans[i]);
	return 0;
}