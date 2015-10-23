package com.ensa.monopoly.entites;

/**
 * 
 * Classe mère de gestion des entités aptes à gerer les flux d'entrée et de sortie d'argent
 *
 */
public class EntiteMonetaire {
	private int argent = 150000;

	public int getArgent() {
		return argent;
	}

	public void setArgent(int argent) {
		this.argent = argent;
	}
	
	/**
	 * Incrémente le capital de l'entité
	 * @param argent Le montant dans l'augmentation du capital
	 */
	public void incrArgent(int argent) {
		this.argent += argent;
	}
	
	/**
	 * Décrémente le capital de l'entité
	 * @param argent Le montant de la coupe dans le capital
	 */
	public void decrAgent(int argent) {
		this.argent -= argent;
	}
}
