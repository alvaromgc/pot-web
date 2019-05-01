package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;
import java.util.List;


public class EntityWsCaixa implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2597444395219147344L;
	
	private Integer numero;
	
	private List<Integer> sorteio;
	
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public List<Integer> getSorteio() {
		return sorteio;
	}
	
	public void setSorteio(List<Integer> sorteio) {
		this.sorteio = sorteio;
	}

}
