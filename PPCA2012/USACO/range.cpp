/*
	ID:bobchenna1
	LANG:C++
	TASK:range
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
int n,s[251][251];
int main(){
	freopen("range.in","r",stdin);
	freopen("range.out","w",stdout);
	scanf("%d",&n);
	for(int i=1;i<=n;++i)
		for(int j=1;j<=n;++j){
			char ch=' ';
			while(ch!='0'&&ch!='1')ch=getchar();
			s[i][j]=s[i-1][j]+s[i][j-1]-s[i-1][j-1]+(ch=='1');
		}
	for(int i=2;i<=n;++i){
		int cnt=0;
		for(int j=1;j+i-1<=n;++j)
			for(int k=1;k+i-1<=n;++k)
				if(s[j+i-1][k+i-1]-s[j+i-1][k-1]-s[j-1][k+i-1]+s[j-1][k-1]==i*i)
					++cnt;
		if(cnt)printf("%d %d\n",i,cnt);
	}
	return 0;
}