/*
	ID:bobchenna1
	LANG:C++
	TASK:prime3
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
bool np[100003];
int sum;
int a[5][5];
/*
0 20 22 23  8   
13  1 17  7 15
9  10  2 11 12
14  6 18  3 16 
5 19 21 24  4
 */
int main(){
	freopen("prime3.in","r",stdin);
	freopen("prime3.out","w",stdout);
	scanf("%d%d",&sum,a[0]+0);
	np[0]=np[1]=1;
	for(int i=2;i<=1000;++i)
		if(!np[i])
			for(int j=i*i;j<100003;j+=i)
				np[j]=1;
	for(a[1][1]=0;a[1][1]<=min(sum-a[0][0],9);++a[1][1])
		for(a[2][2]=0;a[2][2]<=min(sum-a[1][1]-a[0][0],9);++a[2][2])
			for(a[3][3]=0;a[3][3]<=min(sum-a[0][0]-a[1][1]-a[2][2],9);++a[3][3])
				for(a[4][4]=1;a[4][4]<=min(sum-a[0][0]-a[1][1]-a[2][2]-a[3][3],9);a[4][4]+=2)
					if(!np[a[0][0]*10000+a[1][1]*1000+a[2][2]*100+a[3][3]*10+a[4][4]])
						for(a[4][0]=1;a[4][0]<=min(sum-a[0][0],9)&&a[4][0]<=sum-a[2][2];a[4][0]+=2)
							for(a[3][1]=0;a[3][1]<=min(sum-a[1][1],9)&&a[3][1]<=sum-a[4][0]-a[2][2];++a[3][1])
								for(a[1][3]=0;a[1][3]<=9&&a[1][3]<=sum-a[3][3]&&a[1][3]<=sum-a[4][0]-a[3][1]-a[2][2];++a[1][3])
									for(a[0][4]=1;a[0][4]<=9&&a[0][4]<=sum-a[4][4]&&a[0][4]<=sum-a[4][0]-a[3][1]-a[2][2]-a[1][3];++a[0][4])
										if(!np[a[4][0]*10000+a[3][1]*1000+a[2][2]*100+a[1][3]*10+a[0][4]])
											DFS(0,1);
	return 0;
}