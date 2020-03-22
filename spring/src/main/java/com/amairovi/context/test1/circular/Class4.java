package com.amairovi.context.test1.circular;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

public class Class4 {

    @Getter
    @Setter
    @Autowired
    private Class3 class3;

}
