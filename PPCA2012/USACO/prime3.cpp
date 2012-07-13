/*
	ID:bobchenna1
	LANG:C++
	TASK:prime3
*/
#include <set>
#include <map>
#include <cmath>
#include <queue>
#include <cstdio>
#include <vector>
#include <string>
#include <climits>
#include <cstring>
#include <algorithm>
using namespace std;
bool np[100003];
int sum;
int a[5][5];
bool ans=0;
/*
0 20 22 23  8   
13  1 17  7 15
9  10  2 11 12
14  6 18  3 16 
5 19 21 24  4
 */
void DFS(int x,int y){
	if(x==3&&y==4){
		if(a[3][0]+a[3][1]+a[3][2]+a[3][3]+a[3][4]!=sum)return;
		if(np[a[3][0]*10000+a[3][1]*1000+a[3][2]*100+a[3][3]*10+a[3][4]])return;
		if(ans)puts("");
		ans=1;
		for(int i=0;i<5;++i)
			for(int j=0;j<5;++j){
				printf("%d",a[i][j]);
				if(j==4)puts("");
			}
		return;
	}
	int tmp=a[x][y];
	int l=y?(x?0:1):1;
	int r=10;
	if(x==3)
	if(y!=1&&y!=3){
		a[x][y]=sum-a[0][y]-a[1][y]-a[2][y]-a[4][y];
		if(y==0&&a[x][y]==0){
			a[x][y]=tmp;
			return;
		}
		l=a[x][y];
		r=a[x][y]+1;
	}
	else{
		if(a[0][y]+a[1][y]+a[2][y]+a[3][y]+a[4][y]!=sum)return;
	}
	if(a[x][y]!=-1)l=a[x][y],r=a[x][y]+1;
	for(int i=l;i<r&&i>=0&&i<10;++i){
		a[x][y]=i;
		if(x==3)
			if(np[a[0][y]*10000+a[1][y]*1000+a[2][y]*100+a[3][y]*10+a[4][y]])return;
		if(y==4){
			if(np[a[x][0]*10000+a[x][1]*1000+a[x][2]*100+a[x][3]*10+a[x][4]])continue;
			if(a[x][0]+a[x][1]+a[x][2]+a[x][3]+a[x][4]!=sum)continue;
			DFS(x+1,0);
		}
		else DFS(x,y+1);
	}
	a[x][y]=tmp;
}
int main(){
	freopen("prime3.in","r",stdin);
	freopen("prime3.out","w",stdout);
	memset(a,-1,sizeof(a));
	scanf("%d%d",&sum,a[0]+0);
	np[0]=np[1]=1;
	for(int i=2;i<=1000;++i)
		if(!np[i])
			for(int j=i*i;j<100003;j+=i)
				np[j]=1;
	if(sum%3==0)puts("NONE");
	else{
	for(a[1][1]=0;a[1][1]<=min(sum-a[0][0],9);++a[1][1])
		for(a[2][2]=0;a[2][2]<=min(sum-a[1][1]-a[0][0],9);++a[2][2])
			for(a[4][4]=1;a[4][4]<=min(sum-a[0][0]-a[1][1]-a[2][2],9);a[4][4]+=2){
				a[3][3]=sum-a[0][0]-a[1][1]-a[2][2]-a[4][4];
				if(a[3][3]>9||np[a[0][0]*10000+a[1][1]*1000+a[2][2]*100+a[3][3]*10+a[4][4]])continue;
				for(a[4][0]=1;a[4][0]<=min(sum-a[0][0],9)&&a[4][0]<=sum-a[2][2];a[4][0]+=2)
					for(a[3][1]=0;a[3][1]<=min(sum-a[1][1],9)&&a[3][1]<=sum-a[4][0]-a[2][2];++a[3][1])
						for(a[0][4]=1;a[0][4]<=9&&a[0][4]<=sum-a[4][4]&&a[0][4]<=sum-a[4][0]-a[3][1]-a[2][2];++a[0][4]){
							a[1][3]=sum-a[4][0]-a[3][1]-a[2][2]-a[0][4];
							if(a[1][3]>9||np[a[4][0]*10000+a[3][1]*1000+a[2][2]*100+a[1][3]*10+a[0][4]])continue;
							for(a[4][1]=1;a[4][1]<=9&&a[4][1]<=sum-a[4][0]-a[4][4]&&a[4][1]<=sum-a[1][1]-a[3][1];a[4][1]+=2)
								for(a[4][2]=1;a[4][2]<=9&&a[4][2]<=sum-a[4][0]-a[4][1]-a[4][4]&&a[4][2]<=sum-a[2][2];a[4][2]+=2){
									a[4][3]=sum-a[4][0]-a[4][1]-a[4][2]-a[4][4];
									if(a[4][3]>9||np[a[4][0]*10000+a[4][1]*1000+a[4][2]*100+a[4][3]*10+a[4][4]])continue;
									for(a[1][4]=1;a[1][4]<=9&&a[1][4]<=sum-a[1][1]-a[1][3]&&a[1][4]<=sum-a[0][4]-a[4][4];a[1][4]+=2)
										for(a[2][4]=1;a[2][4]<=9&&a[2][4]<=sum-a[2][2]&&a[2][4]<=sum-a[0][4]-a[1][4]-a[4][4];a[2][4]+=2){
											a[3][4]=sum-a[0][4]-a[1][4]-a[2][4]-a[4][4];
											if(a[3][4]>9||np[a[0][4]*10000+a[1][4]*1000+a[2][4]*100+a[3][4]*10+a[4][4]])continue;
											DFS(0,1);
										}
								}
							}
						}
	if(!ans)puts("NONE");
	}
	return 0;
}