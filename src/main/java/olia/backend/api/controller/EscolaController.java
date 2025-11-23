package olia.backend.api.controller;

import jakarta.validation.Valid;
import olia.backend.api.domain.escola.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/escolas")
public class EscolaController {

    @Autowired
    private EscolaRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroEscola dados, UriComponentsBuilder uriBuilder){
        var escola = new Escola(dados);
        repository.save(escola);

        var uri = uriBuilder.path("/escolas/{id}").buildAndExpand(escola.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoEscola(escola));
    }

    @GetMapping //Não precisa do @Transactional, pois é uma operação de leitura, não irá alterar registros no banco
    public ResponseEntity<Page<DadosListagemEscola>> listar(@PageableDefault(size = 10, sort ={"nome"}) Pageable paginacao){ //Page informa, além da lista, os dados da paginação, mas para listar tudo, usar List
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemEscola::new); //Usando page não se faz necessário mais o stream() e o toList()
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoEscola dados){
        var escola = repository.getReferenceById(dados.id());
        escola.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoEscola(escola));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        //repository.deleteById(id); ---> Essa linha faz a exclusão física do dado no banco, mas para essa aplicação, quero apenas fazer uma exclusão lógica, deixar a escola como inativa.
        var escola = repository.getReferenceById(id);
        escola.excluir();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var escola = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoEscola(escola));
    }
}

