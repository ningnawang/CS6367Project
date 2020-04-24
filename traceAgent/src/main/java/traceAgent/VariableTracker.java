package traceAgent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;
import java.util.UUID;

public class VariableTracker extends MethodVisitor implements Opcodes {

    private String methodName;
    private String className;
    private String rawClassName;
    private String methodDescriptor;
    private int access;
    private int counter;
    private boolean isStaticMethod;
    private String[] localVars;
    private FieldNode[] fields;

    public VariableTracker(final MethodVisitor mv, String mn, String md, String cn, int a, String[] lv, List<FieldNode> fs)
    {
        super(Opcodes.ASM5, mv);
        methodName = mn;
        methodDescriptor = md;
        rawClassName = cn;
        className = cn.replaceAll("/", ".");
        access = a;
        counter = 0;
        isStaticMethod = (this.access & Opcodes.ACC_STATIC) != 0;
        localVars = lv;

        if (fs != null)
	    {
		fields = new FieldNode[fs.size()];
		fields = fs.toArray(fields);
	    }
    }

    @Override
    public void visitCode()
    {
        if (fields != null) //keep track of known field values (parameters)
            for (FieldNode field : fields)
                addField(field);
	
        Type[] localVarTypes = Type.getArgumentTypes(methodDescriptor);
        int offset = isStaticMethod ? 0 : 1;

        String varName;
	//add each local variable
        for (int i = 0; i < localVarTypes.length; i++)
	    {
		varName = localVars != null ? localVars[i + offset] : "varName";
		addLocalVariable(localVarTypes[i].getDescriptor(), varName, i + offset);
	    }
	super.visitCode();
    }

    private void addField(FieldNode fieldNode)
    {
        if (isStaticMethod || methodName.equals("<init>"))
	    return;
        String token = UUID.randomUUID().toString();
        int opcode;
        int hashcode = 0;
        String repType;
        String toStringType = fieldNode.desc;
        String strInternal = "L" + Type.getInternalName(String.class) + ";";
        String boolInternal = "I";
        String intInternal = "I";
        String fieldMethodName = "field";

        switch (fieldNode.desc) {
            case "Z":
                opcode = Opcodes.ILOAD;
                repType = "boolean";
                break;
            case "B":
                opcode = Opcodes.ILOAD;
                repType = "byte";
                toStringType = intInternal;
                break;
            case "C":
                opcode = Opcodes.ILOAD;
                repType = "char";
                break;
            case "S":
                opcode = Opcodes.ILOAD;
                repType = "short";
                toStringType = intInternal;
                break;
            case "I":
                opcode = Opcodes.ILOAD;
                repType = "int";
                break;
            case "J":
                opcode = Opcodes.LLOAD;
                repType = "long";
                break;
            case "F":
                opcode = Opcodes.FLOAD;
                repType = "float";
                break;
            case "D":
                opcode = Opcodes.DLOAD;
                repType = "double";
                break;
            default:
                return;
        }

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(fieldMethodName);
        mv.visitLdcInsn(token);
        mv.visitLdcInsn(fieldNode.name);

        if (opcode == Opcodes.ALOAD)
	    {
		String temp = "hashcode";
		mv.visitLdcInsn(temp);
	    }
        else
	    {
		boolean isStaticField = (fieldNode.access & Opcodes.ACC_STATIC) != 0;
		if (isStaticField)
		    mv.visitFieldInsn(Opcodes.GETSTATIC, rawClassName, fieldNode.name, fieldNode.desc);
		else
		    {
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, rawClassName, fieldNode.name, fieldNode.desc);
		    }
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, strInternal, "valueOf", "(" + toStringType + ")" + strInternal, false);
	    }

        mv.visitLdcInsn(repType);
        mv.visitLdcInsn(new Integer(1));
        mv.visitLdcInsn(new Integer(0));
        mv.visitLdcInsn(new Integer(hashcode));

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "traceAgent/TraceManager", "newDatum", "(" + strInternal
                + strInternal
                + strInternal
                + strInternal
                + strInternal
                + strInternal
                + boolInternal
                + boolInternal
                + intInternal + ")V", false);
    }

    private void addLocalVariable(String desc, String varName, int index)
    {
        String token = UUID.randomUUID().toString();
        int opcode;
        int hashcode = 0;
        String repType;
        String toStringType = desc;
        String strInternal = "L" + Type.getInternalName(String.class) + ";";
        String boolInternal = "I";
        String intInternal = "I";

        switch (desc)
	    {
            case "Z":
                opcode = Opcodes.ILOAD;
                repType = "boolean";
                break;
            case "B":
                opcode = Opcodes.ILOAD;
                repType = "byte";
                toStringType = intInternal;
                break;
            case "C":
                opcode = Opcodes.ILOAD;
                repType = "char";
                break;
            case "S":
                opcode = Opcodes.ILOAD;
                repType = "short";
                toStringType = intInternal;
                break;
            case "I":
                opcode = Opcodes.ILOAD;
                repType = "int";
                break;
            case "J":
                opcode = Opcodes.LLOAD;
                repType = "long";
                break;
            case "F":
                opcode = Opcodes.FLOAD;
                repType = "float";
                break;
            case "D":
                opcode = Opcodes.DLOAD;
                repType = "double";
                break;
            default:
                return;
	    }

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(token);
        mv.visitLdcInsn(varName);

        if (opcode == Opcodes.ALOAD)
	    {
		String temp = "hashcode";
		mv.visitLdcInsn(temp);
	    }
        else
	    {
		mv.visitVarInsn(opcode, index);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, strInternal, "valueOf", "(" + toStringType + ")" + strInternal, false);
	    }

        mv.visitLdcInsn(repType);
        mv.visitLdcInsn(new Integer(0));
        mv.visitLdcInsn(new Integer(0));
        mv.visitLdcInsn(new Integer(hashcode));

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "traceAgent/TraceManager", "newDatum", "(" + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + strInternal
                                                                                                                + boolInternal
                                                                                                                + boolInternal
                                                                                                                + intInternal + ")V", false);

    }
}
