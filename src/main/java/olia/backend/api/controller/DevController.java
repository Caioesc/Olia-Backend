package olia.backend.api.controller;

import olia.backend.api.domain.governo.Governo;
import olia.backend.api.domain.governo.GovernoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev-fix")
@CrossOrigin(origins = "*") // só pra garantir
public class DevController {

    @Autowired
    private GovernoRepository governoRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/senha")
    public String fixarSenha() {
        Governo gov = governoRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Governo id=1 não encontrado"));

        gov.setSenha("$2a$12$fttOkZNhRy2Z2TiUeXae6.FlIOBDhq4ArOTjNqU/pPkeLV/EHSdyC");
        governoRepository.save(gov);

        return "Senha do governo atualizada com sucesso.";
    }
}
