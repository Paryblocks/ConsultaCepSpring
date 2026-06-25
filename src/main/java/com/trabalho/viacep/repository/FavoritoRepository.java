package com.trabalho.viacep.repository;

import com.trabalho.viacep.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    void deleteByCepAndUsuarioId(String cep, Long usuarioId);

    List<Favorito> findByUsuarioId(Long usuarioId);

    Favorito findByCepAndUsuarioId(String cep, Long usuarioId);
}
