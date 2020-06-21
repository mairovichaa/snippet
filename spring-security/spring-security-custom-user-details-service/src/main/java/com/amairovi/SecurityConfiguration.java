package com.amairovi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration {

    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    private static class CustomUserDetailsService implements UserDetailsService {

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            // here logic goes
            if (username.equals("user")) {
                return User.builder()
                        .username(username)
                        .password("password")
                        .roles("KING", "GENIUS")
                        .build();
            }

            throw new UsernameNotFoundException("No user with such name.");
        }

    }

    // otherwise, org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder is used and
    // there is an exception
    // use '{noop}password' instead of 'password' to avoid it
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
