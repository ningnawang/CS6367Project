package agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
    //    System.out.println("agentArgs: agentArgs");
        SCClassFileTransformer trans = new SCClassFileTransformer(agentArgs);
        inst.addTransformer(trans);
        //Shutdown hooks are initialized but not-started threads. They start when JVM shutdown triggers.
        Runtime.getRuntime().addShutdownHook(new Thread() {
         	@Override
         	public void run() {
         		System.out.println(SCCollector.mCoverageMap);
         	}
        });
    }

    public static void main(String[] args) {}
}

