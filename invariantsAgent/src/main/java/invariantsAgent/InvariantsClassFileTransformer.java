package invariantsAgent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class InvariantsClassFileTransformer implements ClassFileTransformer {

    private final String pack;

    public InvariantsClassFileTransformer(String p)
    {
        pack = p;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException
    {
        if (className.startsWith(pack))
	    {
		org.objectweb.asm.ClassReader cr = new ClassReader(classfileBuffer);
		org.objectweb.asm.ClassWriter cw = new org.objectweb.asm.ClassWriter(ClassWriter.COMPUTE_FRAMES);
		InvariantsClassVisitor ca = new InvariantsClassVisitor(cw, className);
		cr.accept(ca, 0);
		return cw.toByteArray();
	    }
        return classfileBuffer;
    }
}
