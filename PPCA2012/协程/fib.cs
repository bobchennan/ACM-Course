template <int n>
struct Fib
{
    // binary recursive call
    enum { value = Fib<n - 1>::value + Fib<n - 2>::value };
};

// termination condition
template<>
struct Fib<2>
{
    enum { value = 1 };
};

// termination condition
template <>
struct Fib<1>
{
    enum { value = 1 };
};