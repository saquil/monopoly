package com.ensa.monopoly.cartes;

import com.ensa.monopoly.entites.Joueur;

/**
 * 
 * Classe maitre de gestion des cartes
 * 
 */
public class Carte {
	/**
	 * Le nom de la carte
	 */
	private String nom;
	/**
	 * L'effet que la carte aura sur la cagnotte du joueur (0, positif ou
	 * negatif)
	 */
	int effetArgent;
	/**
	 * La position sur laquelle le joueur sera amen� (-1 si la carte ne deplacer
	 * pas le joueur)
	 */
	int effetPosition;

	public Carte(String nom, int effetArgent, int effetPosition) {
		this.nom = nom;
		this.effetArgent = effetArgent;
		this.effetPosition = effetPosition;
	}
	/**
	 * Applique les effets de la carte � un joueur
	 * @param unJoueur Le joueur qui sera affect� par la carte
	 * @return Une chaine de caractere qui comprend le resultat eventuel de la consequence de la carte
	 */
	public String affecterJoueur(Joueur unJoueur) {
		if (this.getEffetPosition() >= 0) {
			unJoueur.setPosition(this.getEffetPosition());
			if (this.getEffetPosition() == 10) {
				unJoueur.setEstEnPrison(true);
				return "Le joueur " + unJoueur.getId() + " a �t� emprisonn�";
			}
		}
		unJoueur.incrArgent(this.getEffetArgent());
		return "";
	}

	public int getEffetPosition() {
		return effetPosition;
	}

	public void setEffetPosition(int effetPosition) {
		this.effetPosition = effetPosition;
	}

	public int getEffetArgent() {
		return effetArgent;
	}

	public void setEffetArgent(int effetArgent) {
		this.effetArgent = effetArgent;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
