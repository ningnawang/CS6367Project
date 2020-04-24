package traceAgent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TraceEntry
{

    private String token; //unique identifier
    private String className; //name of containing class
    private String methodName; //name of containing method
    private String testCase; //which test case this occured in
    private String varName; //name of the variable
    private String varValue; //value of the variable
    private String varType; //data type
    private boolean parameter; //was it a parameter
    private boolean derived; //was it derived
    private long hashcode; //hashcode value
    private final static long serialVersionUID = 4625025418890774682L;

    public TraceEntry()
    {}

    public TraceEntry(String t, String cn, String mn, String tc, String vn,
		      String vv, String vt, boolean p, boolean d, long hc)
    {
        token = t;
        className = cn;
        methodName = mn;
        testCase = tc;
        varName = vn;
        varValue = vv;
        varType = vt;
        parameter = p;
        derived = d;
        hashcode = hc;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String t)
    {
        token = t;
    }

    public TraceEntry withToken(String t)
    {
        token = t;
        return this;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String cn)
    {
        className = cn;
    }

    public TraceEntry withClassName(String cn)
    {
        className = cn;
        return this;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String mn)
    {
        methodName = mn;
    }

    public TraceEntry withMethodName(String mn)
    {
        methodName = mn;
        return this;
    }

    public String getTestCase()
    {
        return testCase;
    }

    public void setTestCase(String tc)
    {
        testCase = tc;
    }

    public TraceEntry withTestCase(String tc)
    {
        testCase = tc;
        return this;
    }

    public String getVarName()
    {
        return varName;
    }

    public void setVarName(String vn)
    {
        varName = vn;
    }

    public TraceEntry withVarName(String vn)
    {
        varName = vn;
        return this;
    }

    public String getVarValue()
    {
        return varValue;
    }

    public void setVarValue(String vv)
    {
        varValue = vv;
    }

    public TraceEntry withVarValue(String vv)
    {
        varValue = vv;
        return this;
    }

    public String getVarType()
    {
        return varType;
    }

    public void setVarType(String vt)
    {
        varType = vt;
    }

    public TraceEntry withVarType(String vt)
    {
        varType = vt;
        return this;
    }

    public boolean isParameter()
    {
        return parameter;
    }

    public void setParameter(boolean p)
    {
        parameter = p;
    }

    public TraceEntry withParameter(boolean p)
    {
        parameter = p;
        return this;
    }

    public boolean isDerived()
    {
        return derived;
    }

    public void setDerived(boolean d)
    {
        derived = d;
    }

    public TraceEntry withDerived(boolean d)
    {
        derived = d;
        return this;
    }

    public long getHashcode()
    {
        return hashcode;
    }

    public void setHashcode(long hc)
    {
        hashcode = hc;
    }

    public TraceEntry withHashcode(long hc)
    {
        hashcode = hc;
        return this;
    }

    @Override
    public String toString()
    {
	String output = "";
	output += "Token: " + token + "\n";
	output += "\tClass: " + className + "\n";
	output += "\tMethod: " + methodName + "\n";
	output += "\tTest: " + testCase + "\n";
	output += "\t\tVariable: " + varName + "\n";
	output += "\t\t\tType: " + varType + "\n";
	output += "\t\t\tValue: " + varValue + "\n";
	output += "\t\t\tisParameter?: " + parameter + "\n";
	output += "\t\t\twasDerived?: " + derived + "\n";
	
	//	System.out.println(output);
	return output;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(parameter).append(varName).append(token).append(varValue).append(derived).append(className).append(hashcode).append(testCase).append(varType).append(methodName).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
	    return true;
	if ((other instanceof TraceEntry) == false) 
            return false;
        TraceEntry rhs = ((TraceEntry) other);
        return new EqualsBuilder().append(parameter, rhs.parameter).append(varName, rhs.varName).append(token, rhs.token).append(varValue, rhs.varValue).append(derived, rhs.derived).append(className, rhs.className).append(hashcode, rhs.hashcode).append(testCase, rhs.testCase).append(varType, rhs.varType).append(methodName, rhs.methodName).isEquals();
    }

}
