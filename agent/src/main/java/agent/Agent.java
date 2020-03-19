package agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        SCClassFileTransformer trans = new SCClassFileTransformer(agentArgs);
        inst.addTransformer(trans);
        ////////////////////////////////////////////////////////////////////////
        // ningna.wang:                                                       //
        // this is just for debugging purpose                                 //
        // to check if our agent is working properly                          //
        // enable this might cause RunListener not working properly sometimes //
        ////////////////////////////////////////////////////////////////////////
        //Shutdown hooks are initialized but not-started threads. They start when JVM shutdown triggers.
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//         	@Override
//         	public void run() {
//         		System.out.println(SCCollector.mCoverageMap);
//         	}
//        });
    }

    public static void main(String[] args) {}
}

