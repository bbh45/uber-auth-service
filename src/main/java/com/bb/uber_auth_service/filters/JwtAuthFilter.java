package com.bb.uber_auth_service.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.bb.uber_auth_service.services.JwtService;
import com.bb.uber_auth_service.services.UserDetailsServiceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;

    private final RequestMatcher uriMatcher =
            new AntPathRequestMatcher("/api/v1/auth/validate", HttpMethod.GET.name());

    public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        if(request.getCookies() != null){
            for(Cookie cookie: request.getCookies()){
                if(cookie.getName().equals("JwtToken")){
                    token = cookie.getValue();
                }
            }
        }
        if(token == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        System.out.println("Incoming Token: "+ token);
        String email = jwtService.extractEmail(token);
        System.out.println("Email from Token: "+ email);

        if(email != null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if(jwtService.validateToken(token, userDetails.getUsername())){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher matcher = new NegatedRequestMatcher(uriMatcher);
        return matcher.matches(request);
    }
}
