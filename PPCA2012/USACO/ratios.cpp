/*
	ID:bobchenna1
	LANG:C++
	TASK:ratios
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
	freopen("ratios.in","r",stdin);
	freopen("ratios.out","w",stdout);
	int x,y,z;
	int a[3],b[3],c[3];
	scanf("%d%d%d",&x,&y,&z);
	for(int i=0;i<3;++i)
		scanf("%d%d%d",a+i,b+i,c+i);
	for(int tot=1;tot<=300;++tot)
		for(int i=0;i<=tot;++i)
			for(int j=0;j<=tot-i;++j){
				int k=tot-i-j;
				int xx=a[0]*i+a[1]*j+a[2]*k;
				if(x)if(xx%x)continue;
				if(!x)if(xx)continue;
				int yy=b[0]*i+b[1]*j+b[2]*k;
				if(y)if(yy%y)continue;
				if(!y)if(yy)continue;
				int zz=c[0]*i+c[1]*j+c[2]*k;
				if(z)if(zz%z)continue;
				if(!z)if(zz)continue;
				int cnt=x?xx/x:y?yy/y:zz/z;
				if(cnt*x!=xx)continue;
				if(cnt*y!=yy)continue;
				if(cnt*z!=zz)continue;
				printf("%d %d %d %d\n",i,j,k,cnt);
				return 0;
			}
	puts("NONE");
	return 0;
}