package com.trabalho.viacep.controller;

import com.trabalho.viacep.model.Cep;
import com.trabalho.viacep.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cep")
public class CepController {

    @Autowired
    private CepService service;

    @GetMapping("/{cep}")
    public Cep buscar(@PathVariable String cep, @RequestParam(required = false) Long usuarioId) {
        return service.buscar(cep, usuarioId);
    }

    @GetMapping("/{uf}/{cidade}/{logradouro}")
    public List<Cep> buscarPorEndereco(
            @PathVariable String uf,
            @PathVariable String cidade,
            @PathVariable String logradouro,
            @RequestParam(required = false) Long usuarioId) {
        return service.buscarPorEndereco(uf, cidade, logradouro, usuarioId);
    }

    @GetMapping("/mais-consultado")
    public Cep obterMaisConsultadoGlobal() {
        return service.obterCepMaisConsultadoGlobal();
    }

    @GetMapping("/total-acessos/{usuarioId}")
    public int obterTotalAcessos(@PathVariable Long usuarioId) {
        return service.obterTotalAcessos(usuarioId);
    }

    @GetMapping("/top15/{usuarioId}")
    public List<String> obterTop15MaisPesquisados(@PathVariable Long usuarioId) {
        return service.obterTop15MaisPesquisadosDoUsuario(usuarioId);
    }

    @DeleteMapping("/historico/{usuarioId}")
    public ResponseEntity<Void> limparHistorico(@PathVariable Long usuarioId) {
        service.limparHistoricoUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
