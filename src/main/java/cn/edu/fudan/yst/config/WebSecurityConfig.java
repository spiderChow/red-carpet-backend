package cn.edu.fudan.yst.config;

import cn.edu.fudan.yst.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author gzdaijie
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = UserDao.class)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDao userDao;

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.inMemoryAuthentication().withUser("user")
//                .password("password").roles("SUPER_ADMIN");
//    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
        http.csrf().disable();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////,"/hongtan/vote/api/admin/**"
//        http
//                .authorizeRequests()
//                .antMatchers("/","/*", "/hongtan/vote/api/user/**", "/static/**", "/favicon.ico", "/wx-share.jpg","/hongtan/vote/api/admin/**").permitAll()
//                .anyRequest().authenticated()
//
//                .and().formLogin()
//                .loginProcessingUrl("/hongtan/vote/login")
//                .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication auth) -> {
//                    Cookie cookie = new Cookie("admin", "true");
//                    cookie.setPath("/");
//                    cookie.setHttpOnly(false);
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    response.addCookie(cookie);
//                })
//                .failureHandler((HttpServletRequest request, HttpServletResponse response, AuthenticationException e) -> {
//                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                })
//                .and().logout().permitAll();
//        http.
//                csrf().disable();
//
//        http
//                .exceptionHandling()
//                .authenticationEntryPoint((HttpServletRequest request, HttpServletResponse response, AuthenticationException e) -> {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未授权");
//                });
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.authenticationProvider(new AuthenticationProvider() {
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                String name = authentication.getName();
//                String password = authentication.getCredentials().toString();
//                if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
//                    return null;
//                }
//
//                if (userDao.count() < 1) {
//                    String roles = "ROLE_ADMIN,ROLE_SUPER_ADMIN";
//                    List<GrantedAuthority> authority = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
//                    return new UsernamePasswordAuthenticationToken(name, password, authority);
//                }
//
//                User user = userDao.findOne(name);
//                if (user == null || !(password.equals(user.getPassword()) || Utils._3md5(password).equals(user.getPassword()))) {
//                    return null;
//                }
//
//                List<GrantedAuthority> authority = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles());
//                return new UsernamePasswordAuthenticationToken(name, password, authority);
//            }
//
//            @Override
//            public boolean supports(Class<?> authentication) {
//                return authentication.equals(UsernamePasswordAuthenticationToken.class);
//            }
//        });
//    }
}
