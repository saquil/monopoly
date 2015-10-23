package com.ensa.monopoly.cases;

/**
 * 
 * Case de gestion des cartes
 *
 */
public class CaseGare extends CaseCommerciale {

	public CaseGare(int position, String nom, int prix) {
		super(position, nom);
		this.setPrix(prix);
	}
	
	/**
	 * 
	 * @param facteur Facteur accelerateur du prix du loyer (nombre de gares possedées)
	 * @param terrainGroupeSousHypotheque Un boolean pour indiquer à la méthode si l'une des gare est sous hypothèque
	 * @return Le montant à payer pour le joueur qui tombe sur cette case
	 */
	public int prixLoyer(int facteur, boolean terrainGroupeSousHypotheque) {
		if (this.isSousHypotheque()) {
			return 0;
		}
		if (terrainGroupeSousHypotheque == true) {
			return (int) ((float) this.getPrix() / 8.0f);
		}
		switch (facteur) {
		case 1:
			return (int) ((float) this.getPrix() / 8.0f);
		case 2:
			return (int) ((float) this.getPrix() * 4.8f);
		case 3:
			return (int) ((float) this.getPrix() * 2.5f);
		case 4:
			return (int) ((float) this.getPrix());
		default:
			return (int) ((float) this.getPrix() / 8.0f);
		}
	}
}
