package com.ensa.monopoly.cases;

import java.awt.Color;

/**
 * 
 * Classe de gestion des rues
 *
 */
public class CaseRue extends CaseCommerciale {
	/**
	 * La couleur de la rue unique à chaque groupe, qui sert d'identificateur pour grouper un quartier
	 */
	private Color couleur;
	/**
	 * Le nombre de maisons construites sur cette rue
	 */
	private int nbMaison;

	public int getNbMaison() {
		return nbMaison;
	}

	public void setNbMaison(int nbMaison) {
		this.nbMaison = nbMaison;
	}

	public CaseRue(int position, String nom, Color couleur, int prix) {
		super(position, nom);
		this.setPrix(prix);
		this.setCouleur(couleur);

	}

	public Color getCouleur() {
		return couleur;
	}

	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}
	
	/**
	 * Permet de connaitre le prix de construction d'une maison sur cette rue
	 * @return Un entier representant le prix de la maison
	 */
	public int prixMaison() {
		return (int)((float)this.getPrix()*0.75f);
	}
	
	/**
	 * Permet de connaitre la valeur hypotécaire de la maison
	 * @return Un entier representant la valeur hypothécaire de la maison
	 */
	public int valeurHypotheque() {
		return this.nbMaison*this.prixMaison()+(int)(this.getPrix()*0.8f);
	}

	/**
	 * 
	 * @param facteur Facteur accelerateur du prix du loyer (nombre de maisons possedées)
	 * @param terrainGroupeSousHypotheque Un boolean pour indiquer à la méthode si l'une des rues du quartier est sous hypothèque
	 * @return Le montant à payer pour le joueur qui tombe sur cette case
	 */
	public  int prixLoyer(int facteur, boolean terrainGroupeSousHypotheque) {
		if(this.isSousHypotheque()) {
			return 0;
		}
		if(terrainGroupeSousHypotheque) {
			return (int)((float)this.getPrix()/12.0f);
		}
		switch(facteur) {
		case 1 :
			return (int)((float)this.getPrix()/3.0f);
		case 2 :
			return (int)((float)this.getPrix()*0.8f);
		case 3 :
			return (int)((float)this.getPrix()*2.5f);
		case 4 :
			return (int)((float)this.getPrix()*4.0f);
		case 5 :
			return (int)((float)this.getPrix()*5.0f);
		default :
			return (int)((float)this.getPrix()/12.0f);
		}
	}
}
