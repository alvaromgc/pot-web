package org.jboss.as.quickstarts.kitchensink.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jboss.as.quickstarts.kitchensink.model.MediaDesv;
import org.jboss.as.quickstarts.kitchensink.model.Resultado;


public class SurpresaEngine {

public Random random = new Random();
	
	public void setSeed(long seed){
		this.random = new Random(seed);
	}
	public int rodaAroda(){
		return random.nextInt(60);
	}

	/**
	 * 
	 * @param med media
	 * @param dep desv padrao
	 * @param qnt numero de geracoes
	 * @param map *opt mapa com numero calculado de ocorrencias por num
	 * @param occurs *opt lista com quantidades de occorrencias desejadas: 1 maior ocorrencia, 2 media maior, 3 media menor, 4 menor
	 * @param printLog *opt imprime med e desvp das tentativas
	 * @return
	 */
	public List<List<Integer>> littleSurprise(double med, double dep, int qnt, Map<Integer, Integer> map, int[] occurs, boolean printLog) {
		
		List<List<Integer>> lifi = new ArrayList<List<Integer>>();
		
		Integer tentativas = 0;
		boolean checkout = false;
		
		for (int i = 0; i < qnt; i++) {
			lifi.add(new ArrayList<Integer>());
			do {
				List<Integer> seq = new ArrayList<Integer>();
				do {
					Integer num = rodaAroda();
					if (!seq.contains(num) && num > 0) {
						seq.add(num);
					}

				} while (seq.size() < 6);
				if(seq.size() == 6){
					boolean medVale = false;
					boolean depVale = false;
					Double calcmed = media(seq);
					Double calcdep = desv(seq, calcmed);
					Double rangemdw = med-0.5d;
					Double rangemup = rangemdw + 0.5d;
					Double rangeddw = dep-0.5d;
					Double rangedup = rangeddw + 0.5d;
					
					if(rangemdw.compareTo(calcmed) < 0 && calcmed.compareTo(rangemup) < 0){
						medVale = true;
					}
					if(rangeddw.compareTo(calcdep) < 0 && calcdep.compareTo(rangedup) < 0){
						depVale = true;
					}
					
					if(occurs != null && map == null){
						System.out.println("Mapa nao preenchido!!");
						return null;
					}
					
					if(( med == 0 && dep == 0 || (medVale && depVale)) && !containsSeq(lifi, seq) && (occurs == null || analiseQuantidadeMaiorMenorOcorrencia(map, seq,occurs) )){
						lifi.get(i).addAll(seq);
						if(printLog){
							System.out.print("med: "+calcmed);
							System.out.print(" devp: "+calcdep);
							System.out.println();
						}
					}
					tentativas++;
					if(tentativas > 10000000){//max 2bi
						System.out.println("Após 10 mi de tentativas não foi possível gerar todos "+qnt+" numeros com essas características...");
						if(lifi.size() > 0){
							printResult(lifi);
						}
						//System.out.println("Program ended!");
						//System.exit(0);
						//break;
						checkout = true;
						break;
					}
				}
				if(checkout) break;
				
			} while (lifi.get(i).size() < 6);
		}
		printResult(lifi);
		return lifi;
	}
	
	public void printResult(List<List<Integer>> lifi){
		for (int i = 0; i < lifi.size(); i++) {
			Collections.sort(lifi.get(i));
			for (int j = 0; j < 6; j++) {
				if(lifi.get(i) != null && !lifi.get(i).isEmpty()){
					System.out.print(lifi.get(i).get(j) + ",");
				}
			}
			System.out.println();
		}
		System.out.println("gerados: "+lifi.size());
	}
	
	public void printResultMediaDesvp(List<List<Double>> lifi, boolean showNumConcurso){
		for (int i = 0; i < lifi.size(); i++) {
			if(lifi.get(i) != null && !lifi.get(i).isEmpty()){
				String conc = "";
				if(showNumConcurso){
					conc = String.valueOf(i+1);
				}
				System.out.print(conc+" "+lifi.get(i).get(0) + " "+lifi.get(i).get(1));
			}
			System.out.println();
		}
	}
	
