package com.amairovi.context.test1.scope;

import org.springframework.context.annotation.Scope;

import static com.amairovi.context.test1.scope.CustomScope.CUSTOM_SCOPE_NAME;

@Scope(CUSTOM_SCOPE_NAME)
public class BeanWithCustomScope {
}
