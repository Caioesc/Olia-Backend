package olia.backend.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import olia.backend.api.domain.escola.Escola;
import olia.backend.api.domain.escola.EscolaRepository;
import olia.backend.api.domain.governo.Governo;
import olia.backend.api.domain.governo.GovernoRepository;
import olia.backend.api.domain.usuario.Usuario;
import olia.backend.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private GovernoRepository governoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getSubject(tokenJWT);

                UserDetails user = null;

                // === 1. TABELA USUÁRIOS ===
                Usuario usuario = usuarioRepository.findByEmail(subject);
                if (usuario != null) {
                    user = usuario;
                }

                // === 2. TABELA ESCOLAS ===
                if (user == null) {
                    Escola escola = escolaRepository.buscarPorEmailDeAcesso(subject);
                    if (escola != null) {
                        user = escola; // precisa implementar UserDetails!
                    }
                }

                // === 3. TABELA GOVERNO ===
                if (user == null) {
                    Governo governo = governoRepository.findByEmail(subject);
                    if (governo != null) {
                        user = governo; // precisa implementar UserDetails!
                    }
                }

                // === AUTENTICAÇÃO FINAL ===
                if (user != null) {
                    var auth = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                System.out.println("Token inválido ignorado no filtro: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
