package org.jboss.as.quickstarts.kitchensink.model;

public enum EnumClasseAparicao {
	MAIOR(1, "Maior Ocorrencia"),
	MEDIO_MAIOR(2, "Medio Maior"),
	MEDIO_MENOR(3, "Medio Menor"),
	MENOR(2, "Menor");
	
	
	private EnumClasseAparicao(int valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	private int valor;
	private String descricao;
	
	public int getValor() {
		return valor;
	}
	public String getDescricao() {
		return descricao;
	}
}
