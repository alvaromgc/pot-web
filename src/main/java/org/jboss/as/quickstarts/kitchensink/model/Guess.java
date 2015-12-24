/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Guess implements Serializable {

    private Double media;

    private Double desvio;
    
    private int maiorFrequencia;
    
    private int mediaMaiorFrequencia;
    
    private int mediaMenorFrequencia;
    
    private int menorFrequencia;
    
    private Integer quantidade;

	public Double getMedia() {
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
	}

	public Double getDesvio() {
		return desvio;
	}

	public void setDesvio(Double desvio) {
		this.desvio = desvio;
	}

	public int getMaiorFrequencia() {
		return maiorFrequencia;
	}

	public void setMaiorFrequencia(int maiorFrequencia) {
		this.maiorFrequencia = maiorFrequencia;
	}

	public int getMediaMaiorFrequencia() {
		return mediaMaiorFrequencia;
	}

	public void setMediaMaiorFrequencia(int mediaMaiorFrequencia) {
		this.mediaMaiorFrequencia = mediaMaiorFrequencia;
	}

	public int getMediaMenorFrequencia() {
		return mediaMenorFrequencia;
	}

	public void setMediaMenorFrequencia(int mediaMenorFrequencia) {
		this.mediaMenorFrequencia = mediaMenorFrequencia;
	}

	public int getMenorFrequencia() {
		return menorFrequencia;
	}

	public void setMenorFrequencia(int menorFrequencia) {
		this.menorFrequencia = menorFrequencia;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
    

}
