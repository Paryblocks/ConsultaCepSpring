package com.trabalho.viacep.controller;

import com.trabalho.viacep.model.Cep;
import com.trabalho.viacep.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/cep")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class CepController {

    @Autowired
    private CepService service;

    @GetMapping("/{cep}")
    public ResponseEntity<Cep> buscar(@PathVariable String cep, @RequestParam(required = false) Long usuarioId) {
        Cep resultado = service.buscar(cep, usuarioId);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        resultado.setUsuario(null);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{uf}/{cidade}/{logradouro}")
    public ResponseEntity<List<Cep>> buscarPorEndereco(
            @PathVariable String uf,
            @PathVariable String cidade,
            @PathVariable String logradouro,
            @RequestParam(required = false) Long usuarioId) {

        List<Cep> resultados = service.buscarPorEndereco(uf, cidade, logradouro, usuarioId);

        if (resultados == null || resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (Cep c : resultados) {
            c.setUsuario(null);
        }

        return ResponseEntity.ok(resultados);
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

    @GetMapping("/historico/{usuarioId}")
    public List<Cep> obterHistoricoDoUsuario(@PathVariable Long usuarioId) {
        return service.mostrarHistorico(usuarioId);
    }

    @DeleteMapping("/historico/delete/{usuarioId}")
    public ResponseEntity<Void> limparHistorico(@PathVariable Long usuarioId) {
        service.limparHistoricoUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
   
    @DeleteMapping("/historico/{id}")
    public ResponseEntity<Void> deletarUmItem(@PathVariable Long id, @RequestParam Long usuarioId) {
        service.excluirCepDoHistorico(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorio/{usuarioId}")
    public ResponseEntity<InputStreamResource> gerarRelatorio(
            @PathVariable Long usuarioId) {

        ByteArrayInputStream csv = service.gerarRelatorioCsv(usuarioId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=relatorio-ceps.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csv));
    }
}
