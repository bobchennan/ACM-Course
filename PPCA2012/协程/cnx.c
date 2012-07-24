#include <stdio.h>
#include "coroutine.h"

int ascending (ccrContParam) {
   ccrBeginContext;
   int i;
   ccrEndContext(foo);

   if(!foo) {foo= *ccrParam=malloc(sizeof(*foo)); foo->ccrLine=0;}\
                         if (foo) switch(foo->ccrLine) { case 0:;
   for (foo->i=0; foo->i<10; foo->i++)
       do {((struct ccrContextTag *)*ccrParam)->ccrLine=__LINE__;return (foo->i); case __LINE__:;} while (0);
   } free(*ccrParam); *ccrParam=0; return (-1);
}

int main(void) {
   ccrContext z = 0;
   do {
      printf("got number %d\n", ascending (&z));
   } while (z);
}