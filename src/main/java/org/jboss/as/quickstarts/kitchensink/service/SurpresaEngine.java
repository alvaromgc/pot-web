package org.jboss.as.quickstarts.kitchensink.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.as.quickstarts.kitchensink.model.EnumClasseAparicao;
import org.jboss.as.quickstarts.kitchensink.model.Game;
import org.jboss.as.quickstarts.kitchensink.model.MediaDesv;
import org.jboss.as.quickstarts.kitchensink.model.Ocorrencia;
import org.jboss.as.quickstarts.kitchensink.model.Resultado;


public class SurpresaEngine {
	
	@Inject
    private Logger log;

	private long fiboseed = 1123581321345589144l;
	
	public Random random = new Random(fiboseed);
	
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
	 * @param games jogos anteriores para calculo de ocorrencias
	 * @param occurs *opt lista com quantidades de occorrencias desejadas: 1 maior ocorrencia, 2 media maior, 3 media menor, 4 menor
	 * @param printLog *opt imprime med e desvp das tentativas
	 * @return
	 */
	public List<Game> littleSurprise(double med, double dep, int qnt, List<Game> games, int[] occurs, boolean printLog) {
		//List<Ocorrencia> ocorrencias = 
		
		//LinkedHashMap<Integer, Integer> map = transformaMapOcorrencias(ocorrencias);
		if(occurs[0] == 0 && occurs[1] == 0 && occurs[2] == 0 && occurs[3] == 0){
			occurs = null;
		}
		
		Map<Integer, Integer> map = analiseOrdemAparicoes(games);
		
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
					
					Double rangemdw = med-2.0d;
					Double rangemup = rangemdw + 4.0d;
					Double rangeddw = dep-2.0d;
					Double rangedup = rangeddw + 4.0d;
					
					/* antiga margem
					Double rangemdw = med-0.5d;
					Double rangemup = rangemdw + 0.5d;
					Double rangeddw = dep-0.5d;
					Double rangedup = rangeddw + 0.5d;*/
					
					if(rangemdw.compareTo(calcmed) < 0 && calcmed.compareTo(rangemup) < 0){
						medVale = true;
					}
					if(Double.valueOf(dep).equals(Double.valueOf(0.0d)) 
							|| (rangeddw.compareTo(calcdep) < 0 && calcdep.compareTo(rangedup) < 0 )){
						depVale = true;
					}
					
					if(occurs != null && map == null){
						System.out.println("Mapa nao preenchido!!");
						return null;
					}
					
					if(( med == 0 && dep == 0 || (medVale && depVale)) && !containsSeq(lifi, seq) && (occurs == null || analiseQuantidadeMaiorMenorOcorrencia(map, seq,occurs) )){
						lifi.get(i).addAll(seq);
						Collections.sort(lifi.get(i));
						if(printLog){
							log.fine("med: "+calcmed);
							log.fine(" devp: "+calcdep);
						}
					}
					tentativas++;
					if(tentativas > 10000000){//max 2bi
						//System.out.println("Após 10 mi de tentativas não foi possível gerar todos "+qnt+" numeros com essas características...");
						//if(lifi.size() > 0){
						//	printResult(lifi);
						//}
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
		//printResult(lifi);
		List<Game> surpresas = transformaListToGame(lifi);
		return surpresas;
	}
	
	public void printResult(List<List<Integer>> lifi){
		for (int i = 0; i < lifi.size(); i++) {
			//Collections.sort(lifi.get(i));
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
	
	public Map<Integer, Integer> analiseOrdemAparicoes(List<Game> sorteados){
		Map<Integer, Integer> unidades = new HashMap<Integer, Integer>();
		for (int i = 1; i < 61; i++) {
			unidades.put(i, 0);
		}
		for (Integer num : unidades.keySet()) {
			int ap = 0;
			for (Game gam : sorteados) {
				if(gam.getList().contains(num)){
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
		
		//List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		
		
		for (Integer numero : result) {
			//if(printStats)System.out.println("O numero ["+numero+"] saiu ["+unidades.get(numero)+"] vezes.");
			//Ocorrencia oc = new Ocorrencia();
			//oc.setNumero(numero);
			//oc.setQuantidade(unidades.get(numero));
			//ocorrencias.add(oc);
			resultMap.put(numero, unidades.get(numero));
		}
		
		return resultMap;
		
	}
	
	/**
	 * Busca o historico das ordens de aparicao a partir do kesimo concurso
	 * @param resultados
	 */
	//TODO ALGO ERRADO AQUI
	public List<Ocorrencia> getHistoricoOcorrencias(List<Game> resultados, int inicio){
		List<Ocorrencia> listaTiposOcorrencia = new ArrayList<Ocorrencia>();
		for(int i = inicio; i < resultados.size(); i++){
			List<Game> parcial = new ArrayList<>();
			if(i == 0){
			//
			}else if(i == 1){
				parcial.add(resultados.get(0));
			}else{
				parcial = resultados.subList(0, i);
			}
			Map<Integer, Integer> mapaParcial = analiseOrdemAparicoes(parcial);
			int[] a = {0,0,0,0};
			//mapa de ocorrencias gerado a partir do 100 resultado
			//if(i > 100){
			a = quantidadeMaioMenorOcorrencia(mapaParcial, resultados.get(i).getList());
			//}
			
			Ocorrencia oc = new Ocorrencia();
			oc.setOcorrencias(a);
			listaTiposOcorrencia.add(oc);
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
		
		int[] relOcorr = {0,0,0,0};
		for (Integer num : jog) {
			EnumClasseAparicao eca = getClasseOcorrenciapPorPosicao(mapa, num);
			if(eca.equals(EnumClasseAparicao.MAIOR)){
				relOcorr[0]++;
			}else if(eca.equals(EnumClasseAparicao.MEDIO_MAIOR)){
				relOcorr[1]++;
			}else if(eca.equals(EnumClasseAparicao.MEDIO_MENOR)){
				relOcorr[2]++;
			}else if(eca.equals(EnumClasseAparicao.MENOR)){
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
	
	public EnumClasseAparicao getClasseOcorrenciapPorQuantidade(Map<Integer, Integer> mapa, Integer quantidade){
		Iterator<Integer> ite = mapa.keySet().iterator(); 
		Integer menorValor = mapa.get((Integer)ite.next());
		Integer maiorValor = 0;
		while (ite.hasNext()) {
			maiorValor = mapa.get((Integer) ite.next());
		}
		
		Integer meio = (Integer)((maiorValor + menorValor)/2);
		Integer umQuarto = (Integer)((meio + menorValor)/2);
		Integer tresQuarto = (Integer)((maiorValor + meio)/2);
		
		EnumClasseAparicao eca = null;
		
		if(quantidade >= tresQuarto){
			eca = EnumClasseAparicao.MAIOR;
		}else if(quantidade < tresQuarto && quantidade >= meio){
			eca = EnumClasseAparicao.MEDIO_MAIOR;
		}else if(quantidade >= umQuarto && quantidade < meio){
			eca = EnumClasseAparicao.MEDIO_MENOR;
		}else if(quantidade < umQuarto){
			eca = EnumClasseAparicao.MENOR;
		}
		
		return eca;
	}
	
	public EnumClasseAparicao getClasseOcorrenciapPorPosicao(Map<Integer, Integer> mapa, Integer numero){
		List<Integer> numerosOrdenados = new ArrayList<>();  
		numerosOrdenados.addAll(mapa.keySet());
		int posicao = numerosOrdenados.indexOf(numero); 
		EnumClasseAparicao eca = null;
		if(posicao < 15){
			eca = EnumClasseAparicao.MENOR;
		}else if(posicao < 30){
			eca = EnumClasseAparicao.MEDIO_MENOR;
		}else if(posicao < 45){
			eca = EnumClasseAparicao.MEDIO_MAIOR;
		}else if(posicao <= 60){
			eca = EnumClasseAparicao.MAIOR;
		}
		
		return eca;
	}
	
	private LinkedHashMap<Integer, Integer> transformaMapOcorrencias(List<Ocorrencia> ocorrencias){
		LinkedHashMap<Integer, Integer> resultMap = new LinkedHashMap<Integer, Integer>();
		for (Ocorrencia ocorrencia : ocorrencias) {
			resultMap.put(ocorrencia.getNumero(), ocorrencia.getQuantidade());
		}
		return resultMap;
	};
	
	private List<Game> transformaListToGame(List<List<Integer>> lista){
		List<Game> games = new ArrayList<>();
		for (List<Integer> listInt : lista) {
			Game g = new Game();
			g.setNum1(listInt.get(0));
			g.setNum2(listInt.get(1));
			g.setNum3(listInt.get(2));
			g.setNum4(listInt.get(3));
			g.setNum5(listInt.get(4));
			g.setNum6(listInt.get(5));
			games.add(g);
		}
		return games;
	};
	
//	public void printValoresQuantidadesOcorrencias(Map<Integer, Integer> mapa){
//		Iterator<Integer> ite = mapa.keySet().iterator(); 
//		Integer menorValor = mapa.get((Integer)ite.next());
//		Integer maiorValor = 0;
//		while (ite.hasNext()) {
//			maiorValor = mapa.get((Integer) ite.next());
//		}
//		
//		Integer meio = (Integer)((maiorValor + menorValor)/2);
//		Integer umQuarto = (Integer)((meio + menorValor)/2);
//		Integer tresQuarto = (Integer)((maiorValor + meio)/2);
//		
//		System.out.println("Classes de ordem sao: Maior:"+maiorValor+" Meio maior: "+tresQuarto+" Meio : "+meio+" Meio menor: "+umQuarto+" Menor: "+menorValor);
//	}
	
}
