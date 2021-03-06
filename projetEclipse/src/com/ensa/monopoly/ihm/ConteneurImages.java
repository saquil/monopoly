package com.ensa.monopoly.ihm;

import java.awt.Image;
import java.util.HashMap;

/**
 * 
 * Le conteneur général des images de l'IHM
 *
 */
public class ConteneurImages {
	/**
	 * La liste des icones sous forme de HashMap, pour être identifiable par un id texte
	 */
	public static HashMap<String, Image> icones;

	public Image getIconeJoueur(int needle) {
		String playerKey = "icone_joueur" + needle;
		return icones.get(playerKey);
	}

	public Image getIconeGare() {
		return icones.get("icone_gare");
	}

	public Image getIconeChance() {
		return icones.get("icone_chance");
	}

	public Image getIconeMaison() {
		return icones.get("icone_maison");
	}

	public Image getIconeHotel() {
		return icones.get("icone_hotel");
	}

	public Image getIconeParcGratuit() {
		return icones.get("icone_parc_gratuit");
	}

	public Image getIconePrison() {
		return icones.get("icone_prison");
	}

	public Image getIconeAllerPrison() {
		return icones.get("icone_aller_prison");
	}

	public Image getIconeCaisseCommunauté() {
		return icones.get("icone_caisse_communaute");
	}

	public Image getIconeTaxeDeLuxe() {
		return icones.get("icone_taxe");
	}

	public Image getIconeCompagnieElectricite() {
		return icones.get("icone_compagnie_electricite");
	}

	public Image getIconeCompagnieDesEaux() {
		return icones.get("icone_compagnie_eaux");
	}

	public Image getIconeDepart() {
		return icones.get("icone_depart");
	}
	
	public Image getMonopolyLogo() {
		return icones.get("monopoly_logo");
	}

	public Image getMonopolyChance() {
		return icones.get("monopoly_chance");
	}

	public Image getMonopolyChest() {
		return icones.get("monopoly_chest");
	}
}
