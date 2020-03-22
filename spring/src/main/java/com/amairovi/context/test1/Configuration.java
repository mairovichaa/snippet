package com.amairovi.context.test1;

import com.amairovi.context.test1.inner.ComponentClass3;
import com.amairovi.context.test1.inner.ComponentClass4;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ComponentClass3 class3(){
        return new ComponentClass3();
    }

    @Bean
    public ComponentClass4 class4(){
        return new ComponentClass4();
    }
}
