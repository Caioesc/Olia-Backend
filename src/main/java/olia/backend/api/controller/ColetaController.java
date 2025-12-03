package olia.backend.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import olia.backend.api.domain.coleta.*;
import olia.backend.api.domain.escola.EscolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/coletas")
@CrossOrigin(origins = "http://localhost:4200")
public class ColetaController {

    @Autowired
    private ColetaRepository repository;

    @Autowired
    private EscolaRepository escolaRepository;

    // 1. ESCOLA SOLICITA COLETA
    @PostMapping
    @Transactional
    public ResponseEntity solicitar(@RequestBody @Valid DadosSolicitacaoColeta dados) {
        var escola = escolaRepository.getReferenceById(dados.idEscola());
        var coleta = new Coleta(dados, escola);
        repository.save(coleta);
        return ResponseEntity.ok().build();
    }

    // 2. GOVERNO LISTA TUDO
    @GetMapping
    public ResponseEntity<List<DadosListagemColeta>> listar() {
        var lista = repository.findAll().stream().map(DadosListagemColeta::new).toList();
        return ResponseEntity.ok(lista);
    }

    // 3. GOVERNO ATUALIZA STATUS (Agendar / Concluir)
    @Secured("ROLE_GOVERNO")
    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<Coleta> atualizarStatus(@PathVariable Long id, @RequestBody AtualizacaoStatus dto) {
        // 1. Buscamos a coleta e tratamos o caso de não existir (retorna 404)
        var coleta = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coleta não encontrada"));

        // 2. Pegamos o status de dentro do DTO, sem manipulação de String
        String novoStatus = dto.getStatus();

        // 3. Usamos um switch para deixar a lógica mais limpa
        switch (novoStatus) {
            case "AGENDADA":
                coleta.agendar();
                break;
            case "CONCLUIDA":
                coleta.concluir();
                break;
            default:
                // Opcional: Lançar um erro se o status for inválido
                return ResponseEntity.badRequest().build();
        }

        // 4. Retornamos a coleta atualizada no corpo da resposta (boa prática)
        return ResponseEntity.ok(coleta);
    }
}