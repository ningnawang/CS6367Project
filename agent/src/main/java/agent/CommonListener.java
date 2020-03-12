package agent;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.SortedSet;

public class CommonListener extends RunListener {
    public void testRunStarted(Description description) throws Exception {
        System.out.println("Test Run Started\n");
        SCCollector.coverageMap = new HashMap<>();
    }

    public void testRunFinished(Result result) throws Exception {
        System.out.println("Test Run Finished\n");
        try {
            FileWriter fw = new FileWriter("stmt-cov.txt",true);
            StringBuffer sb = new StringBuffer();
            System.out.println("=================>" + SCCollector.coverageMap);
            for(String packageName : SCCollector.coverageMap.keySet()) {
                sb.append(packageName + "\n");
                Map<String, SortedSet<Integer>> mCoverageMap = SCCollector.coverageMap.get(packageName);

                for(String className : mCoverageMap.keySet()){
                    for(Integer i : mCoverageMap.get(className)){
                        sb.append(className + ":" + i + "\n");
                    }
                }
            }
            fw.write(sb.toString());
            fw.close();
        } catch (IOException ex) {
            System.err.println("Couldn't log this");
        }
    }

    public void testStarted(Description description) throws Exception {
        SCCollector.packageName = "[TEST] " + description.getClassName() + ":" + description.getMethodName();
        SCCollector.mCoverageMap = new HashMap<>();
        System.out.println(SCCollector.packageName);
    }
    
    public void testFinished(Description description) throws Exception {
        SCCollector.coverageMap.put(SCCollector.packageName, SCCollector.mCoverageMap);
        System.out.println(SCCollector.packageName + " Finished\n");
    }



}