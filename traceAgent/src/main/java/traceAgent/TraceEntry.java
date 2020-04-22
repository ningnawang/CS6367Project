package traceAgent;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "token",
        "className",
        "methodName",
        "testCase",
        "varName",
        "varValue",
        "varType",
        "parameter",
        "derived",
        "hashcode"
})
public class TraceEntry implements Serializable
{

    @JsonProperty("token")
    private String token;
    @JsonProperty("className")
    private String className;
    @JsonProperty("methodName")
    private String methodName;
    @JsonProperty("testCase")
    private String testCase;
    @JsonProperty("varName")
    private String varName;
    @JsonProperty("varValue")
    private String varValue;
    @JsonProperty("varType")
    private String varType;
    @JsonProperty("parameter")
    private boolean parameter;
    @JsonProperty("derived")
    private boolean derived;
    @JsonProperty("hashcode")
    private long hashcode;
    private final static long serialVersionUID = 4625025418890774682L;

    public TraceEntry()
    {}

    public TraceEntry(String t, String cn, String mn, String tc, String vn,
		      String vv, String vt, boolean p, boolean d, long hc)
    {
        super();
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

    @JsonProperty("token")
    public String getToken()
    {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String t)
    {
        token = t;
    }

    public TraceEntry withToken(String t)
    {
        token = t;
        return this;
    }

    @JsonProperty("className")
    public String getClassName()
    {
        return className;
    }

    @JsonProperty("className")
    public void setClassName(String cn)
    {
        className = cn;
    }

    public TraceEntry withClassName(String cn)
    {
        className = cn;
        return this;
    }

    @JsonProperty("methodName")
    public String getMethodName()
    {
        return methodName;
    }

    @JsonProperty("methodName")
    public void setMethodName(String mn)
    {
        methodName = mn;
    }

    public TraceEntry withMethodName(String mn)
    {
        methodName = mn;
        return this;
    }

    @JsonProperty("testCase")
    public String getTestCase()
    {
        return testCase;
    }

    @JsonProperty("testCase")
    public void setTestCase(String tc)
    {
        testCase = tc;
    }

    public TraceEntry withTestCase(String tc)
    {
        testCase = tc;
        return this;
    }

    @JsonProperty("varName")
    public String getVarName()
    {
        return varName;
    }

    @JsonProperty("varName")
    public void setVarName(String vn)
    {
        varName = vn;
    }

    public TraceEntry withVarName(String vn)
    {
        varName = vn;
        return this;
    }

    @JsonProperty("varValue")
    public String getVarValue()
    {
        return varValue;
    }

    @JsonProperty("varValue")
    public void setVarValue(String vv)
    {
        varValue = vv;
    }

    public TraceEntry withVarValue(String vv)
    {
        varValue = vv;
        return this;
    }

    @JsonProperty("varType")
    public String getVarType()
    {
        return varType;
    }

    @JsonProperty("varType")
    public void setVarType(String vt)
    {
        varType = vt;
    }

    public TraceEntry withVarType(String vt)
    {
        varType = vt;
        return this;
    }

    @JsonProperty("parameter")
    public boolean isParameter()
    {
        return parameter;
    }

    @JsonProperty("parameter")
    public void setParameter(boolean p)
    {
        parameter = p;
    }

    public TraceEntry withParameter(boolean p)
    {
        parameter = p;
        return this;
    }

    @JsonProperty("derived")
    public boolean isDerived()
    {
        return derived;
    }

    @JsonProperty("derived")
    public void setDerived(boolean d)
    {
        derived = d;
    }

    public TraceEntry withDerived(boolean d)
    {
        derived = d;
        return this;
    }

    @JsonProperty("hashcode")
    public long getHashcode()
    {
        return hashcode;
    }

    @JsonProperty("hashcode")
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
	
	System.out.println(output);
	return output;
	/*

        return new StringBuilder().append("{").append("\"token\":\"").append(token).append("\",")
                .append("\"className\":\"").append(className).append("\",")
                .append("\"methodName\":\"").append(methodName).append("\",")
                .append("\"testCase\":\"").append(testCase).append("\",")
                .append("\"varName\":\"").append(varName).append("\",")
                .append("\"varValue\":\"").append(varValue).append("\",")
                .append("\"varType\":\"").append(varType).append("\",")
                .append("\"parameter\":").append(parameter).append(",")
                .append("\"derived\":").append(derived).append(",")
                .append("\"hashcode\":").append(hashcode).append("}").toString();
	//*/
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
