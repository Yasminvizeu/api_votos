package br.com.alura.loja.modelo;

import javax.persistence.*;
import java.awt.*;

@Entity
@Table(name = "categorias")
public class Categoria {

	@EmbeddedId
	private CategoriaId id;
	
	public Categoria() {
	}
	
	public Categoria(String nome) {
		this.id = new CategoriaId(nome, "xptoo");
	}

	public String getNome() {
		return this.id.getNome();
	}

}
