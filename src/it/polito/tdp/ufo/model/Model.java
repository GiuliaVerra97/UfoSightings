package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
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
	
	
	//per RICORSIONE:
	//1 struttura dati finali
	//2 struttura dati parziale
	//3 condizione di terminazione, dopo un determinato nodo, non ci sono più successori che non ho considerato
	//4 generare una nuova soluzione a partire da una soluzione parziale
	//5 dato l'ultimo nodo inserito in parziale, considero tutti i successori di quel nodo che non ho anora considerato
	//6 filtro: alla fine, ritornerò solo una soluzione->quella per cui la size è max
	//7 qual è il livello di ricorsione->lunghezza del percorso parziale
	//8 il caso iniziale->parziale che contiene il mio stato di partenza
	
	private List<String> ottima;		//è una lista di stati in cui c'è lo stato di partenza e  un insieme di altri stati(non ripetuti)
	
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
	
	
	public List<String> getRaggiungibili(String stato){
		List<String> raggiungibili=new LinkedList<>();
		DepthFirstIterator<String, DefaultEdge> it=new DepthFirstIterator<>(this.grafo, stato);
		it.next();		//per saltare lo stato passato come parametro
		while(it.hasNext()) {
			raggiungibili.add(it.next());
		}
		
		return raggiungibili;
	}
	
	
	
	
	//metodo che richiama la ricorsione
	public List<String> getPercorsoOttimo(String partenza){
		this.ottima=new ArrayList<String>();		//ogni volta devo ricrearmi la lista della soluzione
		List<String> parziale=new LinkedList<String>();  //ogni volta devo crearmi la lista del parziale
		parziale.add(partenza);		//aggiungo caso iniziale
		cercaPercorso(parziale);
		return ottima;
	}


	
	//ricorsione
	private void cercaPercorso(List<String> parziale) {

		//condizione di terminazione
		if(parziale.size()>ottima.size()) {
			this.ottima=new LinkedList<String>(parziale);		//clono
		}
		
		List<String> candidati=this.getSuccessori(parziale.get(parziale.size()-1));		//prendo l'ultimo elemento del parziale come parametro da passare all'interno del metodo cercaSuccessori
		
		for(String candidato: candidati) {
			if(!parziale.contains(candidato)) {
				parziale.add(candidato);
				this.cercaPercorso(parziale);
				parziale.remove(parziale.size()-1);		//rimuovo l'ultimo elemento della lista (prendo quello come indice)
			}
		}
	}
	
	
	
	
	
}
