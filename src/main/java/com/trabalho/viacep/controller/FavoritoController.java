package com.trabalho.viacep.controller;

import com.trabalho.viacep.model.Categoria;
import com.trabalho.viacep.model.Favorito;
import com.trabalho.viacep.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService service;

    @PostMapping
    public Favorito favoritar(@RequestParam String cep, @RequestParam String nome, @RequestParam Long usuarioId) {
        return service.salvarFavorito(cep, nome, usuarioId);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Favorito> listarFavoritos(@PathVariable Long usuarioId) {
        return service.listarFavoritos(usuarioId);
    }

    @DeleteMapping
    public ResponseEntity<Void> removerFavorito(@RequestParam String cep, @RequestParam Long usuarioId) {
        service.excluirFavorito(cep, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoriaId}/usuario/{usuarioId}")
    public List<Favorito> listarPorCategoria(@PathVariable Long categoriaId, @PathVariable Long usuarioId) {
        return service.listarPorCategoria(categoriaId, usuarioId);
    }

    @PostMapping("/categorias")
    public Categoria criarCategoria(@RequestParam String nome, @RequestParam Long usuarioId) {
        return service.salvarCategoria(nome, usuarioId);
    }

    @GetMapping("/categorias/usuario/{usuarioId}")
    public List<Categoria> listarCategorias(@PathVariable Long usuarioId) {
        return service.listarCategorias(usuarioId);
    }

    @PostMapping("/vincular")
    public ResponseEntity<Void> vincularCategoria(@RequestParam Long favoritoId, @RequestParam Long categoriaId, @RequestParam Long usuarioId) {
        service.vincular(favoritoId, categoriaId, usuarioId);
        return ResponseEntity.ok().build();
    }
}