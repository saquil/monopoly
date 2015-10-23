package com.ensa.monopoly.entites;

/**
 * 
 * Classe m�re de gestion des entit�s aptes � gerer les flux d'entr�e et de sortie d'argent
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
	 * Incr�mente le capital de l'entit�
	 * @param argent Le montant dans l'augmentation du capital
	 */
	public void incrArgent(int argent) {
		this.argent += argent;
	}
	
	/**
	 * D�cr�mente le capital de l'entit�
	 * @param argent Le montant de la coupe dans le capital
	 */
	public void decrAgent(int argent) {
		this.argent -= argent;
	}
}
