package com.bfkt.forum.config;

import com.bfkt.forum.component.AuthenticationInterceptor;
import com.bfkt.forum.component.LocalDateTimeFormatter;
import com.bfkt.forum.component.MyLocalDateTimeDeserializer;
import com.bfkt.forum.utils.CommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MyWebMvcConfig extends WebMvcConfigurationSupport {
    @Value("${spring.profiles.active}")
    private String active;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 认证拦截器
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error",
                        "/**/login",
                        "/**/register",
                        "/doc.html",
                        "/webjars/**",
                        "/img/icons/**",
                        "/swagger-resources",
                        "/v3/**",
                        "/upload/**");
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //corsConfiguration.addAllowedOrigin("*");
        // 跨域配置报错，将.allowedOrigins替换成.allowedOriginPatterns即可。
        // 设置允许跨域请求的域名
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        // 设置允许的方法
        corsConfiguration.addAllowedMethod("*");
        // 是否允许证书
        corsConfiguration.setAllowCredentials(true);
        // 跨域允许时间
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (active.equals("dev")) {
            registry.addResourceHandler("/doc.html")
                    .addResourceLocations("classpath:/META-INF/resources/doc.html");
        }
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/img/icons/**")
                .addResourceLocations("classpath:/META-INF/resources/img/icons/");
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:./upload/");
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MyLocalDateTimeDeserializer());
        registry.addFormatter(new LocalDateTimeFormatter());
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
        restTemplate.getMessageConverters().add(getMappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
        converters.add(getMappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(CommonUtil.DATE_FORMATTER))
                .deserializerByType(LocalDateTime.class, new MyLocalDateTimeDeserializer());
        return new MappingJackson2HttpMessageConverter(builder.build());
    }
}
