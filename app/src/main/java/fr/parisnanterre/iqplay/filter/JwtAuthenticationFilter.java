package fr.parisnanterre.iqplay.filter;

import fr.parisnanterre.iqplay.provider.JwtProvider;
import fr.parisnanterre.iqplay.service.JwtBlacklistService;
import fr.parisnanterre.iqplay.service.PlayerDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PlayerDetailsService playerDetailsService;
    private final JwtBlacklistService jwtBlacklistService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, PlayerDetailsService playerDetailsService, JwtBlacklistService jwtBlacklistService) {
        this.jwtProvider = jwtProvider;
        this.playerDetailsService = playerDetailsService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;

        // 1. Récupérer le token depuis l'en-tête Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            System.out.println("Token reçu dans Authorization Header : " + token);
        }

        // 2. Récupérer le token depuis le cookie si Authorization est absent
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("Token extrait du cookie : " + token);
                    break;
                }
            }
        }

        // 3. Valider le token et configurer l'authentification
        if (token != null) {
            if (jwtBlacklistService.isTokenBlacklisted(token)) {
                System.out.println("Token est blacklisté");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is invalidated");
                return;
            }

            if (jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);
                System.out.println("Utilisateur authentifié via JWT : " + username);

                UserDetails userDetails = playerDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                System.out.println("Token invalide !");
            }
        } else {
            System.out.println("Aucun token trouvé dans Authorization Header ni dans les cookies");
        }

        filterChain.doFilter(request, response);
    }



}