	public MediaDesv mapaMediaDesvPadrao(List<List<Integer>> jogs){
		MediaDesv mapaMediaDesvio = new MediaDesv();
		mapaMediaDesvio.setDesvio(new ArrayList<Double>());
		mapaMediaDesvio.setMedia(new ArrayList<Double>());
		for (int i = 0; i < jogs.size(); i++) {
			Double media  = media(jogs.get(i));
			mapaMediaDesvio.getMedia().add(media);
			mapaMediaDesvio.getDesvio().add(desv(jogs.get(i),media));
		}
		return mapaMediaDesvio;
	}
	
	public boolean containsSeq(List<List<Integer>>lista, List<Integer> seq){
		boolean found = false;
		for (List<Integer> jogo : lista) {
			if(jogo.containsAll(seq)){
				found = true;
			}
		}
		return found;
	}
	
	public Resultado containsSeqResult(List<List<Integer>>lista, List<Integer> seq){
		Resultado resultado = new Resultado();
		int i = 1;
		for (List<Integer> jogo : lista) {
			if(jogo.containsAll(seq)){
				resultado.setFound(true);
				resultado.setLot(jogo);
				resultado.setGen(seq);
				System.out.println("Posicao :"+i);
			}
			i++;
		}
		return resultado;
	}
	
	
	public double media(List<Integer> seq) {
		int sum = 0;
		for (Integer num : seq) {
			sum = sum + num;
		}
		return ((double) sum / 6);
	}

	public double desv(List<Integer> seq, double med) {
		double sum = 0;
		for (Integer num : seq) {
			double d = Math.abs((double) num - med);
			sum = sum + Math.pow(d, 2);
		}
		return Math.sqrt(sum / 5);
	}
	/**
	 * numero de sequencias em uma lista
	 */
	public void analiseSequenciais(List<List<Integer>> lista, int numeroSequencias){
		int i = 1;
		int contador = 0;
		for (List<Integer> gam : lista) {
			int seqCont = 0;
			for (Integer n : gam) {
				for (Integer m : gam) {
					if(n-m == 1){
						seqCont++;
					}
				}
				if(seqCont == numeroSequencias){
					contador++;
					System.out.println("No ["+i+"] tem ["+seqCont+"] repets.");
				}
			}
			i++;
		}
		System.out.println("Sao ["+contador+"] com ["+numeroSequencias+"] sequenciais");
	}
	
	public Map<Integer, Integer> analiseOrdemAparicoes(List<List<Integer>> sorteados, boolean printStats){
		Map<Integer, Integer> unidades = new HashMap<Integer, Integer>();
		for (int i = 1; i < 61; i++) {
			unidades.put(i, 0);
		}
		for (Integer num : unidades.keySet()) {
			int ap = 0;
			for (List<Integer> gam : sorteados) {
				if(gam.contains(num)){
					ap++;
				}
			}
			unidades.put(num, ap);
			//System.out.println("O numero ["+num+"] saiu ["+unidades.get(num)+"] vezes.");
		}
		
		//System.out.println("Ordenando...");
		
		List<Integer> result = new ArrayList<Integer>(); 
		LinkedHashMap<Integer, Integer> resultMap = new LinkedHashMap<Integer, Integer>();
		
		Integer menorKey = 1;
		List<Integer> winnerKeys = new ArrayList<Integer>();
		Integer menorValor = 99999;
		
		for (int i = 1; i <=60 ; i++) {
			menorValor = 999999;
			for (int g = 1; g <=60 ; g++) {
				if(unidades.get(g) <= menorValor && !winnerKeys.contains(g)){
					menorValor = unidades.get(g);
					menorKey = g;
				}
			}
			winnerKeys.add(menorKey);
			result.add(menorKey);
		}
		
		for (Integer numero : result) {
			if(printStats)System.out.println("O numero ["+numero+"] saiu ["+unidades.get(numero)+"] vezes.");
			resultMap.put(numero, unidades.get(numero));
		}
		
		return resultMap;
		
	}
	
	/**
	 * Busca o historico das ordens de aparicao a partir do kesimo concurso
	 * @param resultados
	 */
	public List<int[]> getHistoricoOcorrencias(List<List<Integer>> resultados, int k){
		List<int[]> listaTiposOcorrencia = new ArrayList<int[]>();
		for(int i = k; i < resultados.size(); i++){
			List<List<Integer>> parcial = resultados.subList(0, i -1);
			Map<Integer, Integer> mapaParcial = analiseOrdemAparicoes(parcial, false);
			int[] a = quantidadeMaioMenorOcorrencia(mapaParcial, resultados.get(i));
			listaTiposOcorrencia.add(a);
		}
		return listaTiposOcorrencia;
		
	}
	
