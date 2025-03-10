package com.menu.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.menu.auth.controller.owner.OwnerAuthController;
import com.menu.auth.controller.owner.OwnerTokenReissueController;
import com.menu.auth.controller.user.UserAuthController;
import com.menu.auth.controller.user.UserTokenReissueController;
import com.menu.auth.service.TokenService;
import com.menu.auth.service.owner.OwnerAuthService;
import com.menu.auth.service.owner.OwnerTokenReissueService;
import com.menu.auth.service.user.UserAuthService;
import com.menu.auth.utils.JwtProvider;
import com.menu.file.service.FileService;
import com.menu.global.config.SecurityConfig;
import com.menu.global.security.JwtAccessDeniedHandler;
import com.menu.global.security.JwtAuthenticationEntryPoint;
import com.menu.menu.controller.MenuApiController;
import com.menu.menu.service.MenuFindService;
import com.menu.menu.service.MenuService;
import com.menu.store.service.StoreFindService;
import com.menu.user.service.UserFindService;
import com.menu.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@ImportAutoConfiguration(SecurityConfig.class)  // 전체 애플리케이션 구성 대신 특정 자동 구성만 선택적으로 적용
@WebMvcTest({
        OwnerAuthController.class,
        UserAuthController.class,
        OwnerTokenReissueController.class,
        UserTokenReissueController.class,
        MenuApiController.class
})
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @MockitoBean
    protected OwnerAuthService ownerAuthService;

    @MockitoBean
    protected UserAuthService userAuthService;

    @MockitoBean
    protected OwnerTokenReissueService ownerTokenReissueService;

    @MockitoBean
    protected UserTokenReissueController userTokenReissueController;

    @MockitoBean
    protected JwtProvider jwtProvider;

    @MockitoBean
    protected JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockitoBean
    protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockitoBean
    protected TokenService tokenService;

    @MockitoBean
    protected UserFindService userFindService;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected FileService fileService;

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
