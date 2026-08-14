package com.ispmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                /*
                 * Páginas públicas
                 */
                .requestMatchers(
                        "/login",
                        "/css/**",
                        "/js/**",
                        "/favicon.ico"
                ).permitAll()

                /*
                 * SOMENTE ADMIN
                 */
                .requestMatchers("/usuarios/**")
                .hasRole("ADMIN")

                /*
                 * Clientes:
                 * ADMIN + SUPORTE
                 */
                .requestMatchers("/clientes/**")
                .hasAnyRole("ADMIN", "SUPORTE")

                /*
                 * Planos:
                 * ADMIN + SUPORTE
                 */
                .requestMatchers("/planos/**")
                .hasAnyRole("ADMIN", "SUPORTE")

                /*
                 * Faturas:
                 * ADMIN + FINANCEIRO
                 */
                .requestMatchers("/faturas/**")
                .hasAnyRole("ADMIN", "FINANCEIRO")

                /*
                 * Demais páginas precisam
                 * de autenticação.
                 */
                .anyRequest()
                .authenticated()
            )

            /*
             * Login
             */
            .formLogin(form -> form

                .loginPage("/login")

                .loginProcessingUrl("/login")

                .defaultSuccessUrl("/", true)

                .failureUrl("/login?erro")

                .permitAll()
            )

            /*
             * Logout
             */
            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login?logout")

                .permitAll()
            );

        return http.build();
    }
}