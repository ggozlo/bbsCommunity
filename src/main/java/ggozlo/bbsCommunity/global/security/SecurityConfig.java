package ggozlo.bbsCommunity.global.security;

import ggozlo.bbsCommunity.global.security.handler.FormAccessDeniedHandler;
import ggozlo.bbsCommunity.global.security.handler.FormLoginFailureHandler;
import ggozlo.bbsCommunity.global.security.handler.FormLoginSuccessHandler;
import ggozlo.bbsCommunity.global.security.handler.FormLogoutSuccessHandler;
import ggozlo.bbsCommunity.global.security.provider.FormAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .defaultSuccessUrl("/")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()
            .and()
                .logout()
                .logoutSuccessHandler(formLogoutSuccessHandler())
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/denied")
                .accessDeniedHandler(accessDeniedHandler())
            .and()
                .csrf().disable();
    }



    @Bean // ???????????? ?????????
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean // ???????????? ?????????
    public AccessDeniedHandler accessDeniedHandler() {
        FormAccessDeniedHandler formAccessDeniedHandler = new FormAccessDeniedHandler("/denied");
        return formAccessDeniedHandler;
    }

    @Bean // ???????????? ?????? ?????????
    public FormLogoutSuccessHandler formLogoutSuccessHandler() {
        return new FormLogoutSuccessHandler();
    }

    @Bean // ?????? ?????? ?????????
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new FormLoginSuccessHandler();
    }

    @Bean // ?????? ?????? ?????????
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new FormLoginFailureHandler();
    }

    @Bean // ?????? ?????? ?????????
    public AuthenticationProvider authenticationProvider() {
        return new FormAuthenticationProvider(userDetailsService, passwordEncoder());
    }

}
