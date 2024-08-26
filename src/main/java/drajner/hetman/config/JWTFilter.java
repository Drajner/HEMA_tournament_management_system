package drajner.hetman.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import drajner.hetman.entities.UserEntity;
import drajner.hetman.utils.TokenUtils;
import drajner.hetman.services.UserDetailsServiceImpl;
import drajner.hetman.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String header = request.getHeader("Authorization");
            String token = null;
            String username = null;
            if(header != null && header.startsWith("Bearer ")){
                token = header.substring(7);
                username = TokenUtils.getUsername(token);
            }
            if(token == null){
                filterChain.doFilter(request, response);
                return;
            }
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserEntity userEntity = userService.searchForUser(username);
                if(TokenUtils.validate(token, userEntity)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEntity, null, getAuthorities(userEntity));
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (AccessDeniedException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private List<GrantedAuthority> getAuthorities(UserEntity user) {
        var type = user.getUserStatus().toString();
        var list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(type));
        return list;
    }
}
