package traceAgent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.HashSet;

public class TraceAgent
{

    public static void premain(String args, Instrumentation inst)
    {
        HashSet<String> packages = getPackages();
        String pack = shortestPackage(packages);
        if (pack == null || pack.length() == 0)
            System.out.println("Unsable to read package name from pom.xml");
        else
	    {
            TraceManager.getInstance().setProgName(getPackge());
            inst.addTransformer(new TraceClassTransformer(pack));
        }
    }

    private static String shortestPackage(HashSet<String> packages)
    {
        String result = null;
        for (String p : packages) 
            if (result == null || p.length() < result.length())
                result = p;
	return result;
    }

    private static HashSet<String> getPackages()
    {
        String prefix = "src" + File.separator + "main" + File.separator + "java";
        File rootDir = new File(prefix);
        File[] files = rootDir.listFiles();
        HashSet<String> packages = new HashSet<>();
        listPackages(files, packages, prefix);
        return packages;
    }

    private static void listPackages(File[] files, HashSet<String> packages, String prefix)
    {
        for (File f : files)
            if (f.isDirectory())
                listPackages(f.listFiles(), packages, prefix);
            else
		{
		    String path = f.getParent();
		    path = path.substring(path.lastIndexOf(prefix) + prefix.length() + 1);
		    packages.add(path);
		}
    }

    private static String getPackge() {
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
	catch (ParserConfigurationException e)
	    {
		e.printStackTrace();
	    }
	catch (IOException e)
	    {
		e.printStackTrace();
	    }
	catch (SAXException e)
	    {
		e.printStackTrace();
	    }
        return null;
    }

}
