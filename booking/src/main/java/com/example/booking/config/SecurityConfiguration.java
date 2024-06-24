package com.example.booking.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@SecurityScheme(name = "Bearer authentication", type = SecuritySchemeType.HTTP, scheme = "bearer")
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()
                        ))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(GET,"/api/booking/findAll","/api/booking/findById/"
                                ,"/api/userInfo/findById/").hasAuthority("USER")
                        .requestMatchers(GET,"/api/businesses/admin").hasAuthority("ADMIN")
                        .requestMatchers(GET,"/api/users/findAll","/api/businesses/findAll","/api/businesses/findById/"
                                , "/api/rooms/findAll","/api/rooms/findById/"
                                ,"/api/roomPricing/findAll","/api/roomPricing/findById/","/api/businesses/prove").permitAll()

                        .requestMatchers(POST,"/api/auth/**").permitAll()
                        .requestMatchers(POST,"/api/booking/save","/api/businesses/search"
                                ,"/api/rooms/getAvailableRooms").hasAuthority("USER")
                        .requestMatchers(POST,"/api/businesses/save"
                                ,"/api/rooms/save","/api/roomPricing/save").hasAuthority("ADMIN")

                        .requestMatchers(PUT,"/api/businesses/update/","/api/rooms/update/"
                                ,"/api/roomPricing/update/").hasAuthority("ADMIN")

                        .requestMatchers(DELETE,"/api/booking/delete/").hasAuthority("USER")
                        .requestMatchers(DELETE,"/api/businesses/delete/","/api/rooms/delete/"
                                ,"/api/roomPricing/update/").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://192.168.1.78:3000");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
//        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://192.168.1.78:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
