package traceAgent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

public class VarTracer extends MethodVisitor implements Opcodes {

    private String methodName;
    private String className;
    private String rawClassName;
    private String methodDescriptor;
    private int access;
    private int counter;
    private boolean isStaticMethod;
    private String[] localVars;
    private FieldNode[] fields;

    public VarTracer(final MethodVisitor mv, String mn, String md, String cn, int a, String[] lv, List<FieldNode> fs)
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
        if (fields != null)
            for (FieldNode field : fields)
                addClassField(field);
	
        Type[] localVarTypes = Type.getArgumentTypes(methodDescriptor);
        int offset = isStaticMethod ? 0 : 1;

        String varName;
	//add each local variable
        for (int i = 0; i < localVarTypes.length; i++)
	    {
		varName = localVars != null ? localVars[i + offset] : "varName";
		addParamVariable(localVarTypes[i].getDescriptor(), varName, i + offset);
	    }
	super.visitCode();
    }

    private void addClassField(FieldNode fieldNode)
    {
	
	if (isStaticMethod || methodName.equals("<init>"))
	    return;

	int opcode;
        String toStringType = fieldNode.desc;
        String strIdent = "L" + Type.getInternalName(String.class) + ";";
        String boolIdent = "I", intIdent = "I";
        String fieldMethodName = "field";

	String type = getType(fieldNode.desc);
	if (type.equals("error"))
	    return;
	else if (type.equals("short") || type.equals("byte"))
	    toStringType = "I";
	opcode = getCode(fieldNode.desc);

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(fieldMethodName);
        mv.visitLdcInsn(fieldNode.name);

	boolean isStaticField = (fieldNode.access & Opcodes.ACC_STATIC) != 0;
	if (isStaticField)
	    mv.visitFieldInsn(Opcodes.GETSTATIC, rawClassName, fieldNode.name, fieldNode.desc);
	else
	    {
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, rawClassName, fieldNode.name, fieldNode.desc);
	    }
	mv.visitMethodInsn(Opcodes.INVOKESTATIC, strIdent, "valueOf", "(" + toStringType + ")" + strIdent, false);


        mv.visitLdcInsn(type);
        mv.visitLdcInsn(new Integer(0));

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "traceAgent/TraceManager", "callInsert", "("
			   + strIdent //className
			   + strIdent //methodName
			   + strIdent //varName
			   + strIdent //value
			   + strIdent //type
			   + boolIdent//isParam
			   + ")V", false);
    }

    private void addParamVariable(String desc, String varName, int index)
    {
        int opcode;
        String toStringType = desc;
        String strIdent = "L" + Type.getInternalName(String.class) + ";";
        String boolIdent = "I", intIdent = "I";
	String type = getType(desc);

	if (type.equals("error"))
	    return;
	else if (type.equals("short") || type.equals("byte"))
	    toStringType = "I";

	opcode = getCode(desc);

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(varName);

	mv.visitVarInsn(opcode, index);
	mv.visitMethodInsn(Opcodes.INVOKESTATIC, strIdent, "valueOf", "(" + toStringType + ")" + strIdent, false);
	
        mv.visitLdcInsn(type);
        mv.visitLdcInsn(new Integer(1));

	mv.visitMethodInsn(Opcodes.INVOKESTATIC, "traceAgent/TraceManager", "callInsert", "("
			   + strIdent //className
			   + strIdent //methodName
			   + strIdent //varName
			   + strIdent //value
			   + strIdent //type
			   + boolIdent//isParam
			   + ")V", false);
    }

    private String getType(String d)
    {
	switch (d)
	    {
            case "Z":
                return "boolean";
            case "B":
                return "byte";
            case "C":
                return "char";
            case "S":
                return "short";
            case "I":
                return "int";
            case "J":
                return "long";
            case "F":
                return "float";
            case "D":
                return "double";
            }
	return "error";
    }

    private int getCode(String d)
    {
	switch (d)
	    {
	    case "J":
                return Opcodes.LLOAD;
	    case "F":
                return Opcodes.FLOAD;
	    case "D":
                return Opcodes.DLOAD;
	    default:
                return Opcodes.ILOAD;
	    } 
    }
}
