/*
	ID:bobchenna1
	LANG:C++
	TASK:kimbits
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
long long C[33][33];
int main(){
	for(int i=0;i<=31;++i)
		C[i][0]=1;
	for(int i=1;i<=31;++i)
		for(int j=1;j<=i;++j)
			C[i][j]=C[i-1][j-1]+C[i-1][j];
	for(int i=0;i<=31;++i)
		for(int j=0;j<=31;++j)
			C[i][j]+=C[i][j-1];
	freopen("kimbits.in","r",stdin);
	freopen("kimbits.out","w",stdout);
	int N,L;
	long long I;
	scanf("%d%d%lld",&N,&L,&I);
	for(;N;N--){
		if(I>C[N-1][L]){putchar('1');I-=C[N-1][L];--L;}
		else putchar('0');
	}
	puts("");
	return 0;
}