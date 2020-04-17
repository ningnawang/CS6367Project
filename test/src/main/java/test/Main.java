package test;

public class Main {

    public static void main(String[] args) {
	int val = 0;
	while (args.length > val)
	    val++;
        Stuff stuff = new Stuff();
        stuff.print(val);
    }

}
