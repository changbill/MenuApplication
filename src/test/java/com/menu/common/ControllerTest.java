package com.menu.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.menu.auth.config.AuthenticationConfig;
import com.menu.auth.config.SecurityConfig;
import com.menu.auth.security.jwt.JwtProvider;
import com.menu.auth.service.CustomOAuth2UserService;
import com.menu.file.service.FileService;
import com.menu.menu.controller.MenuApiController;
import com.menu.menu.service.MenuFindService;
import com.menu.menu.service.MenuService;
import com.menu.store.service.StoreFindService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@ImportAutoConfiguration({
        SecurityConfig.class,
        AuthenticationConfig.class
})  // 전체 애플리케이션 구성 대신 특정 자동 구성만 선택적으로 적용
@WebMvcTest({
        MenuApiController.class
})
@WithMockUser("test")
public abstract class ControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected JwtProvider jwtProvider;

    @MockitoBean
    protected FileService fileService;

    @MockitoBean
    protected CustomOAuth2UserService customOAuth2UserService;

    @MockitoBean
    protected MenuService menuService;

    @MockitoBean
    protected MenuFindService menuFindService;

    @MockitoBean
    protected StoreFindService storeFindService;

    protected String convertObjectToJson(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
