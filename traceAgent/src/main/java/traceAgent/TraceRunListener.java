package traceAgent;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class TraceRunListener extends RunListener {

    private TraceManager manager;

    @Override
    public void testRunStarted(Description description) throws Exception
    {
        super.testRunStarted(description);
        manager = TraceManager.getInstance();
    }

    @Override
    public void testRunFinished(Result result) throws Exception
    {
        super.testRunFinished(result);
        manager.complete();
    }

    @Override
    public void testStarted(Description description) throws Exception
    {
        super.testStarted(description);
        String caseName = "[TEST] " + description.getClassName() + ":" + description.getMethodName();
        manager.addCase(caseName);
    }
}

