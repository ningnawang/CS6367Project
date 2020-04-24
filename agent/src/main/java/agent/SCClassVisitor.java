package agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;

// statement coverage class visitor
public class SCClassVisitor extends ClassVisitor implements Opcodes {
    private final String className;

    // constructor
    public SCClassVisitor(final ClassVisitor cv,
                          final String name) {
        super(Opcodes.ASM5, cv);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature,
                                     final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return mv == null ? null : new SCMethodVisitor(mv, name);
    }

    // inner class
    private class SCMethodVisitor extends MethodVisitor implements Opcodes {
        private final String methodName;
        private int line;

        public SCMethodVisitor(MethodVisitor mv,
                               String methodName) {
            super(Opcodes.ASM5, mv);
            this.methodName = methodName;
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            if (0 != line) {
                this.line = line;
                mv.visitLdcInsn(className);
                mv.visitIntInsn(SIPUSH, line);
                mv.visitMethodInsn(INVOKESTATIC, "agent/SCCollector", "visitLineStatic",
                        "(Ljava/lang/String;I)V", false);
                super.visitLineNumber(line, start);
            }
        }
    }

}
