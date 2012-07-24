#include <cstdio>
#include <iostream>
#include <algorithm>
using namespace std;

#define trail_begin(x,y) static bool cnx=0;\
						 for(;;){try{if(cnx){throw(make_pair(n,v));}\
						 else cnx=1

#define trail_end(x,y) }catch(pair<int,int> d){if(cnx){cnx=0;throw d;}else n=d.first,v=d.second;};}

int fac(int n,int v){
	//printf("%d\n",n);
	trail_begin(n,v);
	return n==1?v:fac(n-1,v*n);
	trail_end(n,v);
}

int main(){
	//test1();
	printf("%d\n",fac(200000,1));
	return 0;
}