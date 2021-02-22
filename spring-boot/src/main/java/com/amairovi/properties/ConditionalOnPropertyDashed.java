package com.amairovi.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("property.dash-style.properties")
@ConditionalOnProperty(name = "property.dash-style.enabled", havingValue = ConditionalOnPropertyDashed.EXPECTED_VALUE)
public class ConditionalOnPropertyDashed {
    static final String EXPECTED_VALUE = "expected value";

    private String inner1;
    private String inner2;

    public String getInner1() {
        return inner1;
    }

    public void setInner1(final String inner1) {
        this.inner1 = inner1;
    }

    public String getInner2() {
        return inner2;
    }

    public void setInner2(final String inner2) {
        this.inner2 = inner2;
    }
}
