#include <stdio.h>

int fac(int n,int v){
	return n?fac(n-1,v*n):v;
}

int main(){
	printf("%d\n",fac(140000,1));
	return 0;
}