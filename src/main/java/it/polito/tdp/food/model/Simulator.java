package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.model.Event.EventType;

public class Simulator {
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private List<Food> food;
	private Model model;
	
	private List<Food> preparti; // Elenco dei cibi ancora da poter preparare a cui vengono tolti quelli già preparati
	private int stazioniRimanenti;
	private Food currentFood; // Cibo di partenza
	private PriorityQueue<Event> queue;
	
	// Output
	private int durata;
	private int numCibi;
	
	public Simulator(SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo, List<Food> cibi, Model model) {
		this.grafo = grafo;
		food = new ArrayList<>(cibi);
		this.model = model;
	}
	
	public void init(int k, Food partenza) {
		durata = 0;
		numCibi = 0;
		currentFood = partenza;
		queue = new PriorityQueue<>();
		preparti = new ArrayList<>();
		preparti.add(currentFood); // Tolgo dai cibi da poter ancora preparare quello di partenza
		stazioniRimanenti = k; // All'inizio le stazioni rimanenti sono uguali al numero messo in input dall'utente
		
		List<Adiacenza> calorieCongiunte = model.calorieCongiunte(currentFood);
		
		for(Adiacenza adiacenza : calorieCongiunte) {
			if(stazioniRimanenti > 0 && food.size() > 0) {
				queue.add(new Event(EventType.INIZIO_PREPARAZIONE, adiacenza.getF2(), stazioniRimanenti, (int)(adiacenza.getPeso().intValue())));
				stazioniRimanenti--;
				numCibi++;
				food.remove(adiacenza.getF2());
				preparti.add(adiacenza.getF2());
			}
			
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.durata = e.getTime(); // In questo modo nel campo durata alla fine della simulazione avrò l'evento di durata massima
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {
			case INIZIO_PREPARAZIONE:
				Food nextFood = sceltaCibo(e.getFoodLavorato());
				DefaultWeightedEdge edge = grafo.getEdge(nextFood, e.getFoodLavorato());
				if(preparti.contains(nextFood)) {
					stazioniRimanenti++; // La stazione si libera e non devo più lavorare nulla al suo interno
				}
				else {
					food.remove(nextFood);
					preparti.add(nextFood);
					queue.add(new Event(EventType.FINE_PREPARAZIONE, nextFood, e.getStazioneLavoro(), e.getTime() + (int)(grafo.getEdgeWeight(edge))));
				}
				
				break;
				
			case FINE_PREPARAZIONE:
				numCibi++;
				queue.add(new Event(EventType.INIZIO_PREPARAZIONE, e.getFoodLavorato(), e.getStazioneLavoro(), e.getTime()));
				break;
	
			default:
				break;
		}
		
	}
	
	public Food sceltaCibo(Food partenza) {
		List<Adiacenza> calorieCongiunte = model.calorieCongiunte(partenza);
		Food next = null;
		
		if(partenza.equals(calorieCongiunte.get(0).getF1()))
			next = calorieCongiunte.get(0).getF2();
		else 
			next = calorieCongiunte.get(0).getF1();
		
		return next;
	}
	
	public int getDurata() {
		return durata;
	}
	
	public int getNumCibi() {
		return numCibi;
	}
}
