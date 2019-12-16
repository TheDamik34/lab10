package zad2;

import javassist.*;
import javassist.bytecode.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("Factorial");
        CtMethod method1 = cc.getDeclaredMethod("fact1");
        CtMethod method2 = cc.getDeclaredMethod("fact2");

        method1.useCflow("fact1");
        method1.insertAt(0, "if($cflow(fact1) == 1) System.out.println(\"Fact1: metoda jest wywolana rekurencyjnie\");");

        method2.useCflow("fact2");
        method2.insertAfter("if($cflow(fact2) == 0) System.out.println(\"Fact2: metoda nie jest wywolana rekurencyjnie\");");

        Class kappa = cc.toClass();
        Method main = kappa.getMethod("main",  String[].class);
        main.setAccessible(true);
        main.invoke(null, new Object[] {null});

        cc.writeFile();
    }
}
