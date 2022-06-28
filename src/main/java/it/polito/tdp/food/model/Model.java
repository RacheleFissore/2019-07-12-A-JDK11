package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	private FoodDao dao;
	private List<Food> vertici;
	
	public Model() {
		dao = new FoodDao();
		idMap = new HashMap<>();
		
		for(Food food : dao.listAllFoods()) {
			idMap.put(food.getFood_code(), food);
		}
	}
	
	public void creaGrafo(int porzioni) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getVertici(porzioni, idMap));
		vertici = new ArrayList<>(grafo.vertexSet());
		
		for(Adiacenza adiacenza : dao.getArchi(porzioni, idMap)) {
			if(grafo.getEdge(adiacenza.getF1(), adiacenza.getF2()) == null) {
				Graphs.addEdge(this.grafo, adiacenza.getF1(), adiacenza.getF2(), adiacenza.getPeso());
			}
		}
	}
	
	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
	 
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Food> getVertici() {
		return vertici;
	}
	
	public List<Adiacenza> calorieCongiunte(Food cibo) {
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		for(Food f : Graphs.neighborListOf(grafo, cibo)) {
			DefaultWeightedEdge edge = grafo.getEdge(cibo, f);
			adiacenze.add(new Adiacenza(cibo, f, grafo.getEdgeWeight(edge)));
		}
		
		Collections.sort(adiacenze);
		return adiacenze;
	}
	
	public String getDatiSimulazione(int k, Food food) {
		Simulator simulator = new Simulator(grafo, vertici, this);
		simulator.init(k, food);
		simulator.run();
		String string = "Durata simulazione: " + simulator.getDurata() + ", numero cibi lavorati: " + simulator.getNumCibi();
		return string;
	}
}
