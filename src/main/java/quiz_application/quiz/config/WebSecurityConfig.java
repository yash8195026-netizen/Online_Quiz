package quiz_application.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import quiz_application.quiz.service.QuizUserDetailsService;
import quiz_application.quiz.config.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final QuizUserDetailsService userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;
    
    public WebSecurityConfig(QuizUserDetailsService userDetailsService,LoginSuccessHandler loginSuccessHandler){
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler= loginSuccessHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
            .authorizeHttpRequests(auth -> auth.requestMatchers("/login","/register","/verifyOtp","/resetPasswordOtp","/resetPassword","/forgotPassword","/css/**","/js/**","/test-email").permitAll()
            .requestMatchers("/quizList","/addQuiz","/editQuiz/**","/deleteQuiz/**").hasRole("ADMIN")
            .requestMatchers("/home","/quiz","/submit","/results","/profile").hasAnyRole("USER","ADMIN")
            .anyRequest()
            .authenticated()
        )
        .formLogin(form -> form.loginPage("/login").successHandler(loginSuccessHandler).permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();

    }
}
