package com.amairovi.context.test1.circular;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

public class Class3 {

    @Getter
    @Setter
    @Autowired
    private Class4 class4;

}
