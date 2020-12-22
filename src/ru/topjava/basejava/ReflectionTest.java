package ru.topjava.basejava;

import ru.topjava.basejava.model.Resume;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Resume r = new Resume();
        Class<? extends Resume> aClass = r.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().toLowerCase().equals("tostring")) {
                System.out.println(m.invoke(r));
                break;
            }
        }

        // Class<?> clazz = ;
        System.out.println(Class.forName("ru.topjava.basejava.ReflectionTest").getPackageName());
    }

    @init(next = 1)
    public void initialization() {
    }

}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface init {
    String version() default "1.0";

    int next();
}