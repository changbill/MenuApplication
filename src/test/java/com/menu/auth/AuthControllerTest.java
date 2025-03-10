package com.menu.auth;

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

@DisplayName("Auth [Controller Layer] -> AuthController 테스트")
public class AuthControllerTest extends ControllerTest {


    @Test
    @DisplayName("회원가입에 성공한다")
    void success() throws Exception {
        // given
        doReturn()
                .when(ownerAuthService)
                .signup(any());

        // when
        final ParentsSignupRequestDto request = createParentsSignupRequestDto();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BASE_URL)
                .contentType(APPLICATION_JSON)
                .content(convertObjectToJson(request));

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk()
                );
    }
}
