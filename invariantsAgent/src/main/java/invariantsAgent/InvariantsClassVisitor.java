package invariantsAgent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.List;

public class InvariantsClassVisitor extends ClassVisitor implements Opcodes {
    private String className;
    private ClassNode classNode;
    private List<FieldNode> fields;

    public InvariantsClassVisitor(final ClassVisitor cv, final String cn)
    {
        super(Opcodes.ASM5, cv);
        className = cn;
        classNode = new ClassNode();
	try
	    {
		ClassReader reader = new ClassReader(className);
		reader.accept(classNode, 0);
		fields = (List<FieldNode>)classNode.fields;
	    }
	catch (IOException e)
	    {
		System.out.println("Error loading class: " + className);
	    }
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions)
    {
        String[] localVars = null;
        for (final MethodNode mn : (List<MethodNode>)classNode.methods) {
            if ( mn.name.equals(name))
		{
		    localVars = new String[mn.localVariables.size()];
		    for (LocalVariableNode n : (List<LocalVariableNode>) mn.localVariables)
			localVars[n.index] = n.name;
		}
        }
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
	if (mv == null)
	    return null;
	return new InvariantsMethodVisitor(mv, name, desc, className, access, localVars, fields);
    }
}
