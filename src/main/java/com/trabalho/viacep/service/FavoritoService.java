package com.trabalho.viacep.service;

import com.trabalho.viacep.model.Favorito;
import com.trabalho.viacep.model.Usuario;
import com.trabalho.viacep.repository.FavoritoRepository;
import com.trabalho.viacep.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritoService {

    @Autowired private FavoritoRepository favoritoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public Favorito salvarFavorito(String cep, String nome, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Favorito jaExiste = favoritoRepository.findByCepAndUsuarioId(cep, usuarioId);

        if (jaExiste != null) {
            throw new RuntimeException("Você já favoritou este CEP");
        }

        Favorito favorito = new Favorito();
        favorito.setCep(cep);
        favorito.setNome(nome);
        favorito.setUsuario(usuario);

        return favoritoRepository.save(favorito);
    }

    public List<Favorito> listarFavoritos(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void excluirFavorito(String cep, Long usuarioId) {
        favoritoRepository.deleteByCepAndUsuarioId(cep, usuarioId);
    }
}