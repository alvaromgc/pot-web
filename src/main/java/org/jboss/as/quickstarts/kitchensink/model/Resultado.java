package org.jboss.as.quickstarts.kitchensink.model;

import java.util.ArrayList;
import java.util.List;

public class Resultado{
	public Boolean found = new Boolean(false);
	public List<Integer> lot = new ArrayList<Integer>();
	public List<Integer> gen = new ArrayList<Integer>();
	
	public Boolean getFound() {
		return found;
	}
	public void setFound(Boolean found) {
		this.found = found;
	}
	public List<Integer> getLot() {
		return lot;
	}
	public void setLot(List<Integer> lot) {
		this.lot = lot;
	}
	public List<Integer> getGen() {
		return gen;
	}
	public void setGen(List<Integer> gen) {
		this.gen = gen;
	}
	
	
}
