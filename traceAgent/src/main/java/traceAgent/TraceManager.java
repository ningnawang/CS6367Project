package traceAgent;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraceManager {
    private static volatile TraceManager instance = null;

    private final Vector<TracedClass> tracedClasses;
    private String progName;
    private String curTest;
    private ExecutorService executor;
    private TraceManager()
    {
        tracedClasses = new Vector<TracedClass>();
        this.executor = Executors.newFixedThreadPool(8);
    }

    public static TraceManager getInstance()
    {
        if (instance == null)
	    synchronized(TraceManager.class) //avoid instance being changed between
		{                            //threads
		    if (instance == null)
			instance = new TraceManager();   
		}
        return instance;
    }

    public void insertTrace(String className, String methodName, String var, String val, String type, boolean isParam)
    {
	boolean found = false;
	for (TracedClass c : tracedClasses)
	    if (className.equals(c.getName()))
		{
		    found = true;
		    if (isParam)
			c.addParam(methodName, var, val, type);
		    else
			c.addField(var, val, type);
		    break;
		}
	if (!found)
	    {
		TracedClass c = new TracedClass(className);
		if (isParam)
		    c.addParam(methodName, var, val, type);
		else
		    c.addField(var, val, type);
		tracedClasses.add(c);
	    }
    }

    public static void callInsert(String className, String methodName, String var, String val, String type, int isParam)
    {
        TraceManager.getInstance().insertTrace(className, methodName, var, val, type, isParam != 0);
    }

    public void setProgName(String n)
    {
        progName = n;
    }

    public String getProgName()
    {
        return progName;
    }

    public void addCase(String name)
    {
        curTest = name;
    }

    public void complete()
    {
	System.out.println("Finished run:");
        synchronized(tracedClasses)
	    {
		outputTraces(tracedClasses);
	    }    
    }

    private void outputTraces(Vector<TracedClass> tracedClasses)
    {
        String logDir = "logs";
        String logPath = logDir + File.separator + "trace" + java.time.Clock.systemUTC().instant() + ".dat";
        try
	    {
		File dir = new File(logDir);
		if (! dir.exists())
		    dir.mkdir();
		File file = new File(logPath);
		if (!file.exists())
		    file.createNewFile();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < tracedClasses.size(); i++) 
                if (tracedClasses.get(i) != null)
                    sb.append(tracedClasses.get(i).stringify(0));
            bw.write(sb.toString());
            bw.close();
        }
	catch (IOException e)
	    {
		e.printStackTrace();
	    }
    }
}
