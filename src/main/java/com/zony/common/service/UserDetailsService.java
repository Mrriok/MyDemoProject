
package com.zony.common.service;

import com.zony.common.service.dto.JwtUserDto;
import com.zony.app.service.DataService;
import com.zony.app.service.RoleService;
import com.zony.app.service.UserService;
import lombok.RequiredArgsConstructor;
import com.zony.common.exception.BadRequestException;
import com.zony.common.exception.EntityNotFoundException;
import com.zony.app.service.dto.UserDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;
    private final RoleService roleService;
    private final DataService dataService;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        UserDto user;
        try {
            user = userService.findByName(username);
        } catch (EntityNotFoundException e) {
            // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
            throw new UsernameNotFoundException("", e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("");
        } else {
            if (!user.getEnabled()) {
                throw new BadRequestException("账号未激活");
            }
            return new JwtUserDto(
                    user,
                    dataService.getDeptIds(user),
                    roleService.mapToGrantedAuthorities(user)
            );
        }
    }
}
