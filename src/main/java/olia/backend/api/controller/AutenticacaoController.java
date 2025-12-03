package olia.backend.api.controller;

import jakarta.validation.Valid;
import olia.backend.api.domain.escola.EscolaRepository;
import olia.backend.api.domain.governo.Governo;
import olia.backend.api.domain.governo.GovernoRepository;
import olia.backend.api.domain.usuario.DadosAutenticacao;
import olia.backend.api.domain.usuario.Usuario;
import olia.backend.api.infra.security.DadosTokenJWT;
import olia.backend.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired // Faz o spring injetar o parâmetro, não somos nós que devemos estanciar
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GovernoRepository governoRepository;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha()); // Recebe nosso
                                                                                                         // DTO com
                                                                                                         // email e
                                                                                                         // senha e cria
                                                                                                         // um DTO do
                                                                                                         // Spring
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        var usuarioLogado = (Usuario) authentication.getPrincipal();

        // Envia o token e o nome
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, usuarioLogado.getNome(), usuarioLogado.getId()));
    }

    @PostMapping("/escola")
    public ResponseEntity loginEscola(@RequestBody @Valid DadosAutenticacao dados) {
        // 1. Busca a escola pelo email de acesso
        var escola = escolaRepository.buscarPorEmailDeAcesso(dados.email());

        // 2. Verifica se existe e se a senha bate
        if (escola != null && passwordEncoder.matches(dados.senha(), escola.getPassword())) {

            // 3. Gera o token
            var token = tokenService.gerarToken(escola);

            // 4. Retorna Token, Nome e ID
            return ResponseEntity.ok(new DadosTokenJWT(token, escola.getNome(), escola.getId()));
        }

        return ResponseEntity.badRequest().body("Login inválido");
    }

    @PostMapping("/governo")
    public ResponseEntity loginGoverno(@RequestBody @Valid DadosAutenticacao dados) {
        var governo = (Governo) governoRepository.findByEmail(dados.email());

        if (governo != null && passwordEncoder.matches(dados.senha(), governo.getPassword())) {
            var token = tokenService.gerarToken(governo);
            // Adicione o ID no retorno também
            return ResponseEntity.ok(new DadosTokenJWT(token, governo.getNome(), governo.getId()));
        }

        return ResponseEntity.badRequest().body("Login inválido");
    }
}
