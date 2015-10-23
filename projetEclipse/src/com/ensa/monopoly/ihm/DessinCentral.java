package com.ensa.monopoly.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import com.ensa.monopoly.cartes.Carte;
import com.ensa.monopoly.cartes.CarteChance;
import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.cases.CaseCompagnie;
import com.ensa.monopoly.cases.CaseGare;
import com.ensa.monopoly.cases.CaseRue;
import com.ensa.monopoly.jeu.Jeu;

/**
 * 
 * La classe de dessin pour la partie centrale de l'HM
 * 
 */
public class DessinCentral {
	/**
	 * Va stocker la case (si elle existe), que l'on doit representer au centre
	 * du plateau
	 */
	Case caseCentrale;
	/**
	 * Va stocker la carte (si elle existe), que l'on doit representer au centre
	 * du plateau
	 */
	Carte carteCentrale;
	/**
	 * Un booleen pour indiquer si il s'agit d'une case ou d'une carte qu'il y a
	 * au centre
	 */
	public boolean bCaseCentrale = false;

	/**
	 * Permet d'indiquer quel objet l'on souhaite dessiner au centre du plateau
	 * 
	 * @param uneCase
	 *            La case que l'on souhaite dessiner au centre
	 */
	public void set(Case uneCase) {
		this.caseCentrale = uneCase;
		this.bCaseCentrale = true;
		this.carteCentrale = null;
	}

	/**
	 * Permet d'indiquer quel objet l'on souhaite dessiner au centre du plateau
	 * 
	 * @param uneCarte
	 *            La carte que l'on souhaite dessiner au centre
	 */
	public void set(Carte uneCarte) {
		this.carteCentrale = uneCarte;
		this.bCaseCentrale = false;
		this.caseCentrale = null;
	}


	/**
	 * Permet de recuperer l'objet que l'on trouve au centre
	 * 
	 * @return Un objet (case ou carte) qui devra être affiché au centre du
	 *         plateau
	 */
	public Object getElementCentral() {
		if (this.caseCentrale == null) {
			return this.carteCentrale;
		}
		return this.caseCentrale;
	}

	/**
	 * Affiche le nom des joueurs avec leur montant respectif
	 * 
	 * @param g
	 *            L'objet graphique qui va nous servir à dessiner
	 * @param jeu
	 *            Un jeu de monopoly contenant toutes les informations
	 *            necessaires
	 */
	public void drawArgentJoueurs(Graphics g, Jeu jeu) {
		int potCommun = jeu.getPlateau().getPotCommun().getArgent();

		Fonts.setFontSize(g, 20, true);
		Fonts.drawString(g, "Parc Gratuit : "+potCommun+"€", Fenetre.globalWidth / 2 - 210, 190, 150);
		for (int i = 0; i < jeu.getJoueurs().size(); i++) {
			int x = Fenetre.globalWidth / 2 - 100;
			int y = 130;
			Fonts.setFontSize(g, 20, true);
		
			Fonts.drawString(g, "Joueur " + jeu.getJoueurs().get(i).getId()
					+ " : " + jeu.getJoueurs().get(i).getArgent() + "€", x, y
					+ i * 20, 200);

		}
	}

	/**
	 * Dessine le logo monopoly au centre ainsi que les cartes chances et caisse
	 * de communauté representative d'une case ou d'une carte en fonction des
	 * sistuations
	 * 
	 * @param g
	 *            L'objet graphique qui va nous servir à dessiner
	 * @param plateau
	 *            Le plateau de jeu sur lequel la carte sera dessinées
	 */
	public void drawLogoEtCartes(Graphics g, DessinPlateau plateau) {
		
		g.drawImage(plateau.getImages().getMonopolyLogo(),
				Fenetre.globalWidth / 2 - 61, 190, 131, 50, plateau);

		int x = Fenetre.globalWidth / 2 + 150;
		int y = Fenetre.globalHeight / 2;
		g.drawRect(x, y, 50, 70);
		g.drawImage(plateau.getImages().getMonopolyChance(), x, y, 50, 70,
				plateau);
		g.drawRect(x, y + 80, 50, 70);
		g.drawImage(plateau.getImages().getMonopolyChest(), x, y + 80, 50, 70,
				plateau);
	}

