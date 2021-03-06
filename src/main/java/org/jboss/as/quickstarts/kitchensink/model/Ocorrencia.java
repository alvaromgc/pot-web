package org.jboss.as.quickstarts.kitchensink.model;

public class Ocorrencia {
	
	private Integer numero;
	private Integer quantidade;
	private int[] ocorrencias;
	private EnumClasseAparicao classeAparicao;
	
	
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	public int[] getOcorrencias() {
		return ocorrencias;
	}
	
	public void setOcorrencias(int[] ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public EnumClasseAparicao getClasseAparicao() {
		return classeAparicao;
	}

	public void setClasseAparicao(EnumClasseAparicao classeAparicao) {
		this.classeAparicao = classeAparicao;
	}


}
