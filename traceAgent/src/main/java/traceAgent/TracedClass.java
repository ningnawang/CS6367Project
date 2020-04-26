package traceAgent;

import java.util.Vector;

public class TracedClass
{
    private String name; //name of the traced classes
    private Vector<TracedMethod> methods; //name of the methods under classes
    private Vector<TracedVar> fieldVars; //name of the parameters passed to the methods
    
    public TracedClass()
    {
	name = "Unknown";
	methods = new Vector<TracedMethod>();
	fieldVars = new Vector<TracedVar>();
    }
    
    public TracedClass(String n)
    {
	name = n;
	methods = new Vector<TracedMethod>();
	fieldVars = new Vector<TracedVar>();
    }

    public String getName()
    {
	return name;
    }

    public void addParam(String mN, String var, String val, String type)
    {
	boolean found = false;
	for (TracedMethod m : methods)
	    if (mN.equals(m.getName()))
		{
		    found = true;
		    m.addParam(var, val, type);
		    break;
		}
	if (!found)
	    {
		TracedMethod m = new TracedMethod(mN);
		m.addParam(var, val, type);
		methods.add(m);
	    }
    }

    public void addField(String var, String val, String type)
    {
	boolean found = false;
	for (TracedVar v : fieldVars)
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
		fieldVars.add(v);
	    }
    }

    public String stringify(int tabs)
    {
	String t = "";
	/*for (int i = 0; i < tabs; ++i)
	    t += "\t";
	*/
	String output = "";
	output += t + "Class: ";
	//output += t + "\tname: " + name + "\n";
        output += t + "class_name: " + name + "\n";
	//output += t + "\tfieldVars: \n";
        output += t + "field_variables: \n";
	for (int i = 0; i < fieldVars.size(); ++i)
	    //output += fieldVars.get(i).stringify(tabs + 2);
            output += fieldVars.get(i).stringify(0);
	//output += t + "\tmethods: \n";
        output += t + "methods: \n";
	for (int i = 0; i < methods.size(); ++i)
	    //output += methods.get(i).stringify(tabs + 2);
            output += methods.get(i).stringify(0);
        return output;
    }
}

