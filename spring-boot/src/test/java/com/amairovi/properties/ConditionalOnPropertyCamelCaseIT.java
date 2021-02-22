package com.amairovi.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static com.amairovi.properties.ConditionalOnPropertyCamelCase.EXPECTED_VALUE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ConditionalOnPropertyCamelCaseIT {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(UserConfigurations.of(TrivialConfiguration.class))
            .withBean(ConditionalOnPropertyCamelCase.class);

    @EnableConfigurationProperties
    static class TrivialConfiguration {
    }

    @Test
    public void configurationShouldBePresentedWhenCamelCaseIsUsedInDeclaration() {
        runner.withPropertyValues(
                "property.dashStyle.enabled=" + EXPECTED_VALUE,
                "property.dashStyle.properties.inner1=value 1",
                "property.dashStyle.properties.inner2=value 2"
        )
                .run(ctx ->
                        {
                            final ConditionalOnPropertyCamelCase actual = ctx.getBean(ConditionalOnPropertyCamelCase.class);
                            assertThat(actual).isInstanceOf(ConditionalOnPropertyCamelCase.class);
                            assertThat(actual.getInner1()).isEqualTo("value 1");
                            assertThat(actual.getInner2()).isEqualTo("value 2");
                        }
                );
    }

    @Test
    public void configurationShouldNotBePresentedWhenEnabledUsingDashAndConditionInCamelCase() {
        runner.withPropertyValues(
                "property.dash-style.enabled=" + ConditionalOnPropertyCamelCase.EXPECTED_VALUE
        )
                .run(ctx ->
                        {
                            assertThatThrownBy(() -> ctx.getBean(ConditionalOnPropertyCamelCase.class))
                                    .isInstanceOf(NoSuchBeanDefinitionException.class)
                                    .hasMessage("No qualifying bean of type 'com.amairovi.properties.ConditionalOnPropertyCamelCase' available");
                        }
                );
    }

    @Test
    public void configurationShouldBePresentedWhenCamelCaseAndDashMixed2() {
        runner.withPropertyValues(
                "property.dashStyle.enabled=" + ConditionalOnPropertyCamelCase.EXPECTED_VALUE,
                "property.dash-style.properties.inner1=value 1",
                "property.dashStyle.properties.inner2=value 2"
        )
                .run(ctx ->
                        {
                            final ConditionalOnPropertyCamelCase actual = ctx.getBean(ConditionalOnPropertyCamelCase.class);
                            assertThat(actual).isInstanceOf(ConditionalOnPropertyCamelCase.class);
                            assertThat(actual.getInner1()).isEqualTo("value 1");
                            assertThat(actual.getInner2()).isEqualTo("value 2");
                        }
                );
    }

    @Test
    public void configurationShouldNotBePresented() {
        runner.withPropertyValues("property.dash-style.enabled=not enabled")
                .run(ctx ->
                        {
                            assertThatThrownBy(() -> ctx.getBean(ConditionalOnPropertyCamelCase.class))
                                    .isInstanceOf(NoSuchBeanDefinitionException.class)
                                    .hasMessage("No qualifying bean of type 'com.amairovi.properties.ConditionalOnPropertyCamelCase' available");
                        }
                );
    }

}