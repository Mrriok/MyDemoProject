
package com.zony.common.security;

import cn.hutool.core.util.StrUtil;
import com.zony.common.service.dto.OnlineUserDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.zony.common.config.security.SecurityProperties;
import com.zony.common.service.OnlineUserService;
import com.zony.common.utils.SpringContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

   private final TokenProvider tokenProvider;

   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      String token = resolveToken(httpServletRequest);
      // 对于 Token 为空的不需要去查 Redis
      if(StrUtil.isNotBlank(token)){
         OnlineUserDto onlineUserDto = null;
         SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
         try {
            OnlineUserService onlineUserService = SpringContextHolder.getBean(OnlineUserService.class);
            onlineUserDto = onlineUserService.getOne(properties.getOnlineKey() + token);
         } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
         }
         if (onlineUserDto != null && StringUtils.hasText(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Token 续期
            tokenProvider.checkRenewal(token);
         }
      }
      filterChain.doFilter(servletRequest, servletResponse);
   }

   private String resolveToken(HttpServletRequest request) {
      SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
      String bearerToken = request.getHeader(properties.getHeader());
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
         // 去掉令牌前缀
         return bearerToken.replace(properties.getTokenStartWith(),"");
      }
      return null;
   }
}
