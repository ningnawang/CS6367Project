package traceAgent;

import java.util.Vector;

public class TracedVar
{
    private Vector<String> foundValues;
    private String name;
    private String type;
    
    public TracedVar()
    {
	name = type = "Unknown";
	foundValues = new Vector<String>();
    }

    public TracedVar(String n, String t)
    {
        name = n;
	type = t;
	foundValues = new Vector<String>();
    }

    public boolean is(String n, String t)
    {
	return name.equals(n) && type.equals(t);
    }

    public void addValue(String val)
    {
	boolean found = false;
	for (String v : foundValues)
	    if (v.equals(val))
		{
		    found = true;
		    break;
		}
	if (!found)
	    foundValues.add(val);
    }

    public String stringify(int tabs)
    {
	String t = "";
	/*for (int i = 0; i < tabs; ++i)
	    t += "\t";
	*/
	String output = "";
        output += t + "var_name: " + name + "\n";
	output += t + "var_type: " + type + "\n";
	output += t + "var_val: \n";
	for (int i = 0; i < foundValues.size(); ++i)
	    //output += t + "\t" + foundValues.get(i) + "\n";
            output += t + "" + foundValues.get(i) + "\n";
        return output;
    }

}
