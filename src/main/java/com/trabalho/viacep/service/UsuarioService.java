package com.trabalho.viacep.service;

import com.trabalho.viacep.model.Usuario;
import com.trabalho.viacep.repository.UsuarioRepository;
import com.trabalho.viacep.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    private final String salt = "svl12j";

    public Usuario cadastrar(Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado!");
        }

        try {
            String senhaComSalt = Util.gerarSHA256(this.salt + usuario.getSenha() + this.salt);
            usuario.setSenha(senhaComSalt);
            usuario.setDataCriacao(Timestamp.from(Instant.now()));

            return repository.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar senha.");
        }
    }

    public Usuario login(String email, String senha) {
        try {
            String senhaComSalt = Util.gerarSHA256(this.salt + senha + this.salt);
            return repository.findByEmailAndSenha(email, senhaComSalt)
                    .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos!"));
        } catch (Exception e) {
            throw new RuntimeException("Erro no processo de login.");
        }
    }

    public Usuario atualizar(Long id, Usuario dadosNovos) {
        Usuario usuarioBanco = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dadosNovos.getEmail() != null && !dadosNovos.getEmail().isEmpty()) {
            usuarioBanco.setEmail(dadosNovos.getEmail());
        }
        if (dadosNovos.getNome() != null && !dadosNovos.getNome().isEmpty()) {
            usuarioBanco.setNome(dadosNovos.getNome());
        }

        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isEmpty()) {
            try {
                String senhaComSalt = Util.gerarSHA256(this.salt + dadosNovos.getSenha() + this.salt);
                usuarioBanco.setSenha(senhaComSalt);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao atualizar senha.");
            }
        }

        return repository.save(usuarioBanco);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não existe.");
        }
        repository.deleteById(id);
    }
}