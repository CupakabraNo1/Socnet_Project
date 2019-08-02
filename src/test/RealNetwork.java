package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import implementation.UndirectedAffinityGraph;

public class RealNetwork {
	
	private static final String WIKI="wiki-RfA.txt";
	private static final String BITCOIN="soc-sign-bitcoinalpha.csv";
	
	public static UndirectedAffinityGraph<Integer, String> smallNetwork(){
		UndirectedAffinityGraph<Integer, String> graph=new UndirectedAffinityGraph<Integer, String>();
		for (int i = 1; i <= 5; i++) {
			graph.addVertex(i);
		}
		
		graph.addEdge(1+" "+2, true, 1, 2);
		graph.addEdge(3+" "+4, true, 3, 4);
		graph.addEdge(2+" "+5, false, 2, 5);
		graph.addEdge(5+" "+3, false, 5, 3);
		graph.addEdge(3+" "+2, false, 3, 2);
		
		return graph;
	}
	
	public static UndirectedAffinityGraph<String,String> wikipedia() {
		UndirectedAffinityGraph<String,String> graph=new UndirectedAffinityGraph<>();
		try {
			File wiki=new File(WIKI);
			BufferedReader reader=new BufferedReader(new FileReader(wiki));
			String line="";
			int i=0;
			try {
				line=reader.readLine();
				while (line!=null) {
					System.out.println(i++);

					if(line.startsWith("SRC:")) {
						String [] tokens=line.split(":");
						String sorce="";
						if(tokens.length > 1) {
							sorce=tokens[1].trim();
						}else {
							sorce="";
						}
						if(!graph.getVertices().contains(sorce)) {
							graph.addVertex(sorce);
						}
						line=reader.readLine();
						tokens=line.split(":");
						String target="";
						if(tokens.length > 1) {
							target=tokens[1].trim();
						}else {
							target="";
						}
						if(!graph.getVertices().contains(tokens[1].trim())) {
							graph.addVertex(tokens[1].trim());
						}
						line=reader.readLine();
						tokens=line.split(":");
						boolean sign=false;
						
						
						//Koristi mapu umesto ovoga brze je//
						
						
//						if("1".equals(tokens[1].trim())) {
//							sign=true;
//						}
//						
//						if(!graph.getNeighbours(sorce, true).contains(target) && !graph.getNeighbours(sorce, false).contains(target)) {
//							graph.addEdge(sorce+" "+target, sign, sorce, target);
//						}else {
//							if(graph.getNeighbours(sorce, true).contains(target) && !sign) {
//								graph.deleteEdge(sorce+" "+target);
//								graph.addEdge(sorce+" "+target, false, sorce, target);
//							}
//						}
					}
					line=reader.readLine();
				} 
			}catch (IOException e) {
				System.err.println("Error!");
				e.printStackTrace();
			}		
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			e.printStackTrace();
		}
		return graph;
		
	}
}
