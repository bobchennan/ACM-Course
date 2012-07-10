/*
	ID:bobchenna1
	LANG:C++
	TASK:fracdec
*/
#include<cstdio>
#include<cstring>
#include<map>
using namespace std;
long long gcd(long long x,long long y)
{
    if (x==0) return y;
    return gcd(y%x,x);
}
map<long long,int> k;
long long n,d,dat[1000001];
int leng(long long v){
	if(!v)return 1;
	int ret;
	for(ret=0;v;++ret)
		v/=10;
	return ret;
}
int main()
{
	freopen("fracdec.in","r",stdin);
	freopen("fracdec.out","w",stdout);
    scanf("%lld%lld",&n,&d);
    long long v=n/d;
    printf("%lld.",v);
    n%=d;
    k.clear();
    long long tmp=d/gcd(n,d);
    while (tmp%2==0) tmp/=2;
    while (tmp%5==0) tmp/=5;
 
    if (tmp==1)
    {
               for (;;)
               {
                   n*=10;
                   printf("%lld",n/d);
                   n%=d;
                   if (n==0) break;
               }
               puts("");
               return 0;
    }
 	int len=leng(v)+2;
    for (int i=1;;i++)
    {
        if (k[n]!=0)
        {
                    for (int j=1;j<=k[n]-1;j++){
                        printf("%lld",dat[j]);
                        ++len;
                        if(len%76==0)puts("");
                    }
                    putchar('(');
                    for (int j=k[n];j<=i-1;j++){
                        printf("%lld",dat[j]);
                        ++len;
                        if(len%76==0)puts("");
                    }
                    puts(")");
                    break;
        }
        k[n]=i;
        n*=10;
        dat[i]=n/d;
        n%=d;
    }
    return 0;
}