/*
	ID:bobchenna1
	LANG:C++
	TASK:heritage
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
char s[27],t[27];
bool in[26];
int len;
void calc(int l1,int r1,int l2,int r2){
	//printf("%c %c %c %c\n",s[l1],s[r1],s[l2],s[r2]);
	if(l1>r1)return;
	if(l1==r1){putchar(s[l1]);}
	else{
		memset(in,0,sizeof(in));
		for(int i=l2;i<=r2;++i){
			if(t[i]==s[l1]){
				bool ok=0;
				for(int j=l1+1;j<=r1;++j){
					if(!in[s[j]-'A']){
						calc(l1+1,j-1,l2,i-1);
						calc(j,r1,i+1,r2);
						putchar(s[l1]);
						ok=1;
						break;
					}
				}
				if(!ok){
					calc(l1+1,r1,l2,r2-1);
					putchar(s[l1]);
				}
				break;
			}
			in[t[i]-'A']=1;
		}
	}
}
int main(){
	freopen("heritage.in","r",stdin);
	freopen("heritage.out","w",stdout);
	scanf("%s",t);
	scanf("%s",s);
	len=strlen(s);
	calc(0,len-1,0,len-1);
	puts("");
	return 0;
}