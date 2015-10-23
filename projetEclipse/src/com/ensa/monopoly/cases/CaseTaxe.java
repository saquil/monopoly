package com.ensa.monopoly.cases;

/**
 * 
 * Classe de gestion des taxes
 *
 */
public class CaseTaxe extends Case {
	/**
	 * Le prix de la taxe
	 */
	int prix;
	
	public CaseTaxe(int position, String nom, int prix) {
		super(position, nom);
		this.setTaxe(prix);
	}

	public int getTaxe() {
		return this.prix;
	}

	public void setTaxe(int taxe) {
		this.prix = taxe;
	}
}
