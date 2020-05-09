package com.amairovi.mbean.standard;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Main {


    // run it
    // then open terminal and run jconsole
    // choose according process
    public static void main(String[] args) throws Exception {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.amairovi.mbean.standard:type=Hello");
        Hello mbean = new Hello();
        mbs.registerMBean(mbean, name);

        ObjectName name2 = new ObjectName("com.amairovi.mbean.standard:type=Hello2");
        Hello2 mbean2 = new Hello2();
        assertThatThrownBy(() -> mbs.registerMBean(mbean2, name2))
                .isInstanceOf(NotCompliantMBeanException.class);

        ObjectName name3 = new ObjectName("com.amairovi.mbean.standard:type=Complex");
        ComplexObject mbean3 = new ComplexObject();
        mbs.registerMBean(mbean3, name3);

        System.out.println("Waiting forever...");
        Thread.sleep(Long.MAX_VALUE);
    }

}
