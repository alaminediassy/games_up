package com.gamesUP.gamesUP.security;

import com.gamesUP.gamesUP.filter.JwtTokenFilter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Clé secrète utilisée pour signer et valider les tokens JWT
    @Value("${jwt.secret-key}")
    String key;

    // Constantes représentant les rôles utilisateurs utilisés dans le système de sécurité
    private static final String ADMIN = "SCOPE_ROLE_ADMIN";
    private static final String CLIENT = "SCOPE_ROLE_CLIENT";

    /**
     * Crée un encodeur JWT à partir de la clé secrète.
     * Cet encodeur est utilisé pour générer les tokens JWT.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key.getBytes()));
    }

    /**
     * Crée un décodeur JWT configuré avec la même clé secrète HMAC.
     * Utilisé automatiquement par Spring Security pour valider les JWT reçus.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Fournit un encodeur de mots de passe basé sur l'algorithme BCrypt.
     * @return un mot de passe encodé
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure l'AuthenticationManager de Spring Security.
     * Il permet d'authentifier les utilisateurs en utilisant un UserDetailsService personnalisé et un password encoder.
     *
     * @param http Configuration HTTP globale
     * @param userDetailsService Service personnalisé chargé de charger les utilisateurs
     * @return l’AuthenticationManager configuré
     * @throws Exception en cas de problème de configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    /**
     * Définit comment extraire les rôles (authorities) d’un token JWT.
     * Les rôles sont extraits du champ "scope" sans préfixe automatique.
     *
     * @return un convertisseur d’authentification JWT
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    /**
     * Configure la chaîne de filtres de sécurité principale.
     * <p>
     * Autorise l'accès public à certaines routes
     * Restreint l’accès à d'autres routes en fonction du rôle de l’utilisateur
     * Active la gestion des tokens JWT via Spring Security
     * Ajoute un filtre personnalisé pour la vérification des tokens blacklistés
     *
     * @param http Configuration HTTP de Spring Security
     * @param jwtTokenFilter Filtre personnalisé pour intercepter les tokens blacklistés
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception Exception en cas de configuration invalide
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/private/admin/**").hasAuthority(ADMIN)
                        .requestMatchers("/api/private/client/**").hasAnyAuthority(CLIENT, ADMIN)
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore((Filter) jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
