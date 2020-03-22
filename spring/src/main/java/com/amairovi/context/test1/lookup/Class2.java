package com.amairovi.context.test1.lookup;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Class2 {

    public Class2(Class2Counter class2Counter){
        class2Counter.amountOfClass2Instances++;
    }
}
