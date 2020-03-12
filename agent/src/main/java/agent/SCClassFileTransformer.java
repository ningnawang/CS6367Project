package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import java.util.Map;
import java.util.HashMap;
import java.util.SortedSet;

public class SCClassFileTransformer implements ClassFileTransformer {
    private final String packageName;
    private static Map<String, Map<String, SortedSet<Integer>>> coverageMap = new HashMap<>();

    public SCClassFileTransformer(final String name) {
        this.packageName = name;
    }

    @Override
    public byte[] transform(ClassLoader classLoader,
                            String className,
                            Class<?> aClass,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
//        System.out.println(className + " : " + packageName);
        if (className.contains(packageName)) {
            // method coverage map
            Map<String, SortedSet<Integer>> mcm = new HashMap<String, SortedSet<Integer>>();
            // ASM Code
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, 0);
            SCClassVisitor visitor = new SCClassVisitor(writer, className, mcm);
            reader.accept(visitor, 0);
            this.coverageMap.put(className, mcm);
            return writer.toByteArray();
        }
        return null;
    }


    public static void printCoverageMap() {
        for (Map.Entry<String, Map<String, SortedSet<Integer>>> entry: coverageMap.entrySet()) {
            System.out.println("----------------------------");
            System.out.println("class: " + entry.getKey());
            for (String key : entry.getValue().keySet()) {
                System.out.println("method: " + key);
                System.out.println("coverage: " + entry.getValue().get(key));
            }
        }
    }

}
