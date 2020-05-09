package com.amairovi.mbean.standard;

import java.util.Map;

public interface ComplexObjectMBean {

    // in the console one can see name, but not value
    Complex getComplex();

    // does not work
    void setComplex(Complex complex);

    Map<String, String> getMap();

    void setMap(Map<String, String> map);

}
