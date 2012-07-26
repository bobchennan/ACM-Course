public static int FactorialContinuation(int n, 
	Func<int, int> continuation)
{
    if (n == 0) return continuation(1);
    return FactorialContinuation(n - 1,
        r => continuation(n * r));
}
FactorialContinuation(10, x => x)