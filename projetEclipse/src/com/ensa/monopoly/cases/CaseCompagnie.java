package com.ensa.monopoly.cases;

/**
 * 
 * La classe de gestion des cases compagnie des eaux et compagnie electricit�
 * 
 */
public class CaseCompagnie extends CaseCommerciale {
	/** 
	 * Un booleen pour identifier la compagne des eaux de la compagnie d'�l�ctricit�
	 */
	boolean compagnieDesEaux;

	public boolean isCompagnieDesEaux() {
		return compagnieDesEaux;
	}

	public void setCompagnieDesEaux(boolean compagnieDesEaux) {
		this.compagnieDesEaux = compagnieDesEaux;
	}

	public CaseCompagnie(int position, String nom, int prix) {
		super(position, nom);
		this.setPrix(prix);
		this.compagnieDesEaux = false;
	}

	/**
	 * M�thode gener�e pour la compatibilit� avec la classe m�re
	 */
	public int prixLoyer(int facteur, boolean terrainGroupeSousHypotheque) {
		return 0;
	}

	/**
	 * Retourne le montant du loyer pour le joueur qui tombe sur cette case
	 * 
	 * @param facteur
	 *            Facteur accelerateur du prix du loyer (nombre de maisons
	 *            possed�es, nombre de gares possed�es, etc...)
	 * @param terrainGroupeSousHypotheque
	 *            Un boolean pour indiquer � la m�thode si l'un des terrains du
	 *            groupe est sous hypoth�que
	 * @param des
	 *            La valeur des d�s pour le calcul du prix du loyer
	 * @return Le montant � payer pour le joueur qui tombe sur cette case
	 */
	public int prixLoyer(int facteur, boolean terrainGroupeSousHypotheque,
			int des) {
		if (this.isSousHypotheque()) {
			return 0;
		}
		if (terrainGroupeSousHypotheque == true) {
			return (int) ((float) des * 15);
		}
		return (facteur == 2) ? (int) ((float) des * 15)
				: (int) ((float) des * 37.5f);
	}
}
