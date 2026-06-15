package com.trabalho.viacep.model;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "historico_cep")
public class Cep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico")
    private Long id;
    private String cep;
    private String cidade;
    private String estado;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String uf;
    private String regiao;
    private String ddd;
    private Timestamp dataConsulta;
    @ManyToOne
    private Usuario usuario;

    public Cep() {}

    public Cep(String cep, String cidade, String estado, String logradouro, String complemento, String bairro, String uf, String regiao, String ddd, Timestamp dataConsulta) {
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.uf = uf;
        this.regiao = regiao;
        this.ddd = ddd;
        this.dataConsulta = dataConsulta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public Timestamp getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(Timestamp dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}