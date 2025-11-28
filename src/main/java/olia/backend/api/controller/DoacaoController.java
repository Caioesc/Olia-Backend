package olia.backend.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import olia.backend.api.domain.doacao.DadosCadastroDoacao;
import olia.backend.api.domain.doacao.DadosListagemDoacao;
import olia.backend.api.domain.doacao.Doacao;
import olia.backend.api.domain.doacao.DoacaoRepository;
import olia.backend.api.domain.escola.EscolaRepository;
import olia.backend.api.domain.usuario.UsuarioRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class DoacaoController {

    @Autowired
    private DoacaoRepository repository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity doar(@RequestBody @Valid DadosCadastroDoacao dados) {
        // 1. Busca a Escola e o Usuário pelo ID que veio do Angular
        var escola = escolaRepository.getReferenceById(dados.idEscola());
        var usuario = usuarioRepository.getReferenceById(dados.idUsuario());

        // 2. Cria a doação usando o construtor que fizemos na Entidade
        var doacao = new Doacao(
                dados.codigo(),
                dados.quantidade(),
                escola,
                usuario);

        repository.save(doacao);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<DadosListagemDoacao>> listarPorUsuario(@PathVariable Long id) {
        var lista = repository.findAllByUsuarioId(id)
                .stream()
                .map(DadosListagemDoacao::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/escola/{id}")
    public ResponseEntity<List<DadosListagemDoacao>> listarPorEscola(@PathVariable Long id) {
        var lista = repository.findAllByEscolaId(id)
                .stream()
                .map(DadosListagemDoacao::new)
                .toList();

        return ResponseEntity.ok(lista);
    }
}
