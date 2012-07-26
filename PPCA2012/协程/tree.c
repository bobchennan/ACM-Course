#include <stdio.h>
#include "coroutine.h"
int t[100001][2];
int DFS(int u,ccrContParam){
	ccrBeginContext;
	int tmp;
	ccrContext z;
	ccrEndContext(foo);
	ccrBegin(foo);
	
	ccrReturn(u);
	
	if(t[u][0]){
		for(;;){
			foo->tmp=DFS(t[u][0],&(foo->z));
			if(foo->tmp==-1)break;
			else ccrReturn(foo->tmp); 
		}
	}
	
	if(t[u][1]){
		for(;;){
			foo->tmp=DFS(t[u][1],&(foo->z));
			if(foo->tmp==-1)break;
			else ccrReturn(foo->tmp);
		}
	}
	ccrFinish(-1);
}
int main(){
	int i,tmp;
	ccrContext z=0;
	for(i=1;i<=100001;++i){
		if(i*2<100001)t[i][0]=i*2;
		if(i*2+1<100001)t[i][1]=i*2+1;
	}
	for(;;){
		tmp=DFS(1,&z);
		if(tmp==-1)break;
		printf("%d\n",tmp);
	}
	return 0;
}