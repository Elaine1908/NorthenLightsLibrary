package com.example.lab2.config;

import com.example.lab2.filter.JwtAuthenticationFilter;
import com.example.lab2.filter.JwtLoginFilter;
import com.example.lab2.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


/**
 * Spring Security的安全配置
 */
@Component
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationEntryPoint myAuthenticationFailEntryPoint;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new MyAuthenticationFailEntryPointImpl();
    }


    /**
     * 在这里设置UserDetailsService
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //TODO 在这里设置UserDetailsService

        //先简单设置密码明文存储
        super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    /**
     * 在这里设置哪些借口分别可以被哪些用户访问
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/superadmin/**").hasAnyAuthority("superadmin")
                .antMatchers("/admin/**").hasAnyAuthority("superadmin", "admin")
                .antMatchers("/useradmin/**").hasAnyAuthority("teacher", "student", "admin", "superadmin")
                .antMatchers("/user/**").hasAnyAuthority("teacher", "student")
                .antMatchers("/auth/login", "/auth/register").permitAll()
                .antMatchers("/auth/changePassword").hasAnyAuthority("teacher", "student", "admin", "superadmin")
                .antMatchers("/student").hasAnyAuthority("student").and()
                .addFilter(new JwtLoginFilter(authenticationManager())).csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        //设置请求被spring security拦截时的自定义信息
        http.exceptionHandling().authenticationEntryPoint(myAuthenticationFailEntryPoint);
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
