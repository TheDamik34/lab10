package zad1;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("Point");
        CtMethod mm = cc.getDeclaredMethod("move");

        MethodInfo methodInfo = mm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();

        LocalVariableAttribute table = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        LineNumberAttribute line = (LineNumberAttribute) codeAttribute.getAttribute(LineNumberAttribute.tag);

        String arg1 = table.variableName(1);
        String arg2 = table.variableName(2);
        mm.insertBefore("System.out.println(" + arg1 +" + \" \" + " + arg2 + " );");

        for(int lineFile = 0; lineFile < 2; lineFile++) {
            int startPc = line.startPc(lineFile);
            byte[] code = codeAttribute.getCode();
            code[startPc+5] = (byte)(28 - lineFile);
        }

        cc.writeFile();
        Class pointClass = cc.toClass();
        Method[] methods = pointClass.getDeclaredMethods();
        for(Method i : methods) {
            System.out.println(i.getName());
            if(i.getName().equals("main")) {
                String[] arg = {"damian"};
                i.invoke(null, new Object[] { arg });
            }
        }
    }
}