	/**
	 * Imprime o historico das ordens de aparicao a partir do 1k
	 * @param resultados
	 */
	public void printHistoricoOcorrencias(List<int[]> resultados, int concInicial, boolean printNumConc){
		
		for(int i = 0; i < resultados.size(); i++){
			//System.out.println(i+" :: "+a[0]+" - "+a[1]+" - "+a[2]+" - "+a[3]);
			String numConc = "";
			if(printNumConc){
				numConc = String.valueOf(concInicial+i+1);
				numConc = "Concurso n "+numConc+" ";
			}
			System.out.println(numConc+resultados.get(i)[0]+" - "+resultados.get(i)[1]+" - "+resultados.get(i)[2]+" - "+resultados.get(i)[3]);
		}
		
	}
	
	public Boolean analiseQuantidadeMaiorMenorOcorrencia(Map<Integer, Integer> mapa, List<Integer> jog, int[] tipos){
		
		if(tipos.length != 4){
			System.out.println("Array de quantitativos deve ter 4 posicoes(Mais, medioMax, MedioMin, menos occorridos ).");
			return null;
		}
		if((tipos[0]+tipos[1]+tipos[2]+tipos[3]) != 6){
			System.out.println("Array de quantitativos deve ter 6 elementos ao todo.");
			return null;
		}
		
		int[] relOcorr = quantidadeMaioMenorOcorrencia(mapa, jog);
		
		return relOcorr[0] == tipos[0] && relOcorr[1] == tipos[1] && relOcorr[2] == tipos[2] && relOcorr[3] == tipos[3];
		
	}
	
	public int[] quantidadeMaioMenorOcorrencia(Map<Integer, Integer> mapa,
			List<Integer> jog) {
		
		Iterator<Integer> ite = mapa.keySet().iterator(); 
		Integer menorValor = mapa.get((Integer)ite.next());
		Integer maiorValor = 0;
		while (ite.hasNext()) {
			maiorValor = mapa.get((Integer) ite.next());
		}
		
		Integer meio = (Integer)((maiorValor + menorValor)/2);
		Integer umQuarto = (Integer)((meio + menorValor)/2);
		Integer tresQuarto = (Integer)((maiorValor + meio)/2);
		
		int[] relOcorr = {0,0,0,0};
		for (Integer num : jog) {
			Integer saiu = mapa.get(num);
			if(saiu >= tresQuarto){
				relOcorr[0]++;
			}else if(saiu < tresQuarto && saiu >= meio){
				relOcorr[1]++;
			}else if(saiu >= umQuarto && saiu < meio){
				relOcorr[2]++;
			}else if(saiu < umQuarto){
				relOcorr[3]++;
			}
		}
		return relOcorr;
	}
	
	public void testarGerados(List<List<Integer>> sorteados, List<List<Integer>> gerados){
		boolean none = true;
		for (List<Integer> gam : gerados) {
			Resultado res = containsSeqResult(sorteados, gam);
			if(res.getFound()){
				System.out.println("Got it miserable!!!");
				System.out.println(res.getLot());
				System.out.println(res.getGen());
				none = false;
			}
		}
		if(none){
			System.out.println("Agua!!!");
		}
	}
	
	public void printValoresQuantidadesOcorrencias(Map<Integer, Integer> mapa){
		Iterator<Integer> ite = mapa.keySet().iterator(); 
		Integer menorValor = mapa.get((Integer)ite.next());
		Integer maiorValor = 0;
		while (ite.hasNext()) {
			maiorValor = mapa.get((Integer) ite.next());
		}
		
		Integer meio = (Integer)((maiorValor + menorValor)/2);
		Integer umQuarto = (Integer)((meio + menorValor)/2);
		Integer tresQuarto = (Integer)((maiorValor + meio)/2);
		
		System.out.println("Classes de ordem sao: Maior:"+maiorValor+" Meio maior: "+tresQuarto+" Meio : "+meio+" Meio menor: "+umQuarto+" Menor: "+menorValor);
	}
	
}
