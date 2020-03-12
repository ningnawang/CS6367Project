package agent;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class SCCollector {
    public static String packageName;
    public static Map<String, Map<String, SortedSet<Integer>>> coverageMap;
    public static Map<String, SortedSet<Integer>> mCoverageMap;

    public static void visitLineStatic(String className, int line) {
        if (className == null || mCoverageMap == null) return;
        SortedSet<Integer> lines = mCoverageMap.get(className);
        if (lines == null)
            lines = new TreeSet<>();
        lines.add(line);
        mCoverageMap.put(className, lines);
    }
}
