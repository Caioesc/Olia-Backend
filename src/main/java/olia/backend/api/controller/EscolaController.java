package olia.backend.api.controller;

import jakarta.validation.Valid;
import olia.backend.api.domain.coleta.Coleta;
import olia.backend.api.domain.coleta.ColetaRepository;
import olia.backend.api.domain.coleta.StatusColeta;
import olia.backend.api.domain.doacao.DoacaoRepository;
import olia.backend.api.domain.doacao.StatusDoacao;
import olia.backend.api.domain.escola.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    @Autowired
    private ColetaRepository coletaRepository;

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

            // Recalcula a % para o card do mapa também
            double maximo = switch (escola.getCapacidade()) {
                case PEQUENA -> 50.0;
                case MEDIA -> 100.0;
                case GRANDE -> 200.0;
            };

            double atual = doacaoRepository.somarTotalPorEscolaEStatus(escola.getId(), StatusDoacao.CONCLUIDO);
            double porcentagem = (atual / maximo) * 100;
            if (porcentagem > 100.0)
                porcentagem = 100.0;

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
                    porcentagem // Manda a % atualizada para o mapa
            );
        });

        return ResponseEntity.ok(page);
    }

@GetMapping("/dashboard/{id}")
    public ResponseEntity detalharDashboard(@PathVariable Long id) {
        var escola = repository.getReferenceById(id);

        // 1. ENTRADA: Total de doações recebidas (Para pontos e histórico)
        Double totalEntrada = doacaoRepository.somarTotalPorEscolaEStatus(id, StatusDoacao.CONCLUIDO);
        if (totalEntrada == null) totalEntrada = 0.0;

        // 2. SAÍDA: Total coletado pelo governo (Para esvaziar o tanque)
        Double totalSaida = coletaRepository.somarTotalColetadoPorEscola(id, StatusColeta.CONCLUIDA); 
        if (totalSaida == null) totalSaida = 0.0;

        // 3. OCUPAÇÃO ATUAL: O que tem no tanque agora
        Double ocupacaoAtual = totalEntrada - totalSaida;
        if (ocupacaoAtual < 0) ocupacaoAtual = 0.0; // Segurança contra negativos

        // 4. Pontos continuam baseados no histórico total (escola não perde pontos ao esvaziar)
        Integer pontosAtuais = (int) (totalEntrada * 10);
        
        // 5. Coletas Realizadas
        Integer coletasRealizadas = doacaoRepository.contarPorEscolaEStatus(id, StatusDoacao.CONCLUIDO);

        // 6. Capacidade Máxima
        double capacidadeLitros = switch (escola.getCapacidade()) {
            case PEQUENA -> 50.0;
            case MEDIA -> 100.0;
            case GRANDE -> 200.0;
        };

        // 7. Porcentagem do Tanque (Baseada na Ocupação Atual)
        Double porcentagemTanque = (ocupacaoAtual / capacidadeLitros) * 100;
        if (porcentagemTanque > 100.0) porcentagemTanque = 100.0;

        // 8. Meta Dinâmica
        Integer metaPontos = (int) (capacidadeLitros * 10);
        if (escola.getMetaAtual() != null && escola.getMetaAtual() > metaPontos) {
            metaPontos = escola.getMetaAtual();
        }

        var dados = new DadosDashboardEscola(
                escola.getNome(),
                totalEntrada,      // Mostra o total histórico acumulado
                coletasRealizadas,
                pontosAtuais,
                capacidadeLitros,
                ocupacaoAtual,     // <--- Mostra quanto tem AGORA
                porcentagemTanque, // <--- Barra baseada no atual
                metaPontos
        );

        return ResponseEntity.ok(dados);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<DadosRanking>> listarRanking() {
        // Busca a lista crua do banco (ID, Nome, TotalLitros) ordenada
        List<Object[]> resultadoBruto = repository.buscarRankingBruto();

        List<DadosRanking> ranking = new ArrayList<>();
        int posicao = 1;

        for (Object[] linha : resultadoBruto) {
            // O Java retorna Object[], precisamos converter
            // String nome = (String) linha[1];
            // Double litros = (Double) linha[2];

            String nome = (String) linha[1];
            Double litros = (Double) linha[2];
            Integer pontos = (int) (litros * 10);

            // Adiciona na lista final
            // O frontend vai decidir quem é "Minha Escola" baseado no login
            ranking.add(new DadosRanking(posicao, nome, pontos, litros, false));

            posicao++;
        }

        return ResponseEntity.ok(ranking);
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
