package invariantsAgent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.Vector;

public class InvariantsAgent {
    final static String prefix = "src" + File.separator + "main" + File.separator + "java";
        
    public static void premain(String args, Instrumentation i)
    {
        String base = getBasePackage();  //get base package
        if (base.length() == 0)
            System.err.println("Unable to read package name from pom.xml");
        else
	    {
		TraceManager.getInstance().setProgName(getPackage()); //Set the program name
		i.addTransformer(new InvariantsClassFileTransformer(base)); //add the transformer
        }
    }

    private static String getBasePackage()
    {
        String base = "";
        for (String p : getPackages()) 
            if (base.length() == 0 || base.length() > p.length())
                base = p;
	return base;
    }

    private static Vector<String> getPackages()
    {
        File rootDir = new File(prefix);
        File[] files = rootDir.listFiles();
        return listDir(files);
    }

    private static Vector<String> listDir(File[] files)
    {
	Vector<String> packages = new Vector<String>();
        for (File f : files)
            if (f.isDirectory())
                packages.addAll(listDir(f.listFiles()));
            else
		packages.add(f.getParent().substring(f.getParent().lastIndexOf(prefix) + prefix.length() + 1));
	return packages;
    }

    private static String getPackage() {
        try
	    {
		File pomFile = new File("pom.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(pomFile);
		doc.getDocumentElement().normalize();
		NodeList rootNodes = doc.getChildNodes();
		NodeList curNodes = null;
		for (int i = 0; i < rootNodes.getLength(); i++)
		    if (rootNodes.item(i).getNodeName().equals("project"))
			{
			    curNodes = rootNodes.item(i).getChildNodes();
			    break;
			}
		if (curNodes == null || curNodes.getLength() == 0)
		    return null;
		Node n = null;
		for (int i = 0; i < curNodes.getLength(); i++)
		    {
			if (curNodes.item(i).getNodeName().equals("groupId"))
			    {
				n = curNodes.item(i);
				break;
			    }
		    }
		if (n == null)
		    return null;
		if (n.getNodeType() == Node.ELEMENT_NODE)
		    return ((Element)n).getTextContent();
		return null;
	    }
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
        return null;
    }
}
