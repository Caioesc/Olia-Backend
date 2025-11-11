package olia.backend.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import olia.backend.api.usuario.DadosCadastroUsuario;
import olia.backend.api.usuario.Usuario;
import olia.backend.api.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
