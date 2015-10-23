package com.ensa.monopoly.cases;

/**
 * 
 * La classe mère de gestion des cases
 *
 */
public class Case {
	/**
	 * La position de la case sur le plateau de jeu [0...40]
	 */
	private int position;
	/**
	 * Le nom de la case
	 */
	private String nom;
	
	public Case(int position, String nom) {
		this.position = position;
		this.nom = nom;
	}


	/**
	 * Permet de vérifier l'orientation de la case sur le plateau en fonction de sa position
	 * @param location Un flag (TOP/LEFT/BOTTOM/RIGHT) qui nous permet de tester l'orientation de la case sur le grid
	 * @return
	 */
	public boolean estSituee(String location) {
		if(this.getPosition() <= 10 && location.equals("BOTTOM")) {
			return true;
		} else if(this.getPosition() > 10 && this.getPosition() <= 20 && location.equals("LEFT")) {
			return true;
		} else if(this.getPosition() > 20 && this.getPosition() <= 30 && location.equals("TOP")) {
			return true;
		} else if(this.getPosition() > 30 && this.getPosition() <= 40 && location.equals("RIGHT")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Permet de savoir ou est située la case sur le plateau à l'aide d'un entier
	 * @return Un entier (0=BAS, 1=GAUCHE,2=HAUT,3=DROITE) qui nous permet de savoir ou la case est orientée sur le plateau
	 */
	public int estSitueeInt() {
		if(this.getPosition() <= 10) {
			return 0;
		} else if(this.getPosition() > 10 && this.getPosition() <= 20) {
			return 1;
		} else if(this.getPosition() > 20 && this.getPosition() <= 30) {
			return 2;
		} else if(this.getPosition() > 30 && this.getPosition() <= 40 ) {
			return 3;
		}
		return 0;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
}