	/**
	 * Dessine les informations correspondant à la case sur laquelle se trouve le joueur
	 * @param g L'objet graphique nous permettant de dessiner
	 * @param plateau Le plateau de jeu surlequel la case sera dessinée
	 */
	public void drawCarteCentrale(Graphics g, DessinPlateau plateau) {
		if (this.getElementCentral() != null) {
			if (this.bCaseCentrale == true) {
				Case caseCentrale = (Case) this.getElementCentral();
				// DESSIN D'UNE CARTE REPRESENTATIVE D'UNE RUE
				if (caseCentrale instanceof CaseRue) {
					CaseRue r = ((CaseRue) caseCentrale);
					int x = Fenetre.globalWidth / 2 - 100;
					int y = Fenetre.globalHeight / 2 - 100;
					g.setColor(Color.BLACK);
					g.drawRect(x, y, 200, 330);
					g.setColor(r.getCouleur());
					g.fillRect(x, y, 200, 70);
					g.setColor(Color.black);
					Fonts.setFontSize(g, 20, true);
					Fonts.drawString(g, r.getNom(), x + 10, y + 90, 180);

					Fonts.setFontSize(g, 16, true);
					Fonts.drawString(g, "Prix : " + r.getPrix() + "€", x + 10,
							y + 130, 180);

					g.drawLine(x, y + 140, x + 200, y + 140);

					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "Terrain nu : " + r.prixLoyer(0, false)
							+ "€", x + 10, y + 160, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "1 maison : " + r.prixLoyer(1, false)
							+ "€", x + 10, y + 175, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "2 maisons : " + r.prixLoyer(2, false)
							+ "€", x + 10, y + 190, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "3 maisons : " + r.prixLoyer(3, false)
							+ "€", x + 10, y + 205, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "4 maisons : " + r.prixLoyer(4, false)
							+ "€", x + 10, y + 220, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "Un hôtel : " + r.prixLoyer(5, false)
							+ "€", x + 10, y + 235, 180);

					g.drawLine(x, y + 245, x + 200, y + 245);

					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "Prix Maison : " + r.prixMaison()
							+ "€", x + 10, y + 265, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g,
							"Prix Hôtel : " + r.prixMaison() + "€", x + 10,
							y + 280, 180);
					Fonts.setFontSize(g, 14, true);

					g.drawLine(x, y + 285, x + 200, y + 285);

					Fonts.drawString(g,
							"Valeur hypothequaire : " + r.valeurHypotheque()
									+ "€", x + 10, y + 305, 180);
				// DESSIN D'UNE CARTE REPRESENTATIVE D'UNE GARE
				} else if (caseCentrale instanceof CaseGare) {
					CaseGare gare = ((CaseGare) caseCentrale);
					int x = Fenetre.globalWidth / 2 - 100;
					int y = Fenetre.globalHeight / 2 - 100;
					g.setColor(Color.BLACK);
					g.drawRect(x, y, 200, 330);
					g.setColor(Color.black);
					Fonts.setFontSize(g, 20, true);
					Fonts.drawString(g, gare.getNom(), x + 10, y + 45, 180);

					g.drawLine(x, y + 70, x + 200, y + 70);

					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "1 Gare : " + gare.prixLoyer(0, false)
							+ "€", x + 10, y + 90, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "2 Gares: " + gare.prixLoyer(1, false)
							+ "€", x + 10, y + 110, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "3 Gares : " + gare.prixLoyer(2, false)
							+ "€", x + 10, y + 130, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "4 Gares : " + gare.prixLoyer(3, false)
							+ "€", x + 10, y + 150, 180);
					g.drawImage(plateau.getImages().getIconeGare(), x + 75,
							y + 160, 50, 50, plateau);
					g.drawLine(x, y + 220, x + 200, y + 220);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g,
							"Valeur hypotecaire : " + gare.valeurHypotheque()
									+ "€", x + 10, y + 250, 180);
				// DESSIN D'UNE CARTE REPRESENTATIVE D'UNE COMPAGNIE
				} else if (caseCentrale instanceof CaseCompagnie) {
					CaseCompagnie compagnie = ((CaseCompagnie) caseCentrale);
					int x = Fenetre.globalWidth / 2 - 100;
					int y = Fenetre.globalHeight / 2 - 100;
					g.setColor(Color.BLACK);
					g.drawRect(x, y, 200, 330);
					g.setColor(Color.black);
					Fonts.setFontSize(g, 20, true);
					Fonts.drawString(g, compagnie.getNom(), x + 10, y + 45, 180);

					g.drawLine(x, y + 70, x + 200, y + 70);

					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g, "1 Compagnie : Valeur des dés * 15 ",
							x + 10, y + 90, 180);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(g,
							"2 Compagnies : Valeur des dés * 37,5 ", x + 10,
							y + 130, 180);

					Image image = (compagnie.isCompagnieDesEaux()) ? plateau
							.getImages().getIconeCompagnieDesEaux() : plateau
							.getImages().getIconeCompagnieElectricite();
					g.drawImage(image, x + 75, y + 160, 50, 50, plateau);
					g.drawLine(x, y + 220, x + 200, y + 220);
					Fonts.setFontSize(g, 14, true);
					Fonts.drawString(
							g,
							"Valeur hypotecaire : "
									+ compagnie.valeurHypotheque() + "€",
							x + 10, y + 250, 180);
				}
			} else {
				// DESSIN REPRESENTIATIF D'UNE CARTE
				Carte carteCentrale = (Carte) this.getElementCentral();
				int x = Fenetre.globalWidth / 2 - 100;
				int y = Fenetre.globalHeight / 2 - 100;
				g.setColor(Color.BLACK);
				g.drawRect(x, y, 200, 220);
				g.setColor(Color.black);
				Fonts.setFontSize(g, 20, true);
				String intitule = (carteCentrale instanceof CarteChance) ? "Carte Chance"
						: "Caisse de communauté";
				Fonts.drawString(g, intitule, x + 10, y + 45, 180);

				g.drawLine(x, y + 70, x + 200, y + 70);

				Fonts.setFontSize(g, 14, true);
				Fonts.drawString(g, carteCentrale.getNom(), x + 10, y + 90, 180);

				Image image = (carteCentrale instanceof CarteChance) ? plateau
						.getImages().getIconeChance() : plateau.getImages()
						.getIconeCaisseCommunauté();
				g.drawImage(image, x + 75, y + 160, 50, 50, plateau);
			}
		}
	}
}
