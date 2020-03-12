package agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        SCClassFileTransformer trans = new SCClassFileTransformer(agentArgs);
        inst.addTransformer(trans);
        //Shutdown hooks are initialized but not-started threads. They start when JVM shutdown triggers.
        Runtime.getRuntime().addShutdownHook(new Thread() {
         	@Override
         	public void run() {
         		SCClassFileTransformer.printCoverageMap();
         	}
        });
    }

    public static void main(String[] args) {}
}

