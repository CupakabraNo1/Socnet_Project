package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
			BufferedReader reader=new BufferedReader(new FileReader(new File(WIKI)));
			String line="";
			Map<String,Boolean> map=new HashMap<String, Boolean>();
			try {
				line=reader.readLine();
				while (line!=null) {
					if(line.startsWith("SRC:")) {
						String sorce=line.substring(4);
						
						line=reader.readLine();
						String target=line.substring(4);
						
						line=reader.readLine();
						String s=line.substring(4).trim();
						Boolean sign=null;
						if(s.equals("1")) sign=true;
						else if (s.equals("-1")) sign=false;
						else continue;
						
						String key=sorce+" "+target;
						String rev=target+" "+sorce;
						if(map.containsKey(key) || map.containsKey(rev)) {
							if(!sign) {
								if(map.get(key)!=null) {
									map.put(key, false);
								}else {
									map.put(rev, false);
								}
							}
						}else {
							map.put(key, sign);
						}
					}
					line=reader.readLine();
				}
				
				map.entrySet().stream().distinct().forEach(x->{
					String [] s=x.getKey().split(" ");
					graph.addEdge(x.getKey(), x.getValue(), s[0], s[1]);
				});
				
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
	
	public static UndirectedAffinityGraph<Integer, String> bitcoin(){
		UndirectedAffinityGraph<Integer,String> graph=new UndirectedAffinityGraph<>();
		
		try {
			BufferedReader reader=new BufferedReader(new FileReader(new File(BITCOIN)));
			String line="";
			Map<String,Boolean> map=new HashMap<String, Boolean>();
			try {
				line=reader.readLine();
				while(line!=null) {
					String [] tokens=line.split(",");
					Integer sorce=Integer.parseInt(tokens[0].trim());
					Integer target=Integer.parseInt(tokens[1].trim());
					Integer s=Integer.parseInt(tokens[2].trim());
					Boolean sign=null;
					if(s<0) sign=false;
					else if(s>0) sign=true;
					else continue;
					
					String key=sorce+" "+target;
					String rev=target+" "+sorce;
					
					if(map.containsKey(key) || map.containsKey(rev)) {
						if(!sign) {
							if(map.get(key)!=null) {
								map.put(key, false);
							}else {
								map.put(rev, false);
							}
						}
					}else {
						map.put(key, sign);
					}
					line=reader.readLine();
					
				}
				map.entrySet().stream().distinct().forEach(x->{
					String [] s=x.getKey().split(" ");
					graph.addEdge(x.getKey(), x.getValue(), Integer.parseInt(s[0]), Integer.parseInt(s[1]));
				});
			} catch (IOException e) {
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
