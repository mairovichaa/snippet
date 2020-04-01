package com.amairovi.context.test1.scope;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;

import static com.amairovi.context.test1.scope.CustomScope.CUSTOM_SCOPE_NAME;

@Scope(CUSTOM_SCOPE_NAME)
@Getter
@RequiredArgsConstructor
public class BeanWithSingletonDependency {
    private final Dependency dependency;
}
