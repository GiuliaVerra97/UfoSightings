package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.AnnoCount;
import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {

	private SightingsDAO dao;
	private List<String> stati;
	private Graph<String, DefaultEdge> grafo;
	
	
	public Model() {
		this.dao=new SightingsDAO();
	}
	
	
	public List<AnnoCount> getAnni(){
		return dao.getAnni();
	}
	
	
	public void creaGrafo(Year anno) {
		this.stati=this.dao.getStati(anno);
		this.grafo=new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.grafo, stati);
		
		//soluzione semplice=>doppio ciclo, controllo esistenza arco
		for(String s1:this.grafo.vertexSet()) {
			for(String s2:this.grafo.vertexSet()) {
				if(!s1.equals(s2)) {
					if(this.dao.esisteArco(s1, s2, anno)) {
						this.grafo.addEdge(s1, s2);
					}
				}
			}
		}
		
		
		System.out.print("Grafo creato!\n");
		System.out.println(this.grafo.vertexSet().size()+" vertici e di archi ce ne sono "+this.grafo.edgeSet().size());
		
		
		
	}


	
	//get e set
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}


	public List<String> getStati() {
		return this.stati;
	}
	
	
	//lista di successori di un vertice-stato
	public List<String> getSuccessori(String stato){
		return Graphs.successorListOf(this.grafo, stato);
	}
	
	
	//lista di predecessori di un vertice-stato
	public List<String> getPredecessori(String stato){
		return Graphs.predecessorListOf(this.grafo, stato);

	}
	
	
	/*public List<String> getRaggiungibili(){
		List<String> raggiungibili=new LinkedList<>();
		DepthFirstIterator dp=new DepthFirstIterator<>(this.grafo);
		return raggiungibili;
	}*/
	
	
	
}
