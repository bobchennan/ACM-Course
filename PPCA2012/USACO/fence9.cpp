/*
	ID:bobchenna1
	LANG:C++
	TASK:fence9
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
	freopen("fence9.in","r",stdin);
	freopen("fence9.out","w",stdout);
	int n,m,p;
	scanf("%d%d%d",&n,&m,&p);
	printf("%d\n",p*m/2-(p+__gcd(n,m)+__gcd(m,abs(p-n)))/2+1);
	return 0;
}