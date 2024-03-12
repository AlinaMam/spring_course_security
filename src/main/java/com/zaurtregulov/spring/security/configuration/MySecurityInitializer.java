package com.zaurtregulov.spring.security.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
//если не будет прописан этот класс, то не будет запрашиваться форма для аутентификации
//где будем вводить username и пароль
public class MySecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
