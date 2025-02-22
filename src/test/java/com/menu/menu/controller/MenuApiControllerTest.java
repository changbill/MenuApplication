package com.menu.menu.controller;

import com.menu.common.ControllerTest;
import com.menu.menu.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Menu [Controller Layer] -> MenuApiController 테스트")
class MenuApiControllerTest extends ControllerTest {

    @Nested
    @DisplayName("메뉴 전체조회 API 테스트 [GET /api/menu/{storeId}]")
    class readMenu {
        private static final String BASE_URL = "/api/menu/{storeId}";
        private static final Long STORE_ID = 1L;

        @Test
        @DisplayName("readMenu")
        void readMenu() throws Exception {
            // given
            when(menuService.readMenu(STORE_ID))
                    .thenReturn(new ArrayList<>());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, STORE_ID)
//                    .header(AUTHORIZATION, BEARER_PREFIX + ACCESS_TOKEN)
                    ;

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(
                            status().isOk()
                    );
        }
    }

    @Test
    @DisplayName("메뉴 등록 성공")
    void successUploadMenu() throws Exception {
        // given
        when(menuService.uploadMenu(anyLong(), any(), any(), anyLong()))
                .thenReturn(1L);

        // when
        MockMultipartFile file = new MockMultipartFile(
                "file", null, "multipart/form-data", new byte[]{}
        );
        MockMultipartFile mockRequest = new MockMultipartFile(
                "request",
                null,
                "application/json",
                objectMapper.writeValueAsString(createMenuRequest()).getBytes(StandardCharsets.UTF_8)
        );

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/api/menu/{storeId}")
                .file(file)
                .file(mockRequest)
                .accept(APPLICATION_JSON);//                .header(AUTHORIZATION,BEARER_TOKEN + ACCESS_TOKEN)

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 업데이트 성공")
    void successUpdateMenu() throws Exception {
        // given
        when(menuService.uploadMenu(anyLong(), any(), any(), any()))
                .thenReturn(1L);

        // when
        MockMultipartFile file = new MockMultipartFile(
                "file", null, "multipart/form-data", new byte[]{}
        );
        MockMultipartFile mockRequest = new MockMultipartFile(
                "request", null, "application/json", objectMapper.writeValueAsString(createMenuRequest()).getBytes(StandardCharsets.UTF_8)
        );

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/api/menu/{storeId}")
                .file(file)
                .file(mockRequest)
                .accept(APPLICATION_JSON);//                .header(AUTHORIZATION,BEARER_TOKEN + ACCESS_TOKEN)

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    private MenuRequest createMenuRequest() {
        return new MenuRequest("new menu", 1000L);
    }
}