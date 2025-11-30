package olia.backend.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import olia.backend.api.domain.escola.EscolaRepository;
import olia.backend.api.domain.usuario.UsuarioRepository; // Importe do domain
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Importe UserDetails
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                // Tenta validar o token. Se falhar, vai para o catch e segue o fluxo sem logar.
                var subject = tokenService.getSubject(tokenJWT);

                UserDetails user = usuarioRepository.findByEmail(subject);

                if (user == null) {
                    user = escolaRepository.buscarPorEmailDeAcesso(subject);
                }

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token inválido ou expirado?
                // Não faz nada, apenas não autentica.
                // O Spring Security vai decidir lá na frente se bloqueia (403) ou deixa passar (se for login).
                System.out.println("Token inválido ignorado no filtro: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}