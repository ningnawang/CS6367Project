package invariantsAgent;

import java.util.Vector;

public class TracedMethod {
    private String name;
    private Vector<TracedVar> params;
    
    public TracedMethod()
    {
	name = "Unknown";
        params = new Vector<TracedVar>();
    }
    
    public TracedMethod(String n)
    {
	name = n;
        params = new Vector<TracedVar>();
    }

    public String getName()
    {
	return name;
    }

    public void addParam(String var, String val, String type)
    {
	boolean found = false;
	for (TracedVar v : params)
	    if (v.is(var, type))
		{
		    found = true;
		    v.addValue(val);
		    break;
		}
	if (!found)
	    {
		TracedVar v = new TracedVar(var, type);
		v.addValue(val);
		params.add(v);
	    }
    }

    public String stringify(int tabs)
    {
	String t = "";
	/*for (int i = 0; i < tabs; ++i){
	    //t += "\t";
        }*/
	
	String output = "";
	output += t + "method_name: " + name + "\n";
	output += t + "params: \n";
	for (int i = 0; i < params.size(); ++i)
	    output += params.get(i).stringify(tabs + 1); 
        return output;
    }
}
