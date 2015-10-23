package com.ensa.monopoly.entites;

import com.ensa.monopoly.primitives.Vecteur;

/**
 * 
 * Classe de gestion d'un joueur de monopoly
 *
 */
public class Joueur extends EntiteMonetaire {
	/**
	 * L'identifiant unique du joueur
	 */
	private int id;
	/**
	 * Le nom du joueur
	 */
	private String nom;
	/**
	 * Le nombre de tours que le joueur a effectué
	 */
	private int nbTours = 0;
	/**
	 * Permet de savoir si le joueur est en prison ou pas
	 */
	private boolean estEnPrison;
	/**
	 * Compte le nombre de tours que le joueur a effectué en prison
	 */
	private int nbToursEnPrison;
	/**
	 * Un vecteur comportant la position du joueur sur le plateau en coordonnées euclidiennes
	 */
	private Vecteur positionPlateau;
	/**
	 * La case du plateau sur laquelle se trouve le joueur
	 */
	private int position;
	/**
	 * Le nombre de doubles que le joueur a effectué
	 */
	private int nbDoubles = 0;
	

	/**
	 * Incrémente la position du joueur de 'step' cases
	 * @param step Le nombre de cases dont le joueur va avancer
	 */
	public void incrementePosition(int step) {
		this.position += step;
		if(this.position > 39) {
			this.position -= 40;
			this.nbTours++;
			incrArgent(20000);
		}
	}
	
	public int getNbDoubles() {
		return nbDoubles;
	}

	public void setNbDoubles(int nbDoubles) {
		this.nbDoubles = nbDoubles;
	}

	public void incrNbDoubles() {
		this.nbDoubles++;
	}
	
	public Joueur(String nom, int id) {
		this.id = id;
		this.nom = nom;
		this.estEnPrison = false;
		this.position = 0;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getNbTours() {
		return nbTours;
	}

	public void setNbTours(int nbTours) {
		this.nbTours = nbTours;
	}
	
	public boolean estEnPrison() {
		return this.estEnPrison;
	}

	public void setEstEnPrison(boolean estEnPrison) {
		this.estEnPrison = estEnPrison;
		nbToursEnPrison = 0;
	}
	
	public int getNbToursEnPrison() {
		return nbToursEnPrison;
	}

	public void incrNbToursEnPrison() {
		this.nbToursEnPrison++;
	}
	
	public Vecteur getPositionPlateau() {
		return positionPlateau;
	}

	public void setPositionPlateau(Vecteur positionPlateau) {
		this.positionPlateau = positionPlateau;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
}
