package drajner.hetman.config;

import drajner.hetman.services.UserDetailsServiceImpl;
import drajner.hetman.status.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer -> {
                    httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/h2**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/getUsers").hasAuthority(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/tournaments/get").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tournaments/get/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/tournaments/add").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/tournaments/delete/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/rename/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/generateGroups/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/generateLadder/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "/tournaments/getFinals/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/tournaments/evaluateFinals/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/purgeFinals/**").hasAuthority(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/groups/get/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/groups/getOne/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/groups/delete/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/setModifier/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/autoGenerateFights/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/evaluateGroups/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/addFight/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/addParticipant/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/groups/deleteParticipant/**").hasAuthority(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/participants/get/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/participants/add/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/participants/delete/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/replace").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/disqualify/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/compete/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/eliminate/**").hasAuthority(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/fights/get/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/fights/getOne/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/fights/replace/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/fights/delete/**").hasAuthority(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/reports/get**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/reports/add").hasAnyAuthority(UserStatus.ADMIN.toString(), UserStatus.STANDARD.toString())
                        .requestMatchers(HttpMethod.DELETE, "/reports/delete/**").hasAuthority(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/reports/rename/**").hasAuthority(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.POST, "/error**").permitAll()



                        .requestMatchers("**").permitAll()    // permission for all endpoints, to use comment above matchers
                        .anyRequest().authenticated())

                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception{
        AuthenticationManagerBuilder amb = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        amb.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return amb.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console**");
    }

}