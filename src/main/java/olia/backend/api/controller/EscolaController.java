package olia.backend.api.controller;

import jakarta.validation.Valid;
import olia.backend.api.domain.doacao.DoacaoRepository;
import olia.backend.api.domain.escola.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/escolas")
@CrossOrigin(origins = "*")
public class EscolaController {

    @Autowired
    private EscolaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroEscola dados, UriComponentsBuilder uriBuilder) {
        var escola = new Escola(dados);
        escola.criptografarSenha(passwordEncoder);
        repository.save(escola);

        var uri = uriBuilder.path("/escolas/{id}").buildAndExpand(escola.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoEscola(escola));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemEscola>> listar(
            @PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {

        var page = repository.findAllByAtivoTrue(paginacao).map(escola -> {

            // Lógica da Capacidade
            double maximo = switch (escola.getCapacidade()) {
                case PEQUENA -> 50.0;
                case MEDIA -> 100.0; 
                case GRANDE -> 200.0;
            };

            // Busca total doado
            double atual = doacaoRepository.totalDoadoPorEscola(escola.getId());

            // Calcula %
            double porcentagem = (atual / maximo) * 100;

            // DTO manualmente
            return new DadosListagemEscola(
                    escola.getId(),
                    escola.getNome(),
                    escola.getNome_responsavel(),
                    escola.getEmail(),
                    escola.getTelefone(),
                    escola.getCnpj(),
                    escola.getCodigo_inep(),
                    escola.getEndereco(),
                    escola.getHorario(),
                    porcentagem // Passa a variável calculada
            );
        });

        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoEscola dados) {
        var escola = repository.getReferenceById(dados.id());
        escola.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoEscola(escola));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        // repository.deleteById(id); ---> Essa linha faz a exclusão física do dado no
        // banco, mas para essa aplicação, quero apenas fazer uma exclusão lógica,
        // deixar a escola como inativa.
        var escola = repository.getReferenceById(id);
        escola.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var escola = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoEscola(escola));
    }
}
