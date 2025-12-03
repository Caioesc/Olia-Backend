package olia.backend.api.controller;

import jakarta.validation.Valid;
import olia.backend.api.domain.doacao.DoacaoRepository;
import olia.backend.api.domain.escola.EscolaRepository;
import olia.backend.api.domain.governo.*;
import olia.backend.api.domain.usuario.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/governo")
public class GovernoController {

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GovernoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroGoverno dados, UriComponentsBuilder uriBuilder) {
        var governo = new Governo(dados);
        governo.criptografarSenha(passwordEncoder);
        repository.save(governo);

        var uri = uriBuilder.path("/governo/{id}").buildAndExpand(governo.getId()).toUri();

        // Você pode criar um DTO DadosDetalhamentoGoverno se quiser retornar algo
        // bonito
        return ResponseEntity.created(uri).body(governo);
    }

    @GetMapping("/impacto")
    public ResponseEntity<DadosImpactoGlobal> dadosImpacto() {
        // 1. Busca os totais e trata o caso de ser null
        Double totalOleo = doacaoRepository.somarTotalGlobal();
        if (totalOleo == null) {
            totalOleo = 0.0; //Garante que não seja null
        }

        Long totalEscolas = escolaRepository.count();
        Long totalUsuarios = usuarioRepository.count();

        // 2. Calcula Sabão (Regra: 5L de óleo = 1 Sabão)
        // Usamos Math.floor para garantir que estamos pegando a parte inteira
        int totalSabao = (int) Math.floor(totalOleo / 3);

        // 3. Retorna o DTO
        var dados = new DadosImpactoGlobal(
                totalOleo,
                totalSabao,
                totalEscolas,
                totalUsuarios);

        return ResponseEntity.ok(dados);
    }
}
