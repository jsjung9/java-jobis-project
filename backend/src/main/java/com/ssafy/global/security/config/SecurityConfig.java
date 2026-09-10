package com.ssafy.global.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.domain.users.repository.UserRepository;
import com.ssafy.global.response.code.ErrorCode;
import com.ssafy.global.response.structure.ErrorResponse;
import com.ssafy.domain.users.service.RefreshTokenService;
import com.ssafy.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.ssafy.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.ssafy.global.oauth2.service.CustomOAuth2UserService;
import com.ssafy.global.security.handler.CustomAuthenticationFailureHandler;
import com.ssafy.global.security.handler.CustomAuthenticationSuccessHandler;
import com.ssafy.global.security.util.JwtUtil;
import com.ssafy.global.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.ssafy.global.security.filter.JwtAuthenticationFilter;
import com.ssafy.global.security.handler.CustomExceptionHandleFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private  final UserRepository userRepository;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomExceptionHandleFilter customExceptionHandleFilter;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                writeSecurityError(response, ErrorCode.UNAUTHORIZED_REQUEST))
                        .accessDeniedHandler((request, response, exception) ->
                                writeSecurityError(response, ErrorCode.FORBIDDEN_ACCESS)))
                .authorizeHttpRequests((authorize) ->
                                authorize
                                        .requestMatchers(
                                                "/oauth2/**",
                                                "/login/oauth2/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/actuator/health",
                                                "/actuator/health/**",
                                                "/user/login/**"
                                        ).permitAll()
                                        .requestMatchers(HttpMethod.POST, "/user/login", "/user/register", "/user/token").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/user/check").permitAll()
                                        .requestMatchers(HttpMethod.GET,
                                                "/company/search",
                                                "/company/info/**",
                                                "/company/statement/**",
                                                "/company/rate/**",
                                                "/company/youthcompany/**",
                                                "/company/quarter/**",
                                                "/company/popular",
                                                "/news/**",
                                                "/patent/**",
                                                "/recommendation/**",
                                                "/worldcup/**"
                                        ).permitAll()
                                        .anyRequest().authenticated()
                )
                .oauth2Login((httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                        ))
        ;

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(customExceptionHandleFilter, JwtAuthenticationFilter.class);
        http.addFilterBefore(customUsernamePasswordAuthenticationFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }
    
    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter filter =
                new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return filter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userRepository);
    }

    private void writeSecurityError(HttpServletResponse response, ErrorCode errorCode)
            throws java.io.IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(java.nio.charset.StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(), ErrorResponse.of(errorCode));
    }
}
