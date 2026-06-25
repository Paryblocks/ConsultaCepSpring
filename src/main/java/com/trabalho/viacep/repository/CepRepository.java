package com.trabalho.viacep.repository;

import com.trabalho.viacep.model.Cep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CepRepository extends JpaRepository<Cep, Long> {
    void deleteByUsuarioId(Long usuarioId);

    List<Cep> findByUsuarioIdOrderByDataConsultaDesc(Long usuarioId);

    Optional<Cep> findByCepAndUsuarioId(String cep, Long usuarioId);

    @Modifying 
    @Query("DELETE FROM Cep c WHERE c.id = :id AND c.usuario.id = :usuarioId")
    void deleteByIdAndUsuarioId(@Param("id") Long id, @Param("usuarioId") Long usuarioId);
}
