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
    private static String packageName;

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
        if (className.contains(packageName) ||
            className.contains("org/apache/commons/dbutils/") ||
            className.contains("pazone/ashot/") ||
            className.contains("com/ning/compress/") ||
            className.contains("perf/") ||
            className.contains("com/jadventure/game") ||
            className.contains("com/elibom/jogger/") ||
            className.contains("com/lastcalc/") ||
            className.contains("com/facebook/LinkBench/") ||
            className.contains("org/redline_rpm/") ||
            className.contains("org/mybatis") ||
            className.contains("au/com/ds/ef/") ||
            className.contains("spark/")) {

            // method coverage map
            Map<String, SortedSet<Integer>> mcm = new HashMap<String, SortedSet<Integer>>();
            // ASM Code
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
            SCClassVisitor visitor = new SCClassVisitor(writer, className);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }
        return classfileBuffer;
    }

}
