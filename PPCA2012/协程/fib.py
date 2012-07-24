def fib(max):
    a,b,c=0,1,0
    while c<max:
	yield a
	a,b,c=b,a+b,c+1

for n in fib(100000000):
    print n