package lab_2;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface InvokeTimes {
    int value();
}

class TestClass {
    public void publicMethod1(String str) {
        System.out.println("Public method 1: " + str);
    }

    @InvokeTimes(1)
    public void publicMethod2(int a, int b) {
        System.out.println("Public method 2: " + a + ", " + b);
    }

    public void publicMethod3(boolean flag) {
        System.out.println("Public method 3: " + flag);
    }

    protected void protectedMethod1(double value) {
        System.out.println("Protected method 1: " + value);
    }

    @InvokeTimes(2)
    protected void protectedMethod2(String first, String second) {
        System.out.println("Protected method 2: " + first + ", " + second);
    }

    @InvokeTimes(3)
    protected void protectedMethod3(int x, boolean flag, char c) {
        System.out.println("Protected method 3: " + x + ", " + flag + ", " + c);
    }

    private void privateMethod1() {
        System.out.println("Private method 1: no parameters");
    }

    @InvokeTimes(4)
    private void privateMethod2(int value, String str) {
        System.out.println("Private method 2: " + value + ", " + str);
    }

    @InvokeTimes(2)
    private void privateMethod3(long l, float f, double d, boolean b) {
        System.out.println("Private method 3: " + l + ", " + f + ", " + d + ", " + b);
    }
}

public class lab_2 {
    public static void main(String[] args) {
        TestClass testInstance = new TestClass();
        invokeAnnotatedMethods(testInstance);
    }

    private static void invokeAnnotatedMethods(Object instance) {
        Class<?> clazz = instance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(InvokeTimes.class)) {
                if (Modifier.isPublic(method.getModifiers())) {
                    continue;
                }

                InvokeTimes annotation = method.getAnnotation(InvokeTimes.class);
                int times = annotation.value();

                method.setAccessible(true);

                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] params = createDefaultValues(paramTypes);

                System.out.println("Invoking method: " + method.getName() + " " + times + " times");

                for (int i = 0; i < times; i++) {
                    try {
                        method.invoke(instance, params);
                    } catch (Exception e) {
                        System.out.println("Error invoking method " + method.getName() + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    private static Object[] createDefaultValues(Class<?>[] paramTypes) {
        Object[] values = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];

            values[i] = switch (type.getName()) {
                case "int" -> 0;
                case "long" -> 0L;
                case "double" -> 0.0;
                case "float" -> 0.0f;
                case "boolean" -> false;
                case "char" -> 'A';
                case "byte" -> (byte) 0;
                case "short" -> (short) 0;
                case "java.lang.String" -> "default";
                default -> null;
            };
        }
        return values;
    }
}