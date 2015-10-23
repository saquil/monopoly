package com.ensa.monopoly.jeu;

import java.util.ArrayList;

/**
 * 
 * Classe d'outils de programmation
 *
 */
public class Tools {
/**
 * Fonction similaire à son homologue PHP, verifie une entree 'needle' dans un tableau 'haystack'
 * @param needle Le parametre a tester
 * @param haystack Le conteneur d'objets
 * @return Retourne TRUE si needle fait partie de haystack
 */
	public static boolean in_array(int needle, ArrayList<Integer> haystack) {
		for(int i = 0; i < haystack.size(); i++) {
			if(needle == haystack.get(i)) {
				return true;
			}
		}
		return false;
	}
}
