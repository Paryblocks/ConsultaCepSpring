package com.trabalho.viacep.repository;

import com.trabalho.viacep.model.Cep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CepRepository extends JpaRepository<Cep, Long> {
    void deleteByUsuarioId(Long usuarioId);

    @Query("SELECT c.cep FROM Cep c " +
            "WHERE c.usuario.id = :usuarioId " +
            "GROUP BY c.cep " +
            "ORDER BY COUNT(c.cep) DESC")
    List<String> findTop15CepsMaisPesquisados(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c.cep FROM Cep c " +
            "GROUP BY c.cep " +
            "ORDER BY COUNT(c.cep) DESC " +
            "LIMIT 1")
    Optional<String> findCepMaisConsultadoGlobal();

    @Query("SELECT COUNT(c) FROM Cep c WHERE c.usuario.id = :usuarioId")
    int contarTotalAcessosPorUsuarioId(@Param("usuarioId") Long usuarioId);

    List<Cep> findByUsuarioIdOrderByDataConsultaDesc(Long usuarioId);

    Optional<Cep> findByCepAndUsuarioId(String cep, Long usuarioId);
}
