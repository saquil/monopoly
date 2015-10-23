package com.ensa.monopoly.cartes;

import java.util.ArrayList;

import com.ensa.monopoly.jeu.De;
/**
 * 
 * Classe de gestion de la pioche
 *
 */
public class Pioche {
	/**
	 * La liste de toutes les cartes chance
	 */
	private ArrayList<CarteChance> pileChance;
	public ArrayList<CarteChance> getPileChance() {
		return pileChance;
	}
	public void setPileChance(ArrayList<CarteChance> pileChance) {
		this.pileChance = pileChance;
	}
	/**
	 * La liste de toutes les cartes communauté
	 */
	private ArrayList<CarteCommunaute> pileCommunaute;
	/**
	 * La liste parmi laquelle les joueurs vont selectionner les cartes chance, qui décroit au fur et a mesure
	 */
	private ArrayList<CarteChance> cartesChance;
	public ArrayList<CarteChance> getCartesChance() {
		return cartesChance;
	}
	public void setCartesChance(ArrayList<CarteChance> cartesChance) {
		this.cartesChance = cartesChance;
	}
	/**
	 * La liste parmi lesquelles les joueurs vont selectionner les cartes communauté, qui décroit au fur et a mesure
	 */
	private ArrayList<CarteCommunaute> cartesCommunaute;
	
	public Pioche() {
		cartesChance = new ArrayList<CarteChance>();
		cartesCommunaute = new ArrayList<CarteCommunaute>();
		pileChance = new ArrayList<CarteChance>();
		pileCommunaute = new ArrayList<CarteCommunaute>();
	}
	public void ajouterCarteChance(CarteChance c) {
		cartesChance.add(c);
	}

	public void ajouterCarteCommunaute(CarteCommunaute c) {
		cartesCommunaute.add(c);
	}

	public ArrayList<CarteCommunaute> getCartesCommunaute() {
		return cartesCommunaute;
	}

	public void setCartesCommunaute(ArrayList<CarteCommunaute> cartesCommunaute) {
		this.cartesCommunaute = cartesCommunaute;
	}

	/**
	 * Cette fonction retourne une carte  chance de manière aléatoire et la supprime de la pile. Lorsque cette pile est vide, elle est régenerée avec
	 * la liste initiale des cartes chance
	 * @return Une carte chance piochée de manière aléatoire sans remise
	 */
	@SuppressWarnings("unchecked")
	public CarteChance piocherUneCarteChance() {
		if(pileChance.size() == 0) {
			// On doit cloner l'objet pour en garder une copie sure
			pileChance = (ArrayList<CarteChance>)cartesChance.clone();
		}
		int randomCarteNumber = De.getRandom(0, cartesChance.size()-1);
		CarteChance resultat = cartesChance.get(randomCarteNumber);
		cartesChance.remove(randomCarteNumber);
		// On regenere la liste
		if(cartesChance.size() == 0) {
			cartesChance = (ArrayList<CarteChance>)pileChance.clone();
		}
		return resultat;
	}

	/**
	 * Cette fonction retourne une carte communauté de manière aléatoire et la supprime de la pile. Lorsque cette pile est vide, elle est régenerée avec
	 * la liste initiale des cartes communauté
	 * @return Une carte communauté piochée de manière aléatoire sans remise
	 */
	@SuppressWarnings("unchecked")
	public CarteCommunaute piocherUneCarteCommunaute() {
		if(pileCommunaute.size() == 0) {
			// On doit cloner l'objet pour en garder une copie sure
			pileCommunaute = (ArrayList<CarteCommunaute>)cartesCommunaute.clone();
		}
		int randomCarteNumber = De.getRandom(0, cartesCommunaute.size()-1);
		CarteCommunaute resultat = cartesCommunaute.get(randomCarteNumber);
		cartesCommunaute.remove(randomCarteNumber);
		// On regenere la liste
		if(cartesCommunaute.size() == 0) {
			cartesCommunaute = (ArrayList<CarteCommunaute>)pileCommunaute.clone();
		}
		return resultat;
	}
}
