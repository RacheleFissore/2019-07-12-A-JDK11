package it.polito.tdp.food.model;

public class Adiacenza implements Comparable<Adiacenza> {
	private Food f1;
	private Food f2;
	private Double peso;
	
	public Adiacenza(Food f1, Food f2, Double peso) {
		super();
		this.f1 = f1;
		this.f2 = f2;
		this.peso = peso;
	}

	public Food getF1() {
		return f1;
	}

	public void setF1(Food f1) {
		this.f1 = f1;
	}

	public Food getF2() {
		return f2;
	}

	public void setF2(Food f2) {
		this.f2 = f2;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Adiacenza o) {
		return -(this.peso.compareTo(o.peso));
	}
	
}
