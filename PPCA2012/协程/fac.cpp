#include <iostream>
using namespace std;

template <int No>
struct Factorial
{
    // linear recursive call
    enum { value = No + Factorial<No - 1>::value };
};

// termination condition
template <>
struct Factorial<0>
{
    enum { value = 1 };
};

int main(){
    cout<<Factorial<1024>::value<<endl;
    return 0;
}