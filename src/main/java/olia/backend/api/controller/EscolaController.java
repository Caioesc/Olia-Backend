package olia.backend.api.controller;

import jakarta.validation.Valid;
import olia.backend.api.escola.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/escolas")
public class EscolaController {

    @Autowired
    private EscolaRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroEscola dados){
        repository.save(new Escola(dados));
    }

    @GetMapping //Não precisa do @Transactional, pois é uma operação de leitura, não irá alterar registros no banco
    public Page<DadosListagemEscola> listar(@PageableDefault(size = 10, sort ={"nome"}) Pageable paginacao){ //Page informa, além da lista, os dados da paginação, mas para listar tudo, usar List
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemEscola::new); //Usando page não se faz necessário mais o stream() e o toList()
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoEscola dados){
        var escola = repository.getReferenceById(dados.id());
        escola.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        //repository.deleteById(id); ---> Essa linha faz a exclusão física do dado no banco, mas para essa aplicação, quero apenas fazer uma exclusão lógica, deixar a escola como inativa.
        var escola = repository.getReferenceById(id);
        escola.excluir();
    }
}

