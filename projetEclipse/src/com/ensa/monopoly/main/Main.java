package com.ensa.monopoly.main;

import java.util.ArrayList;
import java.util.Scanner;

import com.ensa.monopoly.cartes.CarteChance;
import com.ensa.monopoly.cartes.CarteCommunaute;
import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.cases.CaseAllerPrison;
import com.ensa.monopoly.cases.CaseCarte;
import com.ensa.monopoly.cases.CaseChance;
import com.ensa.monopoly.cases.CaseCommerciale;
import com.ensa.monopoly.cases.CaseCommunaute;
import com.ensa.monopoly.cases.CaseCompagnie;
import com.ensa.monopoly.cases.CaseGare;
import com.ensa.monopoly.cases.CaseParcGratuit;
import com.ensa.monopoly.cases.CaseRue;
import com.ensa.monopoly.cases.CaseTaxe;
import com.ensa.monopoly.entites.Joueur;
import com.ensa.monopoly.ihm.Fenetre;
import com.ensa.monopoly.ihm.MessagePopup;
import com.ensa.monopoly.jeu.De;
import com.ensa.monopoly.jeu.Jeu;
import com.ensa.monopoly.jeu.PlateauDeJeu;

/**
 * 
 * Fonction pincipale
 *
 */
public class Main {
	public static Scanner sc;

/**
 * Genere la popup de lancement de dés
 * @param p L'objet popup
 * @return La valeur generée par le lancer
 */
	public static int lancerDes(MessagePopup p) {
		int nb1 = De.getRandom(1, 7);
		int nb2 = De.getRandom(1, 7);
		p.message("Vous avez fait " + nb1 + " et " + nb2);
		return (nb1 == nb2) ? -(nb1 + nb2) : (nb1 + nb2);
	}

	/**
	 * Affiche un message texte dans la console
	 * @param contenu
	 */
	public static void message(String contenu) {
		if (contenu.length() > 0) {
			System.out.println(contenu);
		}
	}


