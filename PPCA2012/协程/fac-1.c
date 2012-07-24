#include <stdio.h>
int fac(int n){
	return n?n*fac(n-1):1;
}
int main(void){
	printf("%d\n",fac(200000));
	return 0;
}