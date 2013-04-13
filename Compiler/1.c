typedef int cnx;

typedef int a;
a d;
struct a{ int *a; int d;} d;a c;
struct A {
    struct A *x, *y, *z;
};

struct B {
    struct B *x, *y, *z;
};

int main() {
	a c;
    struct A a;
    int t;
    void *ptr;
    a=*(struct A*)&t;
    a.x=(struct A*)malloc(sizeof (struct A));
    return 0;
}