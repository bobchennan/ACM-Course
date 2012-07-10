/*
	ID:bobchenna1
	LANG:C++
	TASK:zerosum
*/
#include <cstdio>
#include <string>
#include <algorithm>
using namespace std;
int n;
char ch[]={' ','+','-'};
string s;
FILE *out;
int calc(){
	int i;
	int ret=0;
	for(i=0;s[i]!='+'&&s[i]!='-'&&i<s.size();++i)
		if(s[i]>='0'&&s[i]<='9')
			ret=ret*10+s[i]-'0';
	if(i==s.size())return ret;
	for(;;){
		char ch=s[i];
		int tmp=0;
		for(++i;s[i]!='+'&&s[i]!='-'&&i<s.size();++i)
			if(s[i]>='0'&&s[i]<='9')
				tmp=tmp*10+s[i]-'0';
		if(ch=='+')ret+=tmp;
		else ret-=tmp;
		if(i==s.size())return ret;
	}
}
void check(){
	if(calc()==0)
		fprintf(out,"%s\n",s.c_str());
}
void search(int u){
	if(u>n){
		check();
		return;
	}
	for(int i=0;i<3;++i){
		s+=ch[i];
		s+='0'+u;
		search(u+1);
		s.erase(--s.end());
		s.erase(--s.end());
	}
}
int main(){
	FILE *in=fopen("zerosum.in","r");
	out=fopen("zerosum.out","w");
	fscanf(in,"%d",&n);
	s="1";
	search(2);
	return 0;
}