package drajner.hetman.config;

import drajner.hetman.services.UserDetailsServiceImpl;
import drajner.hetman.services.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        /*
        http.authorizeHttpRequests((authorization) -> authorization
                .anyRequest().authenticated()).httpBasic(withDefaults());

        http.authorizeRequests()
                .antMatchers("/", "/public/**").permitAll() // Public endpoints
                .antMatchers("/admin/**").hasRole("ADMIN") // Admin access only
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // User and Admin access
                .anyRequest().authenticated() // Any other requests must be authenticated
                .and()
                .formLogin() // Enable form-based login
                .loginPage("/login").permitAll()
                .and()
                .logout() // Enable logout functionality
                .permitAll();


        return http.build();

         */
        return http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/getUsers").hasRole(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/tournaments/get").permitAll()
                        .requestMatchers(HttpMethod.POST, "/tournaments/add").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/tournaments/delete/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/rename/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/generateGroups/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/generateLadder/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "/tournaments/getFinals/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/tournaments/evaluateFinals/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/tournaments/purgeFinals/**").hasRole(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/groups/get/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/groups/delete/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/setModifier/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/autoGenerateFights/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/evaluateGroups/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/addFight/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/groups/addParticipant/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/groups/deleteParticipant/**").hasRole(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/participants/get/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/participants/add/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/participants/delete/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/replace").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/disqualify/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/compete/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/participants/eliminate/**").hasRole(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/fights/get/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/fights/replace/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/fights/delete/**").hasRole(UserStatus.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, "/reports/get").permitAll()
                        .requestMatchers(HttpMethod.POST, "/reports/add").hasAnyRole(UserStatus.ADMIN.toString(), UserStatus.STANDARD.toString())
                        .requestMatchers(HttpMethod.DELETE, "/reports/delete/**").hasRole(UserStatus.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/reports/rename/**").hasRole(UserStatus.ADMIN.toString())
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager).build();
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

}