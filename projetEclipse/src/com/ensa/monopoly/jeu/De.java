package com.ensa.monopoly.jeu;

/**
 * 
 * Classe pour lancer des d�s
 *
 */
public class De {
	/**
	 * Calcule un nombre 'al�atoire' entre lower et higher
	 * @param lower Le minimum
	 * @param higher Le maximum
	 * @return Le nombre gener�
	 */
	public static int getRandom(int lower, int higher) {
		
		int random = (int) (Math.random() * (higher - lower)) + lower;
		return random;
	}
}
