package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	public enum EventType {
		INIZIO_PREPARAZIONE,
		FINE_PREPARAZIONE
	}
	
	private EventType type;
	private Food foodLavorato;
	private int stazioneLavoro;
	private int time;
	
	public Event(EventType type, Food foodLavorato, int stazioneLavoro, int time) {
		super();
		this.type = type;
		this.foodLavorato = foodLavorato;
		this.stazioneLavoro = stazioneLavoro;
		this.time = time;
	}

	public EventType getType() {
		return type;
	}


	public void setType(EventType type) {
		this.type = type;
	}


	public Food getFoodLavorato() {
		return foodLavorato;
	}


	public void setFoodLavorato(Food foodLavorato) {
		this.foodLavorato = foodLavorato;
	}


	public int getStazioneLavoro() {
		return stazioneLavoro;
	}


	public void setStazioneLavoro(int stazioneLavoro) {
		this.stazioneLavoro = stazioneLavoro;
	}


	public int getTime() {
		return time;
	}


	public void setTime(int time) {
		this.time = time;
	}


	@Override
	public int compareTo(Event o) {
		return this.time-o.time;
	}
	
}
