package com.menu.menu.controller;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.common.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.menu.common.fixture.TokenFixture.ACCESS_TOKEN;
import static com.menu.common.fixture.TokenFixture.BEARER_PREFIX;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@DisplayName("Menu [Controller Layer] -> MenuApiController 테스트")
class MenuApiControllerTest extends ControllerTest {

    @Nested
    @DisplayName("메뉴 전체조회 API 테스트 [GET /api/menu/{storeId}]")
    class readMenu {
        private static final String BASE_URL = "/api/menu/{storeId}";
        private static final Long STORE_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 token이 없으면 예외 발생")
        void throwExceptionByInvalidPermission() throws Exception {
            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL, STORE_ID);

            // then
            AuthErrorCode expectedError = AuthErrorCode.INVALID_PERMISSION;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isForbidden(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    );
        }

        @Test
        @DisplayName("readMenu")
        void readMenu() throws Exception {
            // given
            when(menuService.readMenu(STORE_ID)).thenReturn();

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, STORE_ID)
                    .header(AUTHORIZATION, BEARER_PREFIX + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(
                            status().isOk()
                    );
        }
    }
}