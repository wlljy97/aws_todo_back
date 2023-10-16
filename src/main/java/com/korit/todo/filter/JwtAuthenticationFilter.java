package com.korit.todo.filter;

import com.korit.todo.entity.Authority;
import com.korit.todo.entity.PrincipalUser;
import com.korit.todo.entity.Role;
import com.korit.todo.entity.User;
import com.korit.todo.repository.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.security.sasl.AuthenticationException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final UserMapper userMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        // Bearer 토큰
        System.out.println("authorization: " + authorization);

        String reqUri = httpServletRequest.getRequestURI();

        if(reqUri.startsWith("/auth") && !reqUri.startsWith("/authenticated")) {
            chain.doFilter(request, response);
            return;
        }

        if(!StringUtils.hasText(authorization)){
            chain.doFilter(request, response);
            return;
        }
        String accessToken = authorization.substring("Bearer ".length());
        Key kye = Keys.hmacShaKeyFor(Decoders.BASE64.decode("IgwBE5D+D+639nDdSwWj/Dvil3XPM2r0UXemY0+jPklLsjfA2PRZf7SvG47lQOAWSndSz4BwxfP7kXwb/0RRmQ=="));

        System.out.println("accessToken: " + accessToken);

        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(kye)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch(Exception e) {
            chain.doFilter(request, response);
            return;
        }

        String username = claims.get("username", String.class);
//        System.out.println(claims.get("auth", List.class));
        List<Object> authList = claims.get("auth", List.class);

//        User user = userMapper.findUserByEmail(username);

        List<Authority> authorities = new ArrayList<>();

        authList.forEach(auth -> {
            Role role = new Role();
            role.setRoleName(((Map<String, String>) auth).get("authority"));
            Authority authority = new Authority();
            authority.setRole(role);
            authorities.add(authority);
        });

        User user = User.builder()
                .email(username)
                .authorities(authorities)
                .build();
        System.out.println(user);

        PrincipalUser principalUser = new PrincipalUser(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}











