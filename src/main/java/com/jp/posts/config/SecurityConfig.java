package com.jp.posts.config;

import com.jp.posts.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    /**
     * Proporciona un bean para codificar contraseñas usando BCryptPasswordEncoder,
     * que es un algoritmo de hash robusto y seguro
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define un UserDetailsService personalizado que se encarga de cargar un usuario por su correo electrónico
     * desde un repositorio (UserRepository).
     * Busca al usuario por su email (findByMail).
     * Si no lo encuentra, lanza una excepción UsernameNotFoundException.
     * Si lo encuentra, construye un objeto User con su email, contraseña y rol (USER).
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            var userEntity = userRepository.findByMail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));

            return User.builder()
                    .username(userEntity.getMail())
                    .password(userEntity.getPassword())
                    .roles("USER")
                    .build();
        };
    }

    /**
     * Configura las reglas de seguridad HTTP:
     * Desactiva CSRF: Usando httpSecurity.csrf(AbstractHttpConfigurer::disable).
     * La configuración frameOptions().sameOrigin asegura que el contenido de H2 pueda mostrarse en un iframe,
     * pero solo desde el mismo dominio, protegiendo contra ataques de clickjacking
     * Esto es común cuando no se usan formularios o para APIs.
     * Permite el acceso público a todas las solicitudes GET (permitAll()).
     * La regla requestMatchers("/h2/**").permitAll() permite que los usuarios accedan a la consola de la base de datos.
     * Exige autenticación para cualquier otra solicitud (anyRequest().authenticated()).
     * Autenticación HTTP básica: Usa autenticación básica (httpBasic(Customizer.withDefaults())).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/**").permitAll()
                        .requestMatchers("/h2/**").permitAll()
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

}
