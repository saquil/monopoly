package com.ensa.monopoly.cases;

/**
 * 
 * Classe de gestion des cases dites commerciales (une case qui peut être possedée par un joueur et au titre de laquelle il perçoit un loyer)
 *
 */
public abstract class CaseCommerciale extends Case {
	/*
	 * Le prix d'achat net la case commerciale
	 */
	private int prix;
	/**
	 * Un identifiant qui retourne le proprietaire
	 */
	private int proprietaire;
	/**
	 * Un booléen pour tester si la case est actuellement sous hypotheque
	 */
	private boolean sousHypotheque;


	public boolean isSousHypotheque() {
		return sousHypotheque;
	}

	public void setSousHypotheque(boolean sousHypotheque) {
		this.sousHypotheque = sousHypotheque;
	}

	public int getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(int proprietaire) {
		this.proprietaire = proprietaire;
	}

	public CaseCommerciale(int position, String nom) {
		super(position, nom);
		this.sousHypotheque = false;
		this.proprietaire = -1;
	}

	public int getPrix() {
		return this.prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	/**
	 * 
	 * @return Un entier qui correspond au prix du bien hypothequé
	 */
	public int valeurHypotheque() {
		return (int)((float)this.getPrix()*0.8f);
	}
	/**
	 * 
	 * @param facteur Facteur accelerateur du prix du loyer (nombre de maisons possedées, nombre de gares possedées, etc...)
	 * @param terrainGroupeSousHypotheque Un boolean pour indiquer à la méthode si l'un des terrains du groupe est sous hypothèque
	 * @return Le prix du loyer à payer pour un joueur arrivant sur cette case
	 */
	public abstract int prixLoyer(int facteur,
			boolean terrainGroupeSousHypotheque);
}
