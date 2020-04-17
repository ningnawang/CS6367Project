package test;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest  {   
    @Test
    public void process() {
        Stuff s = new Stuff();
	s.print(5);
    }

    @Test
    public void compute() {
        Stuff s = new Stuff();
	s.print(4);
    }
}
