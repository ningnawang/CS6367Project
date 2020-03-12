// import org.objectweb.asm.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;

import java.util.Map;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

// statement coverage class visitor
public class SCClassVisitor extends ClassVisitor implements Opcodes {
	private String className;
	private Map<String, SortedSet<Integer>> mCoverageMap; // method coverage map

	// constructor
	public SCClassVisitor(final ClassVisitor cv,
							final String name,
							Map<String, SortedSet<Integer>> map) {
		super(Opcodes.ASM5, cv);
		this.className = name;
		this.mCoverageMap = map;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, 
									final String desc, final String signature, 
									final String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new SCMethodVisitor(mv, name, mCoverageMap);
	}

	// inner class
	private class SCMethodVisitor extends MethodVisitor implements Opcodes {
		private final String methodName;
		private Map<String, SortedSet<Integer>> mCoverageMap;

		public SCMethodVisitor(MethodVisitor mv, 
								String methodName,
								Map<String, SortedSet<Integer>>	map) {
			super(Opcodes.ASM5, mv);
			this.methodName = methodName;
			this.mCoverageMap = map;
		}

		@Override
		public void visitLineNumber(int line, Label start) {
			if (mCoverageMap.containsKey(methodName)) {
				SortedSet<Integer> lines = mCoverageMap.get(methodName);
				lines.add(line);
			} else {
				SortedSet<Integer> lines = new TreeSet<>();
				lines.add(line);
				mCoverageMap.put(methodName, lines);
			}
		}
	}

}