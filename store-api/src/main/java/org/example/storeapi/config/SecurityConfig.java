package org.example.storeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String STORE_MANAGER = "STORE_MANAGER";
    public static final String STORE_EMPLOYEE = "STORE_EMPLOYEE";
    public static final String STORE_CUSTOMER = "STORE_CUSTOMER";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // error endpoint available to everyone
                        .requestMatchers("/error").permitAll()
                        // Swagger endpoints are restricted to STORE_MANAGER only
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole(STORE_MANAGER)

                        // Products endpoints configuration:
                        // - Updating a product (PUT) restricted to a manager or an employee
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAnyRole(STORE_MANAGER, STORE_EMPLOYEE)
                        // - Retrieving all products or finding a specific product (GET) is allowed for all roles
                        .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole(STORE_MANAGER, STORE_EMPLOYEE, STORE_CUSTOMER)

                        // Orders endpoints configuration:
                        // - Placing an order (POST) allowed for all roles
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasAnyRole(STORE_MANAGER, STORE_EMPLOYEE, STORE_CUSTOMER)
                        // - Retrieving orders (GET): all roles can call the endpoint
                        //   for managers and employees all orders are returned
                        //   for customer only his orders are returned
                        .requestMatchers(HttpMethod.GET, "/orders/**").hasAnyRole(STORE_MANAGER, STORE_EMPLOYEE, STORE_CUSTOMER)

                        // All other endpoints require the STORE_MANAGER role
                        .anyRequest().hasRole("STORE_MANAGER")
                )
                .httpBasic(Customizer.withDefaults())
                // Disable CSRF for demo
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails manager = User.withUsername("manager")
                .password("{noop}verySecurePassword")
                .roles(STORE_MANAGER)
                .build();
        UserDetails employee1 = User.withUsername("employee1")
                .password("{noop}verySecurePassword")
                .roles(STORE_EMPLOYEE)
                .build();
        UserDetails employee2 = User.withUsername("employee2")
                .password("{noop}verySecurePassword")
                .roles(STORE_EMPLOYEE)
                .build();
        UserDetails customer1 = User.withUsername("customer1")
                .password("{noop}verySecurePassword")
                .roles(STORE_CUSTOMER)
                .build();
        UserDetails customer2 = User.withUsername("customer2")
                .password("{noop}verySecurePassword")
                .roles(STORE_CUSTOMER)
                .build();
        UserDetails customer3 = User.withUsername("customer3")
                .password("{noop}verySecurePassword")
                .roles(STORE_CUSTOMER)
                .build();

        return new InMemoryUserDetailsManager(manager, employee1, employee2, customer1, customer2, customer3);
    }
}
