package com.trabalho.viacep.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "favorito")
public class Favorito {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_favorito")
	private Long id;
	@ManyToOne
	@JsonIgnoreProperties({"favoritos", "categorias", "senha"})
	private Usuario usuario;
	private String cep;
	private String nome;
	@ManyToMany
	@JoinTable(
			name = "favorito_categoria",
			joinColumns = @JoinColumn(name = "fk_id_favorito"),
			inverseJoinColumns = @JoinColumn(name = "fk_id_categoria")
	)
	@JsonIgnoreProperties("favoritos")
	private List<Categoria> categorias = new ArrayList<>();

	public Favorito() {}

	public Favorito(Usuario usuario, String cep, String nome) {
		this.usuario = usuario;
		this.cep = cep;
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
}
