package com.menu.menu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.menu.common.ControllerTest;
import com.menu.common.TokenService;
import com.menu.common.fixture.MemberFixture;
import com.menu.global.exception.BaseException;
import com.menu.member.domain.Role;
import com.menu.menu.dto.MenuRequest;
import com.menu.store.exception.StoreErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.menu.common.TokenService.BEARER_TOKEN;
import static com.menu.common.fixture.MemberFixture.CHANGHEON;
import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Menu [Controller Layer] -> MenuApiController 테스트")
class MenuApiControllerTest extends ControllerTest {
    private static final Long STORE_ID = 1L;
    private static final Long MENU_ID = 1L;

//    private String accessToken = null;
//    private String refreshToken = null;
//
//    @BeforeEach
//    void setUp() {
//        accessToken = jwtProvider.createAccessToken(
//                CHANGHEON.getEmail(),
//                CHANGHEON.getRole().getValue()
//        ).getToken();
//        refreshToken = jwtProvider.createRefreshToken(CHANGHEON.getEmail()).getToken();
//    }

    @Test
    @DisplayName("메뉴 전체조회 [GET /api/menu/{storeId}]")
    void readMenus() throws Exception {
        // given
        when(menuService.readMenu(STORE_ID))
                .thenReturn(new ArrayList<>());

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/menu/{storeId}", STORE_ID)
//                .header(AUTHORIZATION, BEARER_PREFIX + ACCESS_TOKEN)
                ;

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    @DisplayName("메뉴 등록 [POST /api/menu/{storeId}]")
    void uploadMenu() throws Exception {
        // given
        when(menuService.uploadMenu(any(), anyLong(), any(), any(), anyLong()))
                .thenReturn(1L);

        // when
        MockMultipartFile file = getMockMultipartFile();
        MockMultipartFile mockRequest = getMockMultipartMenuRequest();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/api/menu/{storeId}", STORE_ID)
                .file(file)
                .file(mockRequest)
//                .header(AUTHORIZATION, BEARER_TOKEN + accessToken)
                .accept(APPLICATION_JSON);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Nested
    @DisplayName("메뉴 수정 [PATCH /api/menu/{storeId}/{menuId}]")
    class updateMenu {
        @Test
        @DisplayName("다른 사람 메뉴 수정 불가")
        void throwExceptionWhenUserIsNotOwner() throws Exception {
            // given
            verify(menuService, times(1)).updateMenu(any(), anyLong(), anyLong(), any(), any(), anyLong());

            doThrow(BaseException.type(StoreErrorCode.USER_IS_NOT_OWNER))
                    .when(menuService)
                    .updateMenu(any(), anyLong(), anyLong(), any(), any(), anyLong());

            // when
            MockMultipartFile file = getMockMultipartFile();
            MockMultipartFile mockRequest = getMockMultipartMenuRequest();

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart("/api/menu/{storeId}/{menuId}", STORE_ID, MENU_ID)
                    .file(file)
                    .file(mockRequest)
                    //                .header(AUTHORIZATION,BEARER_TOKEN + ACCESS_TOKEN)
                    .accept(APPLICATION_JSON)
                    .with(request -> {
                        request.setMethod("PATCH");
                        return request;
                    });

            // then
            StoreErrorCode errorCode = StoreErrorCode.USER_IS_NOT_OWNER;
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(errorCode.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(errorCode.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(errorCode.getMessage())
                    );
        }

        @Test
        @DisplayName("메뉴 수정 성공")
        void success() throws Exception {
            // given
            doNothing()
                    .when(menuService)
                    .updateMenu(any(), anyLong(), anyLong(), any(), any(), anyLong());

            // when
            MockMultipartFile file = getMockMultipartFile();
            MockMultipartFile mockRequest = getMockMultipartMenuRequest();

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart("/api/menu/{storeId}/{menuId}", STORE_ID, MENU_ID)
                    .file(file)
                    .file(mockRequest)
                    //                .header(AUTHORIZATION,BEARER_TOKEN + ACCESS_TOKEN)
                    .accept(APPLICATION_JSON)
                    .with(request -> {
                        request.setMethod("PATCH");
                        return request;
                    });

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }


    @Nested
    @DisplayName("메뉴 삭제 [DELETE /api/menu/{storeId}/{menuId}]")
    class deleteMenu {
        @Test
        @DisplayName("다른 사람 메뉴 삭제 불가")
        void throwExceptionWhenUserIsNotOwner() throws Exception {
            // given
            doThrow(BaseException.type(StoreErrorCode.USER_IS_NOT_OWNER))
                    .when(menuService)
                    .deleteMenu(any(), anyLong(),anyLong());
            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete("/api/menu/{storeId}/{menuId}", STORE_ID, MENU_ID)
                    //                .header(AUTHORIZATION,BEARER_TOKEN + ACCESS_TOKEN)
                    ;

            // then
            StoreErrorCode errorCode = StoreErrorCode.USER_IS_NOT_OWNER;
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(errorCode.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(errorCode.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(errorCode.getMessage())
                    );
        }

        @Test
        @DisplayName("메뉴 삭제")
        void success() throws Exception {
            // given
            doNothing()
                    .when(menuService)
                    .deleteMenu(any(), anyLong(),anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete("/api/menu/{storeId}/{menuId}", STORE_ID, MENU_ID)
                    //                .header(AUTHORIZATION,BEARER_TOKEN + ACCESS_TOKEN)
                    ;

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }


    private MenuRequest createMenuRequest() {
        return new MenuRequest("new menu", 1000L);
    }

    private static MockMultipartFile getMockMultipartFile() {
        return new MockMultipartFile(
                "image",
                null,
                "image/jpeg",
                new byte[]{}
        );
    }

    private MockMultipartFile getMockMultipartMenuRequest() throws JsonProcessingException {
        return new MockMultipartFile(
                "request",
                null,
                "application/json",
                objectMapper.writeValueAsString(createMenuRequest()).getBytes(StandardCharsets.UTF_8)
        );
    }
}