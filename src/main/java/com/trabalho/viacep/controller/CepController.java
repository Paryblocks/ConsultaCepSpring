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
public class CepController {

    @Autowired
    private CepService service;
@GetMapping("/{cep}")
    public Cep buscar(@PathVariable String cep, @RequestParam(required = false) Long usuarioId) {
        Cep resultado = service.buscar(cep, usuarioId);
        
        if (resultado != null) {
            resultado.setUsuario(null);
        }
        
        return resultado;
    }

    @GetMapping("/{uf}/{cidade}/{logradouro}")
    public List<Cep> buscarPorEndereco(
            @PathVariable String uf,
            @PathVariable String cidade,
            @PathVariable String logradouro,
            @RequestParam(required = false) Long usuarioId) {
        
        List<Cep> listaResultados = service.buscarPorEndereco(uf, cidade, logradouro, usuarioId);
        
        if (listaResultados != null) {
            for (Cep c : listaResultados) {
                c.setUsuario(null);
            }
        }
        
        return listaResultados;
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

    @GetMapping("/relatorio/{usuarioId}")
    public ResponseEntity<InputStreamResource> gerarRelatorio(
            @PathVariable Long usuarioId) {

        ByteArrayInputStream csv = service.gerarRelatorioCsv(usuarioId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=relatorio-ceps.csv"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csv));
    }
}
