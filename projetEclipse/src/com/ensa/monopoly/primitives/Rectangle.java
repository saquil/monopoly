package com.ensa.monopoly.primitives;
/**
 * 
 * Classe utilitaire de rectangle
 *
 */
public class Rectangle {
	/**
	 * Coordonées X
	 */
	public float x;
	/**
	 * Coordonées Y
	 */
	public float y;
	/**
	 * Largeur
	 */
	public float w;
	/**
	 * Hauteur
	 */
	public float h;
	
	public Rectangle() {
		x = y = w = h = 0;
	}
	public Rectangle(float origineX, float origineY, float rectW, float rectH) {
		x = origineX;
		y = origineY;
		w = rectW;
		h = rectH;
	}
	
	public int getX() {
		return (int)x;
	}
	public int getY() {
		return (int)y;
	}
	public int getW() {
		return (int)w;
	}
	public int getH() {
		return (int)h;
	}
}
