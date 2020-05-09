package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task25 {

    public static String genericDeclaration(Method m) {

        System.out.println(m.toGenericString());

        TypeVariable<Method>[] typeParameters = m.getTypeParameters();
        if (typeParameters.length == 0) {
            System.out.println("No type parameters");
            return "No type parameters";
        }


        for (int i = 0; i < typeParameters.length; i++) {
            TypeVariable<Method> typeParameter = typeParameters[i];
            String name = typeParameter.getName();
            System.out.println(name + " : ");
            Type[] bounds = typeParameter.getBounds();

            for (int j = 0; j < bounds.length; j++) {
                Type bound = bounds[j];
                if (bound instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) bound;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();


                }
            }

        }
//        m.toGenericString()
        m.getGenericParameterTypes();
        return null;
    }

    public static List<String> genericDeclaration2(Method m) {
        List<String> result = new ArrayList<>();

        TypeVariable<Method>[] typeParameters = m.getTypeParameters();
        for (TypeVariable<Method> typeVariable : typeParameters) {
           result.add(typeVariable.getName() + " - simple param.");
        }

        return result;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method get;
        get = Collections.class.getMethod("sort", List.class);
        genericDeclaration(get);

        get = List.class.getMethod("get", int.class);
        genericDeclaration(get);
        get = List.class.getMethod("add", Object.class);
        genericDeclaration(get);

    }

}
