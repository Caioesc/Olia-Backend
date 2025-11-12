package olia.backend.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import olia.backend.api.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    @Transactional        //@Valid faz o Spring se integrar com o validation e fazer a verificação desse DTO
    public void cadastrar(@RequestBody @Valid DadosCadastroUsuario dados){
        repository.save(new Usuario(dados));
    }

    @GetMapping //Não precisa do @Transactional, pois é uma operação de leitura, não irá alterar registros no banco
    public Page<DadosListagemUsuario> listar(@PageableDefault(size = 10, sort ={"nome"}) Pageable paginacao){ //Page informa, além da lista, os dados da paginação, mas para listar tudo, usar List
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new); //Usando page não se faz necessário mais o stream() e o toList()
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados){
        var usuario = repository.getReferenceById(dados.id());
        usuario.atualizarInformacoes(dados);
    }
}
