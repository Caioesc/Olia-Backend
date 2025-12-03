package olia.backend.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import olia.backend.api.domain.doacao.DoacaoRepository;
import olia.backend.api.domain.doacao.StatusDoacao;
import olia.backend.api.domain.escola.EscolaRepository;
import olia.backend.api.domain.recompensa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recompensas")
@CrossOrigin(origins = "http://localhost:4200")
public class RecompensaController {

    @Autowired
    private RecompensaRepository repository;
    @Autowired
    private SolicitacaoRecompensaRepository solicitacaoRepository;
    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroRecompensa dados) {
        repository.save(new Recompensa(dados));
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity editar(@RequestBody @Valid DadosCadastroRecompensa dados) {
        var recompensa = repository.getReferenceById(dados.id());
        recompensa.atualizar(dados);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Recompensa>> listarCatalogo() {
        return ResponseEntity.ok(repository.findAllByAtivoTrue());
    }

    @PostMapping("/solicitar")
    @Transactional
    public ResponseEntity solicitar(@RequestBody @Valid DadosSolicitarRecompensa dados) {
        var escola = escolaRepository.getReferenceById(dados.idEscola());
        var recompensa = repository.getReferenceById(dados.idRecompensa());

        // 1. Calcula Total Ganho (1L = 10 pts)
        Double litrosConfirmados = doacaoRepository.somarTotalPorEscolaEStatus(dados.idEscola(),
                StatusDoacao.CONCLUIDO);
        if (litrosConfirmados == null)
            litrosConfirmados = 0.0;
        int pontosGanhos = (int) (litrosConfirmados * 10);

        // 2. Calcula Total Gasto
        Integer pontosGastos = solicitacaoRepository.somarPontosGastos(dados.idEscola());

        // 3. Saldo Atual
        int saldo = pontosGanhos - pontosGastos;

        // 4. Verificação
        if (saldo < recompensa.getCusto()) {
            return ResponseEntity.badRequest().body("Saldo insuficiente! Você tem " + saldo + " pontos.");
        }

        solicitacaoRepository.save(new SolicitacaoRecompensa(escola, recompensa));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/solicitacoes")
    public ResponseEntity<List<DadosListagemSolicitacao>> listarSolicitacoes() {
        var lista = solicitacaoRepository.findAllByOrderByStatusAscDataDesc()
                .stream().map(DadosListagemSolicitacao::new).toList();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/solicitacoes/{id}/aprovar")
    @Transactional
    public ResponseEntity aprovar(@PathVariable Long id) {
        var solicitacao = solicitacaoRepository.getReferenceById(id);
        solicitacao.aprovar();
        // Aqui você deve descontar os pontos da escola se ainda não tiver feito
        return ResponseEntity.ok().build();
    }

    @PutMapping("/solicitacoes/{id}/negar")
    @Transactional
    public ResponseEntity negar(@PathVariable Long id) {
        var solicitacao = solicitacaoRepository.getReferenceById(id);
        solicitacao.negar();
        return ResponseEntity.ok().build();
    }
}
