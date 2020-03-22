package com.amairovi.context.test1.lookup;

import org.springframework.beans.factory.annotation.Lookup;

public abstract class Class1 {

    public Class2 process() {
        return createClass2();
    }

    @Lookup
    abstract Class2 createClass2();
}
