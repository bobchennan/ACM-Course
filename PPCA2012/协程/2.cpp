#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

int fac(int n){
	return n*fac(n+1);
}

int main(){
	fac(1);
	return 0;
}