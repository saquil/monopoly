package com.ensa.monopoly.primitives;

import com.ensa.monopoly.ihm.Fenetre;

public class Vecteur {
	/**
	 * Coordonnées X
	 */
	public float x;
	/**
	 * Coordonnées Y
	 */
	public float y;
	/**
	 * Renvoie TRUE si le vecteur est en cours d'animation
	 */
	public boolean animate = false;
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public Vecteur() {
		x = y = 0;
	}
	
	public Vecteur(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Transforme les coordonnées du vecteurs en coordonées relatives [0..1]
	 * @return
	 */
	public Vecteur toRelativeCoordinates() {
		this.x = this.x/(float)Fenetre.globalWidth;
		this.y = this.y/(float)Fenetre.globalHeight;
		return this;
	}
	
	/**
	 * Transforme les coordonées du vecteur en coordonées absolues en taille d'écran
	 * @return Un vecteur en coordonées écran
	 */
	public Vecteur toAbsoluteCoordinates() {
		Vecteur v = new Vecteur(this.x*(float)Fenetre.globalWidth, this.y*(float)Fenetre.globalHeight);
		return v;
	}
	
	/**
	 * Soustraction de 2 vecteurs
	 * @param vA parametre 1
	 * @param vB parametre 2
	 * @return Le résultat de la soustraction
	 */
	public static Vecteur soustraire(Vecteur vA, Vecteur vB) {
		return new Vecteur(vA.x - vB.x, vA.y - vB.y);
	}
	
	/**
	 * Addition de 2 vecteurs
	 * @param vA parametre 1
	 * @param vB parametre 2
	 * @return Le résultat de l'addition
	 */
	public static Vecteur additionner(Vecteur vA, Vecteur vB) {
		return new Vecteur(vA.x+vB.x, vA.y+vB.y);
	}
}