	public static void main(String[] argv) throws InterruptedException {
		System.out.println("Bienvenue dans le monopoly ENSA !");
		//System.out.println("(c) 2011 - MIT License");
		MessagePopup popups = new MessagePopup();
		int nbJoueurs = popups.choixNombreJoueurs();
		int scenario = popups.choixScenario();
		Jeu monopolyInstance = new Jeu(nbJoueurs);
		//System.out.println(nbJoueurs+":"+scenario);
		monopolyInstance.scenario(scenario, nbJoueurs);

		message("\n" + monopolyInstance.getNbJoueurs()
				+ " joueurs : la partie peut demarrer avec scenario "+scenario);
		System.out.println("");

		
	
		
		Fenetre fenetre = new Fenetre(monopolyInstance);

		while (true) {
			Jeu monopoly = fenetre.getJeu();
			/* LE JEU EST TERMINE */
			if (monopoly.getNbJoueurs() == 1) {
				Joueur j = monopoly.getJoueurs().get(0);
				popups.message("La partie est terminée ! le grand gagnant est le joueur"
						+ j.getId());
				boolean reload = popups
						.question("Voulez vous recommencer une partie ?");
				if (reload == true) {
					nbJoueurs = popups.choixNombreJoueurs();
					scenario = popups.choixScenario();
					monopolyInstance = new Jeu(nbJoueurs);
					monopolyInstance.scenario(scenario, nbJoueurs);

					fenetre = new Fenetre(monopolyInstance);
				} else {
					System.exit(0);
				}
			}
			Joueur joueur = monopoly.getJoueurCourant();
			boolean actionTerminee = false;
			boolean joueurElimine = false;
			int idActuel = joueur.getId();
			PlateauDeJeu plateau = monopoly.getPlateau();

			/**
			 * SORTIR DE PRISON
			 */

			if (joueur.estEnPrison()) {
				boolean reponse = popups.question("Joueur " + joueur.getId()
						+ ", voulez vous payer 5000€ pour sortir de prison ?");
				if (reponse == true) {
					joueur.decrAgent(5000);
					joueur.setEstEnPrison(false);
					popups.message("Vous sortez de prison en payant 5000€");
				} else {
					popups.message("Joueur "
							+ joueur.getId()
							+ " vous êtes en prison et avez refusé de payer 5000€. Vous pouvez cependant sortir en faisant un double");
				}
			} 

			popups.message("Joueur "
					+ joueur.getId()
					+ " c'est à votre tour ! Appuyez sur OK pour lancer les dés");
			int resultatLancer = lancerDes(popups);
			/**
			 * LE JOUEUR EST EN PRISON, IL N'A PAS FAIT DE DOUBLE, IL N'A PAS
			 * PAYE 5000€
			 */
			if (joueur.estEnPrison()) {
				joueur.incrNbToursEnPrison();
				if (joueur.getNbToursEnPrison() > 3) {
					popups.message("Faute de place, vous ne pouvez plus rester en prison ! Vous allez être liberé.");
					joueur.setEstEnPrison(false);
				} else {
					// Le joueur n'a pas fait de double, il n'a pas payé, il est
					// présent depuis moins de trois tours, il y rest
					resultatLancer = 0;
					popups.message("Vous restez en prison !");
				}
			}
			
			/**
			 * REGLE DES DOUBLES
			 */
			boolean aRealiseUnDouble = false;
			if (resultatLancer < 0) {
				resultatLancer = Math.abs(resultatLancer);
				popups.message("Vous avez fait un double ! Vous aurez le droit de rejouer après votre tour");
				// SORTIR DE PRISON GRACE A UN DOUBLE
				if (joueur.estEnPrison()) {
					joueur.setEstEnPrison(false);
					popups.message("Vous êtes sortis de prison grâce à votre double ! Relancez les dés");
					// Le tour du joueur reprend normalement
					resultatLancer = Math.abs(Main.lancerDes(popups));
					if (resultatLancer < 0) {
						joueur.incrNbDoubles();
					}
				}
				joueur.incrNbDoubles();
				if (joueur.getNbDoubles() == 3) {
					popups.message("Vous avez fait 3 doubles de suite !");
					popups.message("Vous allez directement en prison ! Vous ne toucherez pas 20000€ si vous passez par la case départ");
					joueur.setEstEnPrison(true);
					joueur.setNbDoubles(0);
					aRealiseUnDouble = false;
					fenetre.getUI().deplacerJoueur(joueur.getPosition(), 10);
					aRealiseUnDouble = false;
				} else {
					aRealiseUnDouble = true;
				}
			}

			/**
			 * DEPLACEMENT DU JOUEUR ET RECUPERATION DE LA CASE DE DESTINATION
			 */
			int destination = fenetre.getUI().calculNouvellePosition(
					joueur.getPosition(), resultatLancer);

			/* REGLE DE LA CASE DEPART */
			fenetre.getUI().deplacerJoueur(joueur.getId(), destination);
			Case caseCourante = monopoly.getCaseJoueur(joueur);
			Thread.currentThread();
			Thread.sleep(1500L); // sleep for 1500 ms
			

			/*
			 * ALLER EN PRISON
			 */
			if (caseCourante instanceof CaseAllerPrison) {
				popups.message("Vous allez directement en prison ! Vous ne toucherez pas 20000€ si vous passez par la case départ");
				joueur.setEstEnPrison(true);
				joueur.setNbDoubles(0);
				aRealiseUnDouble = false;
				joueur.getPositionPlateau().animate = false;
				joueur.setPosition(((CaseAllerPrison)caseCourante).getPosition());
				fenetre.getUI().deplacerJoueur(joueur.getId(), 10);
				Thread.currentThread();
				Thread.sleep(1500L); 
				actionTerminee = true;
			}

			

			/**
			 * ARRET SUR UNE PROPRIETE A VENDRE
			 */
			if (caseCourante instanceof CaseCommerciale
					&& ((CaseCommerciale) caseCourante).getProprietaire() < 0
					&& !actionTerminee) {

				fenetre.getUI().getCaseCentrale().set(caseCourante);
				fenetre.getUI().repaint();
				// La case courante a t-elle un proprietaire ?
				// Ce n'est pas le cas, proposer l'achat
				boolean encherePossible = false;
				if (joueur.getArgent() >= ((CaseCommerciale) caseCourante)
						.getPrix()) {
					boolean achat = popups
							.messageAchat(
									caseCourante,
									"Vous etes sur '"
											+ caseCourante.getNom()
											+ "', libre d'achat. Souhaitez vous l'acquerir pour le montant de "
											+ ((CaseCommerciale) caseCourante)
													.getPrix() + "€ ? ");

					if (achat == true) {
						// Effecter la transaction
						joueur.decrAgent(((CaseCommerciale) caseCourante)
								.getPrix());
						((CaseCommerciale) caseCourante).setProprietaire(joueur
								.getId());
					} else {
						// Le joueur refuse d'acheter, l'enchere est possible
						encherePossible = true;
					}
				} else {
					popups.message("Vous êtes sur '"
							+ caseCourante.getNom()
							+ "', libre d'achat, mais vous ne disposez pas de fonds suffisant pour devenir proprietaire.");
					// Le joueur n'a pas assez d'argent, l'enchere est possible
					encherePossible = true;
				}
				// Demarrage de la vente aux encheres
				if (encherePossible == true) {
					boolean enchereTerminee = false;
					ArrayList<Integer> participants = new ArrayList<Integer>();
					for (int i = 0; i < monopoly.getNbJoueurs(); i++) {
						participants.add(monopoly.getJoueurs().get(i).getId());
					}
					popups.message(caseCourante.getNom()
							+ " est en vente aux enchères ! (valeur "
							+ ((CaseCommerciale) caseCourante).getPrix()
							+ "€) : " + participants.size() + " participants");
					int miseAPrix = -1;
					int miseAPrixVisible = 0;
					while (!enchereTerminee) {
						int id = 0;
						while (participants.size() > 1 || miseAPrix == -1) {
							int propositionPrix = popups
									.getPrixProposition("[ La mise a prix actuelle est de "
											+ miseAPrixVisible
											+ "€ ] - Joueur "
											+ participants.get(id)
											+ " proposez votre prix de vente pour l'acquisition de '"
											+ caseCourante.getNom()
											+ "' (Entrez 0 pour ne pas participer)");

							if (propositionPrix == 0
									|| propositionPrix <= miseAPrix) {
								popups.message("Le joueur "
										+ participants.get(id)
										+ " s'est retiré de la vente");
								participants.remove(id);
							} else if (monopoly.getJoueurFromId(
									participants.get(id)).getArgent() < propositionPrix) {
								popups.message("Vous n'avez plus suffisement d'argent pour participer à cette vente");
								participants.remove(id);
							} else if (propositionPrix > miseAPrix) {
								miseAPrix = miseAPrixVisible = propositionPrix;
							}
							id++;

							miseAPrix = (participants.size() == 0) ? 0
									: miseAPrix;
							id = (id >= participants.size()) ? 0 : id;
						}
						// La vente aux encheres est terminée
						enchereTerminee = true;
						if (participants.size() == 1 && miseAPrix > 0) {
							// On a un nouveau proprietaire !
							// Effecter la transaction
							monopoly.getJoueurFromId(participants.get(0))
									.decrAgent(miseAPrix);
							((CaseCommerciale) caseCourante)
									.setProprietaire(participants.get(0));
							popups.message("Le joueur "
									+ participants.get(0)
									+ " a remporté la vente et est desormais l'heureux proprietaire de "
									+ caseCourante.getNom());

						}
					}
				}
				actionTerminee = true;
			}

			/**
			 * CARTES CHANCE
			 */
			if (caseCourante instanceof CaseCarte) {
				int startPosition = joueur.getPosition();
				int finalPosition = 0;
				if (caseCourante instanceof CaseChance) {
					CarteChance carte = plateau.getPioche()
							.piocherUneCarteChance();
					fenetre.getUI().getCaseCentrale().set(carte);
					popups.message("Vous avez tiré une carte chance ! '"
							+ carte.getNom() + "'");
					String message_retour = carte.affecterJoueur(joueur);
					popups.message(message_retour);
					finalPosition = carte.getEffetPosition();
				} else if (caseCourante instanceof CaseCommunaute) {
					CarteCommunaute carte = plateau.getPioche()
							.piocherUneCarteCommunaute();
					fenetre.getUI().getCaseCentrale().set(carte);
					popups.message("Vous avez tiré une carte caisse de communauté ! '"
							+ carte.getNom() + "'");
					String message_retour = carte.affecterJoueur(joueur);
					popups.message(message_retour);
					finalPosition = carte.getEffetPosition();
				}

				if (finalPosition >= 0) {
					joueur.setPosition(startPosition);
					joueur.getPositionPlateau().animate = false;
					fenetre.getUI().deplacerJoueur(joueur.getId(),
							finalPosition);
					Thread.currentThread();
					Thread.sleep(1500L);
				}
				if(finalPosition == 10) {
					aRealiseUnDouble = false;
				}

				fenetre.getUI().repaint();
			}

			/*
			 * ARRET SUR UNE PROPRIETE POSSEDEE PAR UN JOUEUR
			 */
			if (caseCourante instanceof CaseCommerciale
					&& ((CaseCommerciale) caseCourante).getProprietaire() >= 0
					&& !actionTerminee) {
				// Verifier que le joueur n'est pas proprietaire
				int proprietaire = ((CaseCommerciale) caseCourante)
						.getProprietaire();
				Joueur joueurProprietaire = monopoly
						.getJoueurFromId(proprietaire);
				

				if (proprietaire == joueur.getId()) {
					/* Il ne se passe rien */
					// Le crime ne paie pas
				} else if (joueurProprietaire.estEnPrison() == false) {
					// Le joueur doit payer un loyer
					int loyer = 0;
					if (caseCourante instanceof CaseCompagnie) {
						int nbCompagnies = plateau
								.nombreCompagniesPossedees(proprietaire);
						loyer = ((CaseCompagnie) caseCourante).prixLoyer(
								nbCompagnies,
								plateau.compagnieSousHypotheque(),
								resultatLancer);
					} else if (caseCourante instanceof CaseGare) {
						int nbGares = plateau.nombreGarePossedees(proprietaire);
						loyer = ((CaseGare) caseCourante).prixLoyer(nbGares,
								plateau.gareSousHypotheque());
					} else if (caseCourante instanceof CaseRue) {
						// C'est une terrain
						int nbMaisons = ((CaseRue) caseCourante).getNbMaison();
						loyer = ((CaseRue) caseCourante)
								.prixLoyer(
										nbMaisons,
										plateau.quartierSousHypotheque((CaseRue) caseCourante));
					}
					popups.message("Vous allez payer un loyer de " + loyer
							+ "€ au joueur " + proprietaire);
					int recouvrementReel = (joueur.getArgent() < loyer) ? joueur
							.getArgent() : loyer;
					joueur.decrAgent(loyer);
					joueurProprietaire.incrArgent(recouvrementReel);
				}
				actionTerminee = true;
			}

			/*
			 * TAXE ET IMPOTS
			 */
			if (caseCourante instanceof CaseTaxe) {
				popups.message("'" + caseCourante.getNom()
						+ "' - Vous devez regler un montant de "
						+ ((CaseTaxe) caseCourante).getTaxe() + "€");
				joueur.decrAgent(((CaseTaxe) caseCourante).getTaxe());
				plateau.getPotCommun().incrArgent(
						((CaseTaxe) caseCourante).getTaxe());
			}

			/*
			 * FAILLITE POTENTIELLE DU JOUEUR
			 */
			if (joueur.getArgent() < 0) {
				// Redressement fiscal
				ArrayList<CaseCommerciale> casesHypothequables = plateau
						.casesConstructibles(joueur);
				int valeurHypotheque = 0;
				for (int i = 0; i < casesHypothequables.size(); i++) {
					valeurHypotheque += casesHypothequables.get(i)
							.valeurHypotheque();
				}
				// Est ce que le joueur peut revenir dans le vert ?
				if (casesHypothequables.size() > 0
						&& joueur.getArgent() + valeurHypotheque > 0) {
					while (joueur.getArgent() < 0) {
						ArrayList<Integer> liste = popups
								.selectionPropriete(
										casesHypothequables,
										joueur,
										"Selectionnez les proprietes ou vous souhaitez vendre pour revenir dans le vert",
										"Vendre !", "VENTE");

						for (int i = 0; i < liste.size(); i++) {
							CaseCommerciale caseHyp = (CaseCommerciale) plateau
									.getCase(liste.get(i));
							joueur.incrArgent(caseHyp.valeurHypotheque());
							if (caseHyp instanceof CaseRue) {
								((CaseRue) caseHyp).setNbMaison(0);
							}
							caseHyp.setProprietaire(-1);
						}
					}
					popups.message("Bravo joueur " + joueur.getId()
							+ " vous avez echappé à la faillite");
				} else {
					// On desherite le joueur
					for (int i = 0; i < casesHypothequables.size(); i++) {
						CaseCommerciale caseHyp = (CaseCommerciale) plateau
								.getCase(i);
						if (caseHyp instanceof CaseRue) {
							((CaseRue) caseHyp).setNbMaison(0);
						}
						caseHyp.setProprietaire(-1);
					}
					// On l'élimine
					popups.message("Joueur " + joueur.getId()
							+ ", vous êtes eliminés :-(");
					for (int i = 0; i < monopoly.getNbJoueurs(); i++) {
						if (joueur.getId() == monopoly.getJoueurs().get(i)
								.getId()) {
							monopoly.getJoueurs().remove(i);
							joueurElimine = true;
						}
					}
				}
			}
			/*
			 * PARC GRATUIT
			 */
			if (caseCourante instanceof CaseParcGratuit) {
				if (plateau.getPotCommun().getArgent() > 0) {
					popups.message("Jour de chance ! Vous remportez le parc gratuit d'un montant de "
							+ plateau.getPotCommun().getArgent() + "€");
					joueur.incrArgent(plateau.getPotCommun().getArgent());
				}
				plateau.getPotCommun().setArgent(0);
			}

			/*
			 * FIN DE TOUR : TRANSACTION ENTRE JOUEUR OU ACHAT DE MAISON
			 */
			ArrayList<CaseCommerciale> casesConstructibles = plateau
					.casesConstructibles(joueur);
			ArrayList<CaseCommerciale> casesCommercialisables = plateau
					.casesCommercialisables(joueur);
			if (joueur.getArgent() > 0 && !joueur.estEnPrison()) {

				char resultat = 'X';
				while (resultat != '0') {
					String actionFinale = popups.terminerLeTour(
							casesConstructibles, casesCommercialisables);
					if (actionFinale != null) {
						resultat = actionFinale.charAt(0);
					} else {
						resultat = '0';
					}

					/*
					 * CONSTRUCTION DE PROPRIETES
					 */
					if (resultat == '1') {
						if (casesConstructibles.size() > 0) {
							ArrayList<Integer> liste = popups
									.selectionPropriete(
											casesConstructibles,
											joueur,
											"Selectionnez les lieux où vous souhaitez construire",
											"Construire !", "ACHAT");
							int nbTransactions = 0;
							boolean transactionsOk = true;
							for (int i = 0; i < liste.size(); i++) {
								CaseRue caseConstr = (CaseRue) plateau
										.getCase(liste.get(i));
								if (joueur.getArgent() > caseConstr
										.prixMaison()) {
									joueur.decrAgent(caseConstr.prixMaison());
									caseConstr.setNbMaison(caseConstr
											.getNbMaison() + 1);
									nbTransactions++;
								} else {
									transactionsOk = false;
								}
							}
							String erreurTransaction = (transactionsOk == true) ? " "
									: " Vous n'aviez pas assez de fond pour tout construire ";
							if (nbTransactions > 0 || transactionsOk == false) {
								popups.message(nbTransactions
										+ " transactions effectuée(s). "
										+ erreurTransaction);
							}
						}
					}

					/*
					 * TRANSACTION ENTRE JOUEURS
					 */
					if (resultat == '2') {
						int joueurChoisi = popups.selectionJoueur(joueur,
								monopoly.getJoueurs());
						ArrayList<Integer> liste = popups.selectionPropriete(
								casesCommercialisables, joueur,
								"Selectionnez proprietés que vous souhaitez vendre au Joueur "
										+ joueurChoisi,
								"Je choisis ces propriétés", "TRANSACTION");
						if (liste.size() > 0) {
							int prixProposition = popups
									.getPrixProposition("Proposez un prix pour ce(s) bien(s)");
							String question = "Joueur " + joueurChoisi
									+ ", acceptez vous ce(s) bien(s)";
							for (int i = 0; i < liste.size(); i++) {
								question += "\n - "
										+ plateau.getCase(liste.get(i))
												.getNom();
							}
							question += "\n pour la somme de "
									+ prixProposition + "€ ?";
							boolean choixJoueur = popups.question(question);
							boolean transactionPossible = (monopoly
									.getJoueurFromId(joueurChoisi).getArgent() > prixProposition) ? true
									: false;
							if (choixJoueur == true
									&& transactionPossible == true) {
								for (int i = 0; i < liste.size(); i++) {
									((CaseCommerciale) plateau.getCase(liste
											.get(i)))
											.setProprietaire(joueurChoisi);
								}
								monopoly.getJoueurFromId(joueurChoisi)
										.decrAgent(prixProposition);
								joueur.incrArgent(prixProposition);
							} else {
								popups.message("Transaction annulée");
							}
						}
					}
					if(resultat == '3') {
						boolean quitter = popups.question("Etes vous sur de vouloir quitter le jeu ?");
						if(quitter == true) {
							System.exit(0);
						}
					}
				}
			}

			fenetre.getUI().repaint();

			if (!aRealiseUnDouble) {
				monopoly.prochainJoueur(joueurElimine, idActuel);
			}
		}
	}
}
