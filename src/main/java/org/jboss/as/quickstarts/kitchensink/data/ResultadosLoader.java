package org.jboss.as.quickstarts.kitchensink.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultadosLoader {

	/**
	 * Gera txt e lista a partir do html da cx
	 * @return
	 */
	public List<List<Integer>> geraFromHtml() {
		String fileName = "/resource/input/D_MEGA.HTM";
		String filePath = new File("").getAbsolutePath();
		
		String line = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		Pattern regexpDt = Pattern.compile("\\d{2}\\/\\d{2}\\/\\d{4}");
	    Matcher matcherDt = regexpDt.matcher("");
	    
	    Pattern regexpNum = Pattern.compile(">(\\d{2})<");
	    Matcher matcherNum = regexpNum.matcher("");
		
	    List<List<Integer>> todos = new ArrayList<List<Integer>>();
	    
		int counter = 0;
		int credit = 0;
		int concurso = 0;
		try {
			FileWriter outfile = new FileWriter(filePath+"/resource/input/result.txt");
	        BufferedWriter output = new BufferedWriter(outfile); 
	        
			// FileReader reads text files in the default encoding.
			fileReader = new FileReader(filePath+fileName);

			// Always wrap FileReader in BufferedReader.
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null){// && counter < 110) {
				counter++;
				// print out the lines above that are found in the text
				matcherDt.reset(line);
					
				if(credit > 0){
					matcherNum.reset(line);
					//System.out.println(line);
					if(matcherNum.find()){
						output.write(matcherNum.group(1)+" ");
						todos.get(concurso).add(Integer.valueOf(matcherNum.group(1)));
					}
					if(credit == 1){
						output.newLine();
						concurso++;
					}
					credit--;
				}
				if(matcherDt.find()){
					credit = 12;
					todos.add(new ArrayList<Integer>());
				}
			}
			bufferedReader.close();
			fileReader.close();
			output.close();
			outfile.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

		return todos;
	}
	
	/**
	 * Gera array a partir do txt obtido do geraArray	
	 * @return
	 */
	public List<List<Integer>> geraFromResource() {
		String fileName = "/WEB-INF/classes/input/result.txt";
		String filePath = new File("").getAbsolutePath();
		System.out.println(filePath);
		
		String line = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		Pattern regexpDt = Pattern.compile("(\\d{2}) (\\d{2}) (\\d{2}) (\\d{2}) (\\d{2}) (\\d{2})");
	    Matcher matcherDt = regexpDt.matcher("");
	    
	    List<List<Integer>> todos = new ArrayList<List<Integer>>();
	    
		int counter = 0;
		try {
	        
			// FileReader reads text files in the default encoding.
			fileReader = new FileReader(filePath+fileName);

			// Always wrap FileReader in BufferedReader.
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null){// && counter < 110) {
				// print out the lines above that are found in the text
				matcherDt.reset(line);
				if(matcherDt.find()){
					todos.add(new ArrayList<Integer>());
					todos.get(counter).add(Integer.valueOf(matcherDt.group(1)));
					todos.get(counter).add(Integer.valueOf(matcherDt.group(2)));
					todos.get(counter).add(Integer.valueOf(matcherDt.group(3)));
					todos.get(counter).add(Integer.valueOf(matcherDt.group(4)));
					todos.get(counter).add(Integer.valueOf(matcherDt.group(5)));
					todos.get(counter).add(Integer.valueOf(matcherDt.group(6)));
					
				}
				counter++;
			}
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}

		return todos;
	}
	
}
