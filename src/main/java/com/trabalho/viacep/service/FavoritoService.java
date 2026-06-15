package com.trabalho.viacep.service;

import com.trabalho.viacep.model.Categoria;
import com.trabalho.viacep.model.Favorito;
import com.trabalho.viacep.model.Usuario;
import com.trabalho.viacep.repository.CategoriaRepository;
import com.trabalho.viacep.repository.FavoritoRepository;
import com.trabalho.viacep.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoritoService {

    @Autowired private FavoritoRepository favoritoRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public Favorito salvarFavorito(String cep, String nome, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Favorito favorito = new Favorito();
        favorito.setCep(cep);
        favorito.setNome(nome);
        favorito.setUsuario(usuario);

        return favoritoRepository.save(favorito);
    }

    public List<Favorito> listarFavoritos(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }

    public void excluirFavorito(String cep, Long usuarioId) {
        favoritoRepository.deleteByCepAndUsuarioId(cep, usuarioId);
    }

    public List<Favorito> listarPorCategoria(Long categoriaId, Long usuarioId) {
        return favoritoRepository.findByCategoriasIdAndUsuarioId(categoriaId, usuarioId);
    }

    public Categoria salvarCategoria(String nome, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setUsuario(usuario);

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategorias(Long usuarioId) {
        return categoriaRepository.findByUsuarioId(usuarioId);
    }

    public void vincular(Long favoritoId, Long categoriaId, Long usuarioId) {
        Favorito fav = favoritoRepository.findById(favoritoId)
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado"));

        Categoria cat = categoriaRepository.findByIdAndUsuarioId(categoriaId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        fav.getCategorias().add(cat);

        favoritoRepository.save(fav);
    }
}