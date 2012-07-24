def travel(max):
    i=0
    while i<max:
        yield i
        i=i+1

for n in travel(100):
    print n