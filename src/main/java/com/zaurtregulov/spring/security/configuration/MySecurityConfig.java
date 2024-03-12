package com.zaurtregulov.spring.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class MySecurityConfig {

    DataSource dataSource;

    @Autowired
    public MySecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Теперь Spring значет, что нужно брать инфо о user-ах из Базы данных
    //А как подключиться к БД - инфо содержится в DataSource
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    //прописываем username, пароли и роли
   /* @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

   /* @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //при процессе аутентификации надо сравнивать введенные username и пароль с user и пароль, которые прописаны
        manager.createUser(User.withUsername("Zaur")
                .password(passwordEncoder.encode("zaur"))//используется дефолтное шифрование паролей
                .roles("EMPLOYEE")
                .build());
        manager.createUser(User.withUsername("Elena")
                .password(passwordEncoder.encode("elena"))
                .roles("HR")
                .build());
        manager.createUser(User.withUsername("Ivan")
                .password(passwordEncoder.encode("ivan"))
                .roles("MANAGER", "HR")
                .build());
        return manager;
    }*/

    //ограничиваем доступ на показ информации
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/").hasAnyRole("EMPLOYEE", "HR", "MANAGER")
                .requestMatchers("/hr_info").hasRole("HR")
                .requestMatchers("/manager_info/**").hasRole("MANAGER")//если будет несколько адресов начинающихся с manager_info, то можно написать так, у человека с ролью manager_info будет доступ на любой адрес, начинающийся с manager_info
                .anyRequest().authenticated() //декларирует, что все запросы к любой конечной точке должны быть авторизованы, иначе они должны быть отклонены
                .and().formLogin().permitAll();//форма логин и пароль будет запрашиваться у всех
        return httpSecurity.build();
    }
}
