package test;

public class Stuff {
    private int z;
    public void print(int x) {
        System.out.println(x);
	x++;
	foo(1);
	foo(2);
    }
    private void foo( int y)
    {
	z = 2;
	int q = y;
	return;
    }
}
