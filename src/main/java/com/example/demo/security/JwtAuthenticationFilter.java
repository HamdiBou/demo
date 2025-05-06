package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.CustomUserDetailsService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private List<String> excludePatterns = Arrays.asList("/api/users/register", "/api/users/login");

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Skip CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = request.getServletPath();
        return excludePatterns.stream().anyMatch(pattern -> path.startsWith(pattern));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
            throws ServletException, IOException {
       
        try {
            String jwt = getJwtFromRequest(request);
           
            System.out.println("Request path: " + request.getServletPath());
            System.out.println("JWT token exists: " + (jwt != null && !jwt.isEmpty()));
           
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String userId = tokenProvider.getUserIdFromToken(jwt);
                System.out.println("Token is valid, extracted userId: " + userId);
                
                // Create an Authentication object and set it in the SecurityContext
                UserDetails userDetails = userDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                    );
                
                // Set the authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // Store userId in request for controllers to use
                request.setAttribute("userId", userId);
            } else if (!shouldNotFilter(request)) {
                System.out.println("Invalid or missing token for protected endpoint");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception ex) {
            System.out.println("Exception in JWT filter: " + ex.getMessage());
            logger.error("Could not set user authentication in security context", ex);
            SecurityContextHolder.clearContext();  // Clear context on error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
       
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
       
        System.out.println("Authorization header: " + bearerToken);
       
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
       
        return null;
    }
}