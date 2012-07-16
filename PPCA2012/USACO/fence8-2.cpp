/*
    ID:bobchenna1
    LANG:C++
    TASK:fence8
*/
#include <stdio.h> 
#include <algorithm> 
using std::sort; 
long wood[51]; 
long need[1025]; 
long n,m,i,j,k,l,ans,r,mid; 
long sum; 
long count; 
const long maxn=50000000; 
inline long max(const long x,const long y){ 
    return x<y?y:x; 
} 
bool dfs(const long pp,const long first){ 
    ++sum; 
    if(pp==-1) return 1; 
    bool ch=0; 
    if(sum==maxn) return 0; 
    for(long ii=need[pp]==need[pp+1]?first:0;ii<m;++ii) 
        if(need[pp]<=wood[ii]){ 
            wood[ii]-=need[pp],count-=need[pp]; 
            if(dfs(pp-1,ii)) ch=1; 
            wood[ii]+=need[pp],count-=need[pp]; 
            if(ch) return 1; 
            if(sum==maxn) return 0; 
            } 
    return 0; 
} 
int main(){ 
    freopen("fence8.in","r",stdin);
    freopen("fence8.out","w",stdout);
    for(scanf("%ld",&m),i=0;i<m;++i) scanf("%ld",&wood[i]),count+=wood[i]; 
    for(scanf("%ld",&n),i=0;i<n;++i) scanf("%ld",&need[i]); 
    sort(wood,wood+m); 
    sort(need,need+n); 
    l=-1,r=n; 
    while(sum=0,mid=(l+r)/2,l+1!=r){ 
        if(dfs(mid,0)) 
            l=mid; 
        else
            r=mid; 
        } 
    printf("%ld\n",l+1); 
    end: 
    scanf("%ld",&n); 
    return 0; 
}