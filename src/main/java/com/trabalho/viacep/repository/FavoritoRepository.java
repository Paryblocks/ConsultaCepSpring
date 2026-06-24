package com.trabalho.viacep.repository;

import com.trabalho.viacep.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
<<<<<<< HEAD
    Optional<Favorito> findByCepAndUsuarioId(String cep, Long usuarioId);

=======
>>>>>>> 0f3ff815dec9c8c3500216bf3a0f2b91c6e83f3d
    void deleteByCepAndUsuarioId(String cep, Long usuarioId);

    List<Favorito> findByUsuarioId(Long usuarioId);

    List<Favorito> findByCategoriasIdAndUsuarioId(Long categoriaId, Long usuarioId);
}
