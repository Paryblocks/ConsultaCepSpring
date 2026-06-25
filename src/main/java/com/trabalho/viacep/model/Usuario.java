package com.trabalho.viacep.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String senha;
	@Column(nullable = false, updatable = false)
	private Timestamp dataCriacao;
	@JsonIgnoreProperties("usuario")
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<Cep> historico = new ArrayList<>();
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<Favorito> favoritos = new ArrayList<>();
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<Categoria> categorias = new ArrayList<>();

	public Usuario() {}

	public Usuario(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Timestamp getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Timestamp dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<Cep> getHistorico() {
		return historico;
	}

	public void setHistorico(List<Cep> historico) {
		historico.forEach(c -> c.setUsuario(this));
		this.historico = historico;
	}

	public List<Favorito> getFavoritos() {
		return favoritos;
	}

	public void setFavoritos(List<Favorito> favoritos) {
		favoritos.forEach(f -> f.setUsuario(this));
		this.favoritos = favoritos;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		categorias.forEach(c -> c.setUsuario(this));
		this.categorias = categorias;
	}
}