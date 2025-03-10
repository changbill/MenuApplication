package com.menu.global.security;

import com.menu.user.domain.User;
import com.menu.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserFindService userFindService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User user = userFindService.findByEmail(email);
            UserDetailDto userDetailDto = new UserDetailDto(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole().getAuthority()
            );

        return new CustomUserDetails(userDetailDto);
    }
}
