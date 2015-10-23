package com.ensa.monopoly.jeu;

/**
 * 
 * Classe pour lancer des dés
 *
 */
public class De {
	/**
	 * Calcule un nombre 'aléatoire' entre lower et higher
	 * @param lower Le minimum
	 * @param higher Le maximum
	 * @return Le nombre generé
	 */
	public static int getRandom(int lower, int higher) {
		
		int random = (int) (Math.random() * (higher - lower)) + lower;
		return random;
	}
}
