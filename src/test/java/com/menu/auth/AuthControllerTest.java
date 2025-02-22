package com.menu.auth;

import static com.menu.common.fixture.TokenFixture.ACCESS_TOKEN;
import static com.menu.common.fixture.TokenFixture.BEARER_PREFIX;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.common.ControllerTest;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("Authorization Controller 테스트")
public class AuthControllerTest extends ControllerTest {

    @Nested
    @DisplayName("Authorization 필요 API")
    class AuthorizationNecessityTests {

        @Test
        @DisplayName("[GET] Authorization Header에 token이 없으면 예외 발생")
        void MenuApiTest() throws Exception {
            final String BASE_URL = "/api/menu/{storeId}";

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL, anyLong());

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
    }
}
