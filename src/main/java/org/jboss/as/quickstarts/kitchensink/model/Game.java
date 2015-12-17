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
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table()
public class Game implements Serializable {

    @Id
    private Integer concurso;

    @NotNull
    private Integer num1;
    
    @NotNull
    private Integer num2;
    
    @NotNull
    private Integer num3;
    
    @NotNull
    private Integer num4;
    
    @NotNull
    private Integer num5;
    
    @NotNull
    private Integer num6;

    @Transient
    public List<Integer> getList(){
    	return Arrays.asList(num1,num2,num3,num4,num5,num6);
    }
    
    
    @Transient
    public String getPrintResult(){
    	return num1+" "+num2+" "+num3+" "+num4+" "+num5+" "+num6;
    }

    public Integer getNum1() {
		return num1;
	}


	public void setNum1(Integer num1) {
		this.num1 = num1;
	}


	public Integer getNum2() {
		return num2;
	}


	public void setNum2(Integer num2) {
		this.num2 = num2;
	}


	public Integer getNum3() {
		return num3;
	}


	public void setNum3(Integer num3) {
		this.num3 = num3;
	}


	public Integer getNum4() {
		return num4;
	}


	public void setNum4(Integer num4) {
		this.num4 = num4;
	}


	public Integer getNum5() {
		return num5;
	}


	public void setNum5(Integer num5) {
		this.num5 = num5;
	}


	public Integer getNum6() {
		return num6;
	}


	public void setNum6(Integer num6) {
		this.num6 = num6;
	}


	public Integer getConcurso() {
        return concurso;
    }

    public void setConcurso(Integer concurso) {
        this.concurso = concurso;
    }

}
