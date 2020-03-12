import java.lang.instrument.Instrumentation;
import java.lang.System.*;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("before trans creation");
        SCClassFileTransformer trans = new SCClassFileTransformer(agentArgs);
        System.out.println("after trans creation");
        inst.addTransformer(trans);
        System.out.println("before trans print");
        trans.printCoverageMap();
        // Runtime.getRuntime().addShutdownHook(new Thread() {
        // 	@Override
        // 	public void run() {
        // 		SCClassFileTransformer.printCoverageMap();
        // 	}
        // });
    }

    public static void main(String[] args) {}
}
