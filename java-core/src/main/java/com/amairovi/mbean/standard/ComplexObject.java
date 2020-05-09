package com.amairovi.mbean.standard;

import java.util.Collections;
import java.util.Map;

public class ComplexObject implements ComplexObjectMBean {

    private Complex complex;
    private Map<String, String> map = Collections.singletonMap("key asda", "value asdd");

    @Override
    public Complex getComplex() {
        return complex;
    }

    @Override
    public void setComplex(Complex complex) {
        this.complex = complex;
    }

    @Override
    public Map<String, String> getMap() {
        return null;
    }

    @Override
    public void setMap(Map<String, String> map) {
        this.map = map;
    }

}
