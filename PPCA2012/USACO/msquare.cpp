/*
	ID:bobchenna1
	LANG:C++
	TASK:msquare
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

queue< vector<int> > Q;
map< vector<int> ,string> f;

int main(){
	freopen("msquare.in","r",stdin);
	freopen("msquare.out","w",stdout);
	vector<int> md,cs;
	for(int i=0;i<8;++i){
		int x;
		scanf("%d",&x);
		md.push_back(x);
		cs.push_back(i+1);
	}
	Q.push(cs);f[cs]="";
	while(!Q.empty()){
		vector<int> now=Q.front();Q.pop();
		if(now==md){
			printf("%d\n",f[now].size());
			printf("%s\n",f[now].c_str());
			break;
		}
		//A
		vector<int> tmp=now;
		for(int i=0;i<4;++i)
			swap(tmp[i],tmp[7-i]);
		if(f.find(tmp)==f.end()){
			f[tmp]=f[now]+'A';
			Q.push(tmp);
		}
		//B
		tmp=now;
		for(int i=0;i<4;++i){
			tmp[i]=now[(i+3)&3];
			tmp[i+4]=now[((i+1)&3)+4];
		}
		if(f.find(tmp)==f.end()){
			f[tmp]=f[now]+'B';
			Q.push(tmp);
		}
		//C
		tmp[0]=now[0];tmp[1]=now[6];tmp[2]=now[1];tmp[3]=now[3];
		tmp[7]=now[7];tmp[6]=now[5];tmp[5]=now[2];tmp[4]=now[4];
		if(f.find(tmp)==f.end()){
			f[tmp]=f[now]+'C';
			Q.push(tmp);
		}
	}
	return 0;
}