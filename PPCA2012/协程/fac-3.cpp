#include <cstdio>
#include <iostream>
using namespace std;

template <int No, int a>
struct Factorial
{
    // tail recursive call
    enum { value = Factorial<No - 1, No * a>::value };
};

// termination condition
template <int a>
struct Factorial<0, a>
{
    enum { value = a };
};

int main(){
	printf("%d\n",Factorial<300000,0>::value);
	return 0;
}