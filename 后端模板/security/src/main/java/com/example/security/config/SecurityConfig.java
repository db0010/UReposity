package com.example.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("daibin").password(new BCryptPasswordEncoder().encode("123")).roles("vip1","vip2","vip3")
                .and()
                .withUser("xl").password(new BCryptPasswordEncoder().encode("456")).roles("vip1")
                .and()
                .withUser("xb").password(new BCryptPasswordEncoder().encode("789")).roles("vip1","vip2");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //授权
        http.authorizeRequests().antMatchers("/").permitAll()
//                .antMatchers("/login").permitAll()
                .antMatchers("/toLevel1/**").hasRole("vip1")
                .antMatchers("/toLevel2/**").hasRole("vip2")
                .antMatchers("/toLevel3/**").hasRole("vip3");
        //http.formLogin();开启了登录功能，不开启的化/login页面都无法访问，类似的操作就是把登录信息加入了内置的Session，这样就有了相关的权限信息
        //下面是更换默认的登录界面
        http.formLogin()
                .successForwardUrl("/") // 登录页面成功之后将要跳转去的页面
                .loginPage("/toLogin");//转到自定义的登录页面，就是替换原有登录页面的意思
        http.csrf().disable();
        http.logout().logoutSuccessUrl("/");//这样点了注销之后，就相当于把当前用户的session销毁了

    }
}
