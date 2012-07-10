/*
	ID:bobchenna1
	LANG:C++
	TASK:comehome
*/
#include <map>
#include <cmath>
#include <queue>
#include <cstdio>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

map<char,vector< pair<char,int> > > h;
map<char,int> d;
map<char,bool> inQ;
queue<char> Q;

int main(){
	FILE *in=fopen("comehome.in","r");
	FILE *out=fopen("comehome.out","w");
	int n;
	fscanf(in,"%d%*c",&n);
	for(int i=0;i<n;++i){
		char c1,c2;
		int v;
		fscanf(in,"%c %c %d%*c",&c1,&c2,&v);
		h[c1].push_back(make_pair(c2,v));
		h[c2].push_back(make_pair(c1,v));
		d[c1]=d[c2]=(~0U>>1);
	}
	d['Z']=0;
	Q.push('Z');
	inQ['Z']=1;
	while(!Q.empty()){
		char x=Q.front();Q.pop();
		inQ[x]=0;
		for(int i=0;i<h[x].size();++i){
			char y=h[x][i].first;
			int v=h[x][i].second;
			if(d[x]+v<d[y]){
				d[y]=d[x]+v;
				if(!inQ[y]){
					Q.push(y);
					inQ[y]=1;
				}
			}
		}
	}
	char ch=' ';
	int ans=(~0U>>1);
	for(map<char,int>::iterator it=d.begin();it!=d.end();++it)
		if(it->first>='A'&&it->first<'Z')
			if(ch==' '||it->second<d[ch])
				ans=it->second,ch=it->first;
	fprintf(out,"%c %d\n",ch,ans);
	return 0;
}