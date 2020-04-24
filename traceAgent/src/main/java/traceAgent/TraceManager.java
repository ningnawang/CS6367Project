package traceAgent;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraceManager {
    private static volatile TraceManager instance = null;

    private final List<TraceEntry> entries;
    private String progName;
    private String curTest;
    private ExecutorService executor;
    private final Queue<List<TraceEntry>> queue;
    private int total;

    private TraceManager()
    {
        entries = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(8);
        queue = new LinkedList<>();
        total = 0;
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

    public void addDatum(String className, String methodName, String token, String var, String val, String type, boolean isField, boolean isDerived, int hashcode)
    {
        synchronized (entries)
	    {
		if (entries.size() > 1000)
		    {
			total += entries.size();
			queue.add(new ArrayList<>(entries));
			entries.clear();
			schedule();
		    }
	    }

        TraceEntry entry = new TraceEntry()
	    .withClassName(className == null ? "null" : className)
	    .withMethodName(methodName == null ? "null" : methodName)
	    .withToken(token == null ? "null" : token)
	    .withTestCase(curTest == null ? "null" : curTest)
	    .withDerived(isDerived)
	    .withHashcode(hashcode)
	    .withParameter(!isField)
	    .withVarName(var == null ? "null" : var)
	    .withVarValue(val == null ? "null" : val)
	    .withVarType(type == null ? "null" : type);
        entries.add(entry);
    }

    public static void newDatum(String className, String methodName, String token, String var, String val, String type, int isField, int isDerived, int hashcode)
    {
        TraceManager.getInstance().addDatum(className, methodName,
					    token, var, val, type, isField != 0,
					    isDerived != 0, hashcode);
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
        synchronized (entries)
	    {
		total += entries.size(); //entries = list of TraceEntries
		queue.add(new ArrayList<>(entries));
		entries.clear();
	    }

        flush();
        System.out.println("Total traced entries: " + total);
    }

    private void schedule()
    {
        executor.execute(this::flush);
    }

    private void flush()
    {
        while (true)
	    synchronized (queue)
		{
		    if (queue.isEmpty())
			break;
		    List<TraceEntry> entries = queue.poll();
		    if (entries == null || entries.size() == 0)
			continue;
		    writeToFile(entries);
		}
    }

    private void writeToFile(List<TraceEntry> entries)
    {
        String logDir = "logs";
        String logPath = logDir + File.separator + "trace" + UUID.randomUUID() + ".dat";
        try
	    {
		File dir = new File(logDir);
		if (! dir.exists())
		    dir.mkdir();
		File file = new File(logPath);
		if (!file.exists())
		    file.createNewFile();

            ObjectMapper objectMapper = new ObjectMapper();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            StringBuilder sb = new StringBuilder();
            int i;
            for (i = 0; i < entries.size() - 1; i++) 
                if (entries.get(i) != null)
                    sb.append(entries.get(i).toString());
            bw.write(sb.toString());
            bw.close();
        }
	catch (IOException e)
	    {
		e.printStackTrace();
	    }
    }
}
