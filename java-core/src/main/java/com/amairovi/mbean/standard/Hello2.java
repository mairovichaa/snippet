package com.amairovi.mbean.standard;

public class Hello2 implements HelloMBean {

    private String name = "Andrei Mairovich";

    public String getName() {
        return this.name;
    }

    // it's not declared in interface, so it won't be possible to set name value
    public void setName(String name) {
        this.name = name;
    }

    public int add(int x, int y) {
        return x + y;
    }

    public void sayHello() {
        System.out.println("hello, world");
    }

    private static final int DEFAULT_CACHE_SIZE = 200;

    private int cacheSize = DEFAULT_CACHE_SIZE;

    public int getCacheSize() {
        return this.cacheSize;
    }

    public synchronized void setCacheSize(int size) {

        this.cacheSize = size;
        System.out.println("Cache size now " + this.cacheSize);
    }

}
