package com.example.lab2.config;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.filter.JwtAuthenticationFilter;
import com.example.lab2.filter.JwtLoginFilter;
import com.example.lab2.filter.MakeInStreamRereadableFilter;
import com.example.lab2.interceptor.CommentReplyInterceptor;
import com.example.lab2.interceptor.UserCreditInterceptor;
import com.example.lab2.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Spring Security的安全配置
 */
@Component
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    //用户信用低于50就禁止预约
    private final static int RESERVE_MIN_CREDIT = 50;

    //用户信用低于0就禁止借阅
    private final static int BORROW_MIN_CREDIT = 0;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private HandlerInterceptor commentReplyInterceptor;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDenied();
    }

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
        //设置请求被spring security拦截时的自定义信息
        http.authorizeRequests()
                .antMatchers("/useradmin/getAllBookType").permitAll()
                .antMatchers("/useradmin/getBookType").permitAll()
                .antMatchers("/superadmin/**").hasAnyAuthority("superadmin")
                .antMatchers("/admin/**").hasAnyAuthority("superadmin", "admin")
                .antMatchers("/useradmin/**").hasAnyAuthority("undergraduate", "postgraduate", "teacher", "student", "admin", "superadmin")
                .antMatchers("/user/**").hasAnyAuthority("undergraduate", "postgraduate", "teacher", "student")
                .antMatchers("/auth/login", "/auth/register").permitAll()
                .antMatchers("/auth/changePassword").hasAnyAuthority("undergraduate", "postgraduate", "teacher", "student", "admin", "superadmin").and()
                .addFilter(new JwtLoginFilter(authenticationManager())).csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).accessDeniedHandler(accessDeniedHandler);

        //设置关闭服务的url只有在内网才能访问
        http.authorizeRequests()
                .antMatchers("/actuator/shutdown").hasIpAddress("127.0.0.1");
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor reserveInterceptor = new UserCreditInterceptor(
                RESERVE_MIN_CREDIT, userRepository, UserCreditInterceptor.WHERE_IS_USERNAME.TOKEN);

        HandlerInterceptor borrowInterceptor = new UserCreditInterceptor(
                BORROW_MIN_CREDIT, userRepository, UserCreditInterceptor.WHERE_IS_USERNAME.HTTP_SERVLET_REQUEST);


        //设置预约的信用拦截器
        registry.addInterceptor(reserveInterceptor).addPathPatterns(
                "/user/reserveBook"
        );

        //设置借阅的信用拦截器
        registry.addInterceptor(borrowInterceptor).addPathPatterns(
                "/admin/lendBookToUser", "/admin/lendReservedBookToUser"
        );

        //设置回复和评论的拦截器
        registry.addInterceptor(commentReplyInterceptor).addPathPatterns(
                "/user/postComment", "/user/postReply"
        );


    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public MakeInStreamRereadableFilter makeInStreamRereadableFilter() {
        return new MakeInStreamRereadableFilter();
    }

    @Bean
    public FilterRegistrationBean<MakeInStreamRereadableFilter> makeInStreamRereadableFilterFilterRegistrationBean() {
        FilterRegistrationBean<MakeInStreamRereadableFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(makeInStreamRereadableFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
