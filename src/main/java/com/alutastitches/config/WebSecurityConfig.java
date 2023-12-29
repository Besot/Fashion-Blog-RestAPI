package com.alutastitches.config;


import com.alutastitches.serviceImpli.UserServiceImpl;
import com.alutastitches.utils.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

//    private UserServiceImpl userService;
//
//    @Autowired
//    public WebSecurityConfig(@Lazy UserServiceImpl userService) {
//        this.userService = userService;
//    }
//
//    @Bean//bcryptPasswordEncoder is enabled for spring security hashing/salting of user's password information
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    //AuthenticationProvider(DAOAuthenticationProvider) is enabled to function as the "bouncer" in our application. Checking
//    //password and User information credibility.
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(username -> userService.loadUserByUsername(username));
//        return daoAuthenticationProvider;
//    }
//
//    @Bean//Creating our authorisation security for providing the right authorisation process
//    // from before "logging in" till after "logging out"
//    public SecurityFilterChain httpSecurity (HttpSecurity httpSecurity) throws Exception {
//         return httpSecurity
//                 .authorizeHttpRequests(httpRequest->
//                         httpRequest.requestMatchers( "/login", "/", "/sign-up").permitAll())
//                 //  // the above line to have "/login" endpoint permitted
//
//                 //permitting access to these endpoints without first authenticating or authorising.
//                 .formLogin(loginForm-> loginForm.loginPage("/login")
//
//                         .loginProcessingUrl("/dashboard"))
//                 .logout(logout->logout.logoutUrl("/logout"))
//                 .cors(AbstractHttpConfigurer::disable)//Cross Origin Resource Sharing...disabling access to our resources(what are resources?) across(in and out) our application.
//                 .csrf(AbstractHttpConfigurer::disable)//Cross Site Request Forgery...disabling other sites from creating elements(like iframe or even other html elements) in your application.
//                 .build();
//    }
@Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private UserServiceImpl userService;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, @Lazy UserServiceImpl userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(username -> userService.loadUserByUsername(username));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequest ->httpRequest
                        .requestMatchers("api/login","api/sign-up").permitAll()
                        .requestMatchers("api/dashboard").authenticated())
                .sessionManagement(sessionManagement->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
