#include <stdio.h>

#define cB static int state=0;switch(state){case 0:
#define cR(x) do{state=__LINE__;return x;\
                   case __LINE__:;}while(0)
#define cF }

int f(void){
    static int i;
    cB;
    for(i=0;i<10;++i)
        cR(i);
    cF;
}

int main(void){
    for(int i=0;i<10;++i)
        printf("%d\n",f());
    return 0;
}