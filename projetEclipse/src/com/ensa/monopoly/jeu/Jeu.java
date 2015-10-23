package com.ensa.monopoly.jeu;

import java.util.ArrayList;

import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.cases.CaseCommerciale;
import com.ensa.monopoly.cases.CaseRue;
import com.ensa.monopoly.entites.Joueur;

/**
 * 
 * La classe de jeu monopoly
 * 
 */
public class Jeu {
	/**
	 * Le joueur courant
	 */
	private int joueurCourant;
	/**
	 * Une liste contenant tous les joueurs participants
	 */
	private ArrayList<Joueur> joueurs;
	/**
	 * Le plateau de jeu
	 */
	private PlateauDeJeu plateau;

	public ArrayList<Joueur> getJoueurs() {
		return joueurs;
	}

	public void setJoueurs(ArrayList<Joueur> joueurs) {
		this.joueurs = joueurs;
	}

	public PlateauDeJeu getPlateau() {
		return plateau;
	}

	public void setPlateau(PlateauDeJeu plateau) {
		this.plateau = plateau;
	}

	public int getNbJoueurs() {
		return joueurs.size();
	}

	public Joueur getJoueurFromId(int id) {
		for (int i = 0; i < joueurs.size(); i++) {
			if (id == joueurs.get(i).getId()) {
				return joueurs.get(i);
			}
		}
		return null;
	}

	public Joueur getJoueurCourant() {
		return joueurs.get(this.joueurCourant);
	}

	public void setJoueurCourant(int joueurCourant) {
		this.joueurCourant = joueurCourant;
	}

	public Jeu(int nbJoueurs) {
		this.joueurCourant = 0;

		// / INITIALISATION DES JOUEURS
		this.joueurs = new ArrayList<Joueur>();
		for (int i = 0; i < nbJoueurs; i++) {
			this.joueurs.add(new Joueur("Joueur " + i, i));
		}

		// CREATION DU PLATEAU DE JEU
		this.plateau = new PlateauDeJeu();
	}

	/**
	 * Retourne une case directement depuis l'objet jeu
	 * 
	 * @param needle
	 *            L'index de la case que l'on souhaite obtenir
	 * @return Ka case
	 */
	public Case getCase(int needle) {
		if (needle > -1 && needle < 40) {
			return this.plateau.getCase(needle);
		}
		return null;
	}

	public int getNbcases() {
		return this.plateau.getNbcases();
	}

	public Case getCaseJoueur(Joueur unJoueur) {
		return this.getCase(unJoueur.getPosition());
	}

	/**
	 * Calcule et stocke dans joueurCourant l'index du prochain joueur
	 * 
	 * @param joueurElimine
	 *            A préciser à TRUE si un joueur vient d'être eliminé
	 * @param idActuel
	 *            L'id unique du joueur qui vient de terminer son tour
	 */
	public void prochainJoueur(boolean joueurElimine, int idActuel) {
		if (joueurElimine) {
			this.joueurCourant = 0;
			for (int i = 0; i < joueurs.size(); i++) {
				if (joueurs.get(i).getId() > idActuel) {
					this.joueurCourant = i;
					break;
				}
			}
		} else {
			this.joueurCourant += 1;
			// Puis par la taille du tableau
			if (this.joueurCourant >= this.getNbJoueurs()) {
				this.joueurCourant = 0;
			}
		}
		this.joueurs.get(this.joueurCourant).setNbDoubles(0);
	}

	/**
	 * Sort un joueur de prison
	 * 
	 * @param joueur
	 *            Le joueur à sortir de prison
	 */
	public void sortirDePrison(Joueur joueur) {
		joueur.decrAgent(5000);
		joueur.setEstEnPrison(false);
	}

	/**
	 * Change les parametres du jeu pour les mettre en accord avec un scenario
	 * précis
	 * 
	 * @param scenario
	 *            L'index du scenario choisi
	 * @param nbJoueurs
	 *            Le nombre de joueurs
	 */
	public void scenario(int scenario, int nbJoueurs) {
		// 1 - Normal
		// 2 - Environnement peuplé
		// 3 Catastrophe
		if (scenario == 2) {
			for (int i = 0; i < 40; i += 2) {
				int proprietaireAleatoire = (De.getRandom(0, nbJoueurs));
				if (this.getPlateau().getCase(i) instanceof CaseCommerciale) {
					((CaseCommerciale) this.getPlateau().getCase(i))
							.setProprietaire(proprietaireAleatoire);
				}
			}
		}

		if (scenario == 3) {
			for (int i = 1; i < this.getNbJoueurs(); i++) {
				this.getJoueurFromId(i).setArgent(100000);
			}
			for (int i = 0; i < 40; i++) {
				if (this.getPlateau().getCase(i) instanceof CaseRue) {
					((CaseRue) this.getPlateau().getCase(i)).setProprietaire(0);
					((CaseRue) this.getPlateau().getCase(i)).setNbMaison(5);
				} else if (this.getPlateau().getCase(i) instanceof CaseCommerciale) {
					int proprietaireAleatoire = (nbJoueurs > 2) ? De.getRandom(
							1, nbJoueurs - 1) : -1;
	
					((CaseCommerciale) this.getPlateau().getCase(i))
							.setProprietaire(proprietaireAleatoire);
				}

			}
		}

	}
}
