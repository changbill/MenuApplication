package com.menu.auth;

import static com.menu.fixture.OwnerFixture.CHANGHEON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.menu.auth.dto.request.OwnerSignupRequestDto;
import com.menu.common.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("Auth [Controller Layer] -> OwnerAuthController 테스트")
public class OwnerAuthControllerTest extends ControllerTest {
    private static final String BASE_URL = "/api/owner/auth";

    @Test
    @DisplayName("owner 회원가입 성공")
    void signup() throws Exception {
        // given
        doReturn(anyLong())
                .when(ownerAuthService)
                .signup(any());

        // when
        final OwnerSignupRequestDto request = createOwnerSignupRequestDto();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BASE_URL + "/signup")
                .contentType(APPLICATION_JSON)
                .content(convertObjectToJson(request));

        // then
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    @DisplayName("owner 로그인 성공")
    void login() throws Exception {
        doReturn()
    }

    private OwnerSignupRequestDto createOwnerSignupRequestDto() {
        return new OwnerSignupRequestDto(
                CHANGHEON.getName(),
                CHANGHEON.getEmail(),
                CHANGHEON.getSocialType(),
                CHANGHEON.getSocialId(),
                CHANGHEON.getProfileImageUrl(),
                CHANGHEON.getBirthday()
        );
    }
}
