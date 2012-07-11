/*
	ID:bobchenna1
	LANG:C++
	TASK:shopping
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

int f[7][7][7][7][7];
int p[101],need[101],ch[101][7][3];
int q[7][3],r[7];
int main(){
	freopen("shopping.in","r",stdin);
	freopen("shopping.out","w",stdout);
	memset(f,44,sizeof(f));
	f[0][0][0][0][0]=0;
	int n;
	scanf("%d",&n);
	for(int i=1;i<=n;++i){
		scanf("%d",need+i);
		for(int j=1;j<=need[i];++j)
			scanf("%d%d",ch[i][j]+0,ch[i][j]+1);
		scanf("%d",p+i);
	}
	int b;scanf("%d",&b);
	for(int i=1;i<=b;++i){
		scanf("%d%d%d",q[i]+0,q[i]+1,q[i]+2);
	}
	while(b<5){
		++b;
		q[b][0]=q[b][1]=q[b][2]=0;
	}
	for(int i=1;i<=n;++i){
		bool ok=1;
		memset(r,0,sizeof(r));
		for(int j=1;j<=need[i];++j){
			if(ch[i][j][0]==q[1][0])
				{r[1]=j;}
			else if(ch[i][j][0]==q[2][0]){r[2]=j;}
			else if(ch[i][j][0]==q[3][0]){r[3]=j;}
			else if(ch[i][j][0]==q[4][0]){r[4]=j;}
			else if(ch[i][j][0]==q[5][0]){r[5]=j;}
			else{ok=0;break;}
		}
		if(!ok)continue;
		for(int x1=0;x1+ch[i][r[1]][1]<=q[1][1];++x1)
			for(int x2=0;x2+ch[i][r[2]][1]<=q[2][1];++x2)
				for(int x3=0;x3+ch[i][r[3]][1]<=q[3][1];++x3)
					for(int x4=0;x4+ch[i][r[4]][1]<=q[4][1];++x4)
						for(int x5=0;x5+ch[i][r[5]][1]<=q[5][1];++x5)
							f[x1+ch[i][r[1]][1]][x2+ch[i][r[2]][1]][x3+ch[i][r[3]][1]][x4+ch[i][r[4]][1]][x5+ch[i][r[5]][1]]=min(
								f[x1+ch[i][r[1]][1]][x2+ch[i][r[2]][1]][x3+ch[i][r[3]][1]][x4+ch[i][r[4]][1]][x5+ch[i][r[5]][1]],f[x1][x2][x3][x4][x5]+p[i]);
	}
	int ans=(~0U>>1);
	for(int x1=0;x1<=q[1][1];++x1)
		for(int x2=0;x2<=q[2][1];++x2)
			for(int x3=0;x3<=q[3][1];++x3)
				for(int x4=0;x4<=q[4][1];++x4)
					for(int x5=0;x5<=q[5][1];++x5){
						int tmp=f[x1][x2][x3][x4][x5];
						tmp+=q[1][2]*(q[1][1]-x1);
						tmp+=q[2][2]*(q[2][1]-x2);
						tmp+=q[3][2]*(q[3][1]-x3);
						tmp+=q[4][2]*(q[4][1]-x4);
						tmp+=q[5][2]*(q[5][1]-x5);
						ans=min(ans,tmp);
					}
	printf("%d\n",ans);
	return 0;
}