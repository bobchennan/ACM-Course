/*
	ID:bobchenna1
	LANG:C++
	TASK:cryptcow
*/
#include <set>
#include <map>
#include <cmath>
#include <queue>
#include <cstdio>
#include <vector>
#include <string>
#include <climits>
#include <cstring>
#include <algorithm>
using namespace std;

char goal[]="Begin the Escape execution at the Break of Dawn";
char s[77],t1[77],t2[77],g[11][77];
int max_dep,h1[11][77],h2[11][77],h3[11][77],len;
int ok[3001][53];

int next(int u,char v){
	if(v>='a'&&v<='z')return v-'a';
	else if(v>='A'&&v<='Z')return v-'A'+26;
	else return 53;
}

bool search(int u){
	if(u==max_dep){
		for(int i=0;i<47;++i)
			if(s[i]!=goal[i])
				return 0;
		return 1;
	}

	int cnt=0,cnt2=0,*r1=h1[u],*r2=h2[u],*r3=h3[u];
	char *bak=g[u];
	r1[0]=r2[0]=r3[0]=0;
	for(int i=0,p=1;i<len;++i){
		if(s[i]=='C'){
			++cnt;
			r1[++r1[0]]=i;
			p=1;
		}
		else if(s[i]=='W'){
			--cnt;++cnt2;
			r3[++r3[0]]=i;
			p=1;
		}
		else if(s[i]=='O'){
			r2[++r2[0]]=i;
			p=1;
		}
		else{
			if(cnt==0&&(cnt2==0||cnt2==max_dep-u))
				if(s[i]!=goal[i-3*cnt2])return 0;
			p=ok[p][next(p,s[i])];
			if(p==0)
				return 0;
		}
	}
	if(r1[0]!=r2[0]||r2[0]!=r3[0])return 0;
	if(!(r1[1]<r2[1]&&r1[1]<r3[1]))return 0;
	if(!(r3[r3[0]]>r2[r2[0]]&&r3[r3[0]]>r1[r1[0]]))return 0;
	memcpy(bak,s,sizeof(s));
	if((r1[0]==1||r1[2]>r3[1])&&(r2[0]==1||r2[2]>r3[1])){
				int i=1,j=1,k=1;
				if(!(r1[j]<r2[i]&&r2[i]<r3[k]))return 0;
				if(r1[j]+1==r2[i]&&r2[i]+1==r3[k]){
					for(int l=r1[j];l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				else if(r1[j]+1==r2[i]){
					for(int l=r1[j];l<r3[k]-2;++l)s[l]=s[l+2];
					for(int l=r3[k]-2;l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				else if(r2[i]+1==r3[k]){
					for(int l=r1[j];l<r2[i]-1;++l)s[l]=s[l+1];
					for(int l=r2[i]-1;l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				else{
					for(int l=r1[j]+1;l<r2[i];++l)t2[l-r1[j]-1]=s[l];
					for(int l=r2[i]+1;l<r3[k];++l)t1[l-r2[i]-1]=s[l];
					for(int l=r1[j];l<r1[j]+r3[k]-r2[i]-1;++l)s[l]=t1[l-r1[j]];
					for(int l=r1[j]+r3[k]-r2[i]-1;l<r3[k]-2;++l)s[l]=t2[l-r1[j]-r3[k]+r2[i]+1];
					for(int l=r3[k]-2;l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				len+=3;
				memcpy(s,bak,sizeof(s));
	}
	else
	for(int i=1;i<=r2[0];++i)
		for(int j=1;r1[j]<r2[i]&&j<=r1[0];++j)
			for(int k=r3[0];r3[k]>r2[i]&&k;--k){
				if(r1[j]+1==r2[i]&&r2[i]+1==r3[k]){
					for(int l=r1[j];l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				else if(r1[j]+1==r2[i]){
					for(int l=r1[j];l<r3[k]-2;++l)s[l]=s[l+2];
					for(int l=r3[k]-2;l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				else if(r2[i]+1==r3[k]){
					for(int l=r1[j];l<r2[i]-1;++l)s[l]=s[l+1];
					for(int l=r2[i]-1;l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				else{
					for(int l=r1[j]+1;l<r2[i];++l)t2[l-r1[j]-1]=s[l];
					for(int l=r2[i]+1;l<r3[k];++l)t1[l-r2[i]-1]=s[l];
					for(int l=r1[j];l<r1[j]+r3[k]-r2[i]-1;++l)s[l]=t1[l-r1[j]];
					for(int l=r1[j]+r3[k]-r2[i]-1;l<r3[k]-2;++l)s[l]=t2[l-r1[j]-r3[k]+r2[i]+1];
					for(int l=r3[k]-2;l<len-3;++l)s[l]=s[l+3];
					len-=3;
					if(search(u+1))return 1;
				}
				len+=3;
				memcpy(s,bak,sizeof(s));
			}
	return 0;
}

int main(){
	len=0;
	for(int i=0;i<47;++i){
		int p=1;
		for(int j=i;j<47;++j){
			int tmp=next(p,goal[j]);
			if(!ok[p][tmp]){
				++len;
				ok[p][tmp]=len;
				p=len;
			}
			else p=ok[p][tmp];
		}
	}

	freopen("cryptcow.in","r",stdin);
	freopen("cryptcow.out","w",stdout);
	gets(s);
	len=strlen(s);

	if((len-47)%3)puts("0 0");
	else{
		max_dep=(len-47)/3;
		if(search(0))printf("1 %d\n",max_dep);
		else puts("0 0");
	}
	return 0;
}