package agent;

/**
 * only used to generate ASM code
 *
 * $ javac -cp asm-all-6.0_BETA.jar ASMified.java SCCollector.java
 * $ java -cp .:asm-all-6.0_BETA.jar org.objectweb.asm.util.ASMifier ASMified
 */
public class ASMified {
    public static void main(String args[]) {
        SCCollector.visitLineStatic("aaaaa", 9999);
    }
}