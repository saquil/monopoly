package com.ensa.monopoly.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.cases.CaseAllerPrison;
import com.ensa.monopoly.cases.CaseChance;
import com.ensa.monopoly.cases.CaseCommerciale;
import com.ensa.monopoly.cases.CaseCommunaute;
import com.ensa.monopoly.cases.CaseCompagnie;
import com.ensa.monopoly.cases.CaseGare;
import com.ensa.monopoly.cases.CaseParcGratuit;
import com.ensa.monopoly.cases.CasePrison;
import com.ensa.monopoly.cases.CaseRue;
import com.ensa.monopoly.cases.CaseTaxe;
import com.ensa.monopoly.entites.Joueur;
import com.ensa.monopoly.jeu.Jeu;
import com.ensa.monopoly.primitives.Rectangle;
import com.ensa.monopoly.primitives.Vecteur;

/**
 * 
 * Classe de gestion du dessin du plateau
 *
 */
public class DessinPlateau extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * L'élément contenant le jeu de monopoly en lui-même
	 */
	private Jeu jeu;
	/**
	 * Le conteneur pour l'affichage de toutes les images
	 */
	private ConteneurImages images;
	/**
	 * La classe de dessin central
	 */
	private DessinCentral dessinCentral;
	/**
	 * La taille des icones de jetons de joueur par défaut
	 */
	
	/**
	 * Retourne la case centrale
	 * @return La case centrale
	 */
	public DessinCentral getCaseCentrale() {
		return this.dessinCentral;
	}
	public static int imageSize = 20;
	

	public ConteneurImages getImages() {
		return images;
	}

	public void setImages(ConteneurImages images) {
		this.images = images;
	}

	public DessinPlateau(Jeu jeu) {
		this.jeu = jeu;
		images = new ConteneurImages();
		dessinCentral = new DessinCentral();
		this.initPositionJoueur();
		this.setBackground(jeu.getPlateau().getBgColor());
	}

	public Dimension getPreferredSize() {
		return new Dimension(Fenetre.globalWidth, Fenetre.globalHeight);
	}

	/**
	 * Méthode suchargée pour le dessin du plateau
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.dessinerPlateau(g);
	}

	/**
	 * 
	 * Dessine intégralement le plateau de jeu (cases, joueurs, informations supplémentaires)
	 */
	public void dessinerPlateau(Graphics g) {
		// Dessin des cases
		for (int i = 0; i < this.jeu.getNbcases(); i++) {
			this.drawCase(this.jeu.getCase(i), g);
		}
		for (int i = 0; i < this.jeu.getNbJoueurs(); i++) {
			this.drawJoueur(this.jeu.getJoueurs().get(i), g);
		}

		this.dessinCentral.drawCarteCentrale(g, this);
		this.dessinCentral.drawArgentJoueurs(g, jeu);
		this.dessinCentral.drawLogoEtCartes(g, this);
	}

	/**
	 * Permet de déplacer le joueur d'une case à une autre, avec une animation 
	 * @param iJoueurId L'id unique du joueur à déplacer
	 * @param iCaseDest La case de destination pour le joueur
	 * @return La case de destination sur laquelle le joueur s'est déplacé
	 */
	public int deplacerJoueur(int iJoueurId, int iCaseDest) {
		if (this.jeu.getJoueurFromId(iJoueurId) != null && this.jeu.getJoueurFromId(iJoueurId).getPositionPlateau().animate == false) {
			if(this.jeu.getJoueurFromId(iJoueurId).getPosition() > iCaseDest && this.jeu.getJoueurFromId(iJoueurId).estEnPrison() == false) {
				this.jeu.getJoueurFromId(iJoueurId).incrArgent(20000);
			}
			Case cCaseSource = this.jeu.getPlateau().getCase(
					this.jeu.getJoueurFromId(iJoueurId).getPosition());
			// On (re)fixe la position initiale du joueur
			this.jeu.getJoueurFromId(iJoueurId).setPositionPlateau(
					Geom.positionCase(cCaseSource));
			// Orientation (direction+amplitude) du joueur
			int[] iOrientation = this.calculerOrientation(
					cCaseSource.getPosition(), iCaseDest);
			// Lancement du thread qui va déplacer graphiquement le joueur
			this.jeu.getJoueurFromId(iJoueurId).getPositionPlateau().animate = true;
			this.deplacerObject(this.jeu.getJoueurFromId(iJoueurId)
					.getPositionPlateau(), cCaseSource, iOrientation, 1000);
			this.jeu.getJoueurFromId(iJoueurId).setPosition(iCaseDest);
		}
		return iCaseDest;
	}

	/**
	 * Fonction qui permet de calculer les différentes orientations pour le déplacement du joueur
	 * @param iCaseSourceIndex La case à partir de laquelle on va calculer l'orientation
	 * @param iCaseDestIndex La case de destination du joueur
	 * @return Un tableau int[4] qui va comporter les différentes amplitudes de mouvement en suivant le principe suivant :
	 * int[0] contient le nombre de cases que le joueur à besoin de parcourir lorsqu'il est situé en bas
	 * int[1] contient le nombre de cases que le joueur à besoin de parcourir lorsqu'il est situé à gauche
	 * int[2] contient le nombre de cases que le joueur à besoin de parcourir lorsqu'il est situé en haut
	 * int[3] contient le nombre de cases que le joueur à besoin de parcourir lorsqu'il est situé à droite
	 */
	public int[] calculerOrientation(int iCaseSourceIndex, int iCaseDestIndex) {
		// 0 = BAS, 1 = GAUCHE, 2 = HAUT, 3 = DROIT
		int[] iMouvement = new int[4];
		iMouvement[0] = iMouvement[1] = iMouvement[2] = iMouvement[3] = 0;
		// On calcul l'amplitude du mouvement en fonction de la case de depart
		// et de la case d'arrivée
		int iAmplitude = (iCaseSourceIndex > iCaseDestIndex) ? (40 - iCaseSourceIndex)
				+ iCaseDestIndex
				: iCaseDestIndex - iCaseSourceIndex;
		// On recupere l'orientation de (BAS, GAUCHE, HAUT, DROITE) de la case
		// de départ
		int iOrientation = this.jeu.getPlateau().getCase(iCaseSourceIndex)
				.estSitueeInt();
		// On ajoute dans le tableau de mouvement les différentes orientations
		for (int i = 1; i <= iAmplitude; i++) {
			if (i == 1 && iCaseSourceIndex % 10 == 0 && iCaseSourceIndex != 0) {
				iOrientation = (iOrientation + 1 > 3) ? 0 : iOrientation + 1;
			}
			iMouvement[iOrientation]++;
			if ((iCaseSourceIndex + i) % 10 == 0) {
				iOrientation = (iOrientation + 1 > 3) ? 0 : iOrientation + 1;
			}
		}
		return iMouvement;
	}

	/**
	 * Déplace un vecteur d'une case à une autre selon un temps défini
	 * @param Origin Le vecteur que l'on souhaite déplacer
	 * @param caseStart La case de départ
	 * @param mouvement Le tableau d'orientation de mouvements
	 * @param dureeMouvement La durée du mouvement en millisecondes
	 */
	public void deplacerObject(Vecteur Origin, Case caseStart, int[] mouvement,
			long dureeMouvement) {
		final Vecteur vOrigin = Origin;
		final long iDureeMouvement = dureeMouvement;
		final Case cCaseDepart = caseStart;
		final int[] iMouvement = mouvement;
		new Thread() {
			public void run() {
				try {
					long STEP_SLEEP = 50L; // 20 FPS
					int STEPS = (int) (iDureeMouvement / STEP_SLEEP);
					// Calcul des différentes targets
					int iOrientationCourante = cCaseDepart.estSitueeInt();
					int iFutureOrientation = (iOrientationCourante + 1 > 3) ? 0
							: iOrientationCourante + 1;

					boolean moveFinish = false;
					int iCaseCourante = cCaseDepart.getPosition();
					Case caseCible = new Case(0, "");
					for (int i = 0; i < 4; i++) {
						if (!moveFinish) {
							if (iMouvement[iFutureOrientation] > 0) {
								// Calcul d'une position intermediaire (l'index
								// suivant contient une amplitude de mouvement)
								iMouvement[iOrientationCourante] = 0;
								iOrientationCourante = iFutureOrientation;
								iFutureOrientation = (iOrientationCourante + 1 > 3) ? 0
										: iOrientationCourante + 1;
								int iPositionIntermediaire = iOrientationCourante * 10;
								caseCible = DessinPlateau.this.jeu.getPlateau()
										.getCase(iPositionIntermediaire);
								iCaseCourante = iPositionIntermediaire;

							} else {
								// Un mouvement simple (sans changement
								// d'orientation) signale la fin de la
								// trajectoire du joueur
								int iPositionFinale = DessinPlateau.this
										.calculNouvellePosition(
												iCaseCourante,
												iMouvement[iOrientationCourante]);
								caseCible = DessinPlateau.this.jeu.getPlateau()
										.getCase(iPositionFinale);
								moveFinish = true;
							}
							// On effectue le mouvement
							Vecteur vMouvement = Vecteur.soustraire(
									Geom.positionCase(caseCible), vOrigin);
							for (int j = 0; j < STEPS; j++) {
								vOrigin.x += vMouvement.x / (float) STEPS;
								vOrigin.y += vMouvement.y / (float) STEPS;
								Thread.sleep(STEP_SLEEP);
								DessinPlateau.this.repaint();
							}
						}
					}
					/* ON DESSINE LA CARTE D'ARRIVEE */
					vOrigin.animate = false;

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Calcule une position sur le grid à partir d'une position de départ un d'une amplitude de mouvement
	 * @param position La position de départ
	 * @param step l'amplitude de mouvement
	 * @return La position finale
	 */
	public int calculNouvellePosition(int position, int step) {
		return (position + step > 39) ? (position + step) - 40 : position
				+ step;
	}
/**
 * Initialise la position des joueurs sur le placeau
 */
	public void initPositionJoueur() {
		for (int i = 0; i < this.jeu.getNbJoueurs(); i++) {
			Joueur unJoueur = this.jeu.getJoueurs().get(i);
			Case caseInitiale = this.jeu.getCase(unJoueur.getPosition());
			Vecteur positionJoueur = Geom.positionCase(caseInitiale);
			unJoueur.setPositionPlateau(positionJoueur);
		}

	}

	/**
	 * Compte le nombre de joueurs présents sur la  case
	 * @param cCase La case que l'on souhaite tester
	 * @return Le nombre de joueurs présents sur la case
	 */
	public int nbJoueursSurCase(Case cCase) {
		int playerCount = 0;
		for (int i = 0; i < this.jeu.getNbJoueurs(); i++) {
			if (cCase.getPosition() == this.jeu.getJoueurs().get(i).getPosition()) {
				playerCount++;
			}
		}
		return playerCount;
	}

	/**
	 * Dessine un joueur sur le plateau
	 * @param unJoueur Le joueur que l'on souhaite dessiner
	 * @param g L'objet graphics propre au desisn
	 */
	public void drawJoueur(Joueur unJoueur, Graphics g) {
		Vecteur imageOffset[] = new Vecteur[4];
		Rectangle stdRect = Geom.getCaseRectangle(this.jeu.getCase(2));
		// Calcul des "offsets" (décalement des images) en fonction du nombre de
		// pions sur la case
		if (this.nbJoueursSurCase(this.jeu.getCase(unJoueur.getPosition())) > 1) {
			if (this.jeu.getCase(unJoueur.getPosition()).estSitueeInt() == 0) {
				imageOffset[0] = new Vecteur(-stdRect.w / 5, 8);
				imageOffset[1] = new Vecteur(stdRect.w / 5, 8);
				imageOffset[2] = new Vecteur(-stdRect.w / 5, 30);
				imageOffset[3] = new Vecteur(stdRect.w / 5, 30);
			} else if (this.jeu.getCase(unJoueur.getPosition()).estSitueeInt() == 1) {
				imageOffset[0] = new Vecteur(-stdRect.w / 5, -4);
				imageOffset[1] = new Vecteur(stdRect.w / 5, -4);
				imageOffset[2] = new Vecteur(-stdRect.w / 5, 15);
				imageOffset[3] = new Vecteur(stdRect.w / 5, 15);
			} else if (this.jeu.getCase(unJoueur.getPosition()).estSitueeInt() == 2) {
				imageOffset[0] = new Vecteur(-stdRect.w / 5, -15);
				imageOffset[1] = new Vecteur(stdRect.w / 5, -15);
				imageOffset[2] = new Vecteur(-stdRect.w / 5, 4);
				imageOffset[3] = new Vecteur(stdRect.w / 5, 4);
			} else if (this.jeu.getCase(unJoueur.getPosition()).estSitueeInt() == 3) {
				imageOffset[0] = new Vecteur(0, -4);
				imageOffset[1] = new Vecteur((stdRect.w / 5) * 2, -4);
				imageOffset[2] = new Vecteur(0, 15);
				imageOffset[3] = new Vecteur((stdRect.w / 5) * 2, 15);
			}
		} else {
			// Si le joueur est seul, on le centre
			imageOffset[0] = imageOffset[1] = imageOffset[2] = imageOffset[3] = new Vecteur(
					0, 0);
		}
		g.drawImage(this.images.getIconeJoueur(unJoueur.getId()), unJoueur
				.getPositionPlateau().toAbsoluteCoordinates().getX()
				+ imageOffset[unJoueur.getId()].getX(), unJoueur
				.getPositionPlateau().toAbsoluteCoordinates().getY()
				+ imageOffset[unJoueur.getId()].getY(),
				DessinPlateau.imageSize, DessinPlateau.imageSize, this);

	}
	/**
	 * Dessine une case du plateau
	 * @param uneCase La case que l'on souhaite dessiner
	 * @param g L'objet graphics
	 */
	public void drawCase(Case uneCase, Graphics g) {
		// x et y correspondent à la position de l'origine de la case
		Rectangle caseRect = Geom.getCaseRectangle(uneCase);
		// dessin specifique
		Fonts.setFontSize(g, 10, true);
		if (uneCase instanceof CaseRue) {
			this.dessinerRue(uneCase, caseRect, g);
			// Gares
		} else if (uneCase instanceof CaseGare) {
			this.dessinerCaseCommerciale((CaseCommerciale) uneCase,
					this.images.getIconeGare(), caseRect, g);
			// Taxes
		} else if (uneCase instanceof CaseTaxe) {
			this.dessinerCase((CaseTaxe) uneCase,
					this.images.getIconeTaxeDeLuxe(), caseRect, g);
			// Compagnie des eaux
		} else if (uneCase instanceof CaseCompagnie && ((CaseCompagnie)uneCase).isCompagnieDesEaux()) {
			this.dessinerCaseCommerciale((CaseCommerciale) uneCase,
					this.images.getIconeCompagnieDesEaux(), caseRect, g);
			// Comagnie d'éléctricité
		} else if (uneCase instanceof CaseCompagnie && !((CaseCompagnie)uneCase).isCompagnieDesEaux()) {
			this.dessinerCaseCommerciale((CaseCommerciale) uneCase,
					this.images.getIconeCompagnieElectricite(), caseRect, g);
			// CASES NON COMMERCIALES
			// Aller en prison
		} else if (uneCase instanceof CaseAllerPrison) {
			this.dessinerCase(uneCase, this.images.getIconeAllerPrison(),
					caseRect, g);
			// Prison
		} else if (uneCase instanceof CasePrison) {
			this.dessinerCase(uneCase, this.images.getIconePrison(), caseRect,
					g);
			// Cartes chances
		} else if (uneCase instanceof CaseChance) {
			this.dessinerCase(uneCase, this.images.getIconeChance(), caseRect,
					g);
			// Case départ
		} else if (uneCase.getPosition() == 0) {
			this.dessinerCase(uneCase, this.images.getIconeDepart(), caseRect,
					g);
			// Caisse de communauté
		} else if (uneCase instanceof CaseCommunaute) {
			this.dessinerCase(uneCase, this.images.getIconeCaisseCommunauté(),
					caseRect, g);
			// Parc gratuit
		} else if (uneCase instanceof CaseParcGratuit) {
			this.dessinerCase(uneCase, this.images.getIconeParcGratuit(),
					caseRect, g);
		} else {
			g.setColor(Color.BLACK);
			g.drawRect(caseRect.getX(), caseRect.getY(), caseRect.getW(),
					caseRect.getH());
		}
	}

	/**
	 * Dessine plus spécifiquement une case commerciale (case sur laquelle peut s'effectuer des transactions)
	 * @param uneCase La case commerciale que l'on souhaite dessiner
	 * @param image L'image representative de la case
	 * @param caseRect Le rectangle qui contiendra cette case
	 * @param g L'objet graphics
	 */
	public void dessinerCaseCommerciale(CaseCommerciale uneCase, Image image,
			Rectangle caseRect, Graphics g) {

		FontMetrics fm = g.getFontMetrics();
		int textHeight = (fm.getHeight()) * 2; // -4 -> Notre "line height"
		g.setColor(Color.BLACK);
		g.drawRect(caseRect.getX(), caseRect.getY(), caseRect.getW(),
				caseRect.getH());

		int minDimension = (caseRect.getH() > caseRect.getW()) ? caseRect
				.getW() : caseRect.getH();
		int imageDimension = (minDimension + 5) - textHeight;

		Vecteur positionImage = Geom.getPositionCentre(caseRect, new Rectangle(
				0, 0, imageDimension, imageDimension));
		g.drawImage(image, positionImage.getX(), positionImage.getY(),
				imageDimension, imageDimension, this);
		String prix = uneCase.getPrix() + "€";
		Fonts.drawTopAndBottomString(uneCase, caseRect, new Rectangle(0, 0, 0,
				0), g, uneCase.getNom(), prix);

		/* ON DESSINE LE PROPRIETAIRE */
		for (int i = 0; i < this.jeu.getNbJoueurs(); i++) {
			if (((CaseCommerciale) uneCase).getProprietaire() == this.jeu.getJoueurs().get(i).getId()) {
				int id = this.jeu.getJoueurs().get(i).getId();
				int x = 0;
				int y = 0;
				if (uneCase.estSituee("BOTTOM")) {
					x = caseRect.getX() + caseRect.getW() / 2;
					y = caseRect.getY() - DessinPlateau.imageSize;
				} else if (uneCase.estSituee("LEFT")) {
					x = caseRect.getX() + caseRect.getW();
					y = caseRect.getY() + caseRect.getH() / 2;
				} else if (uneCase.estSituee("TOP")) {
					x = caseRect.getX() + caseRect.getW() / 2;
					y = caseRect.getH();
					;
				} else if (uneCase.estSituee("RIGHT")) {
					x = caseRect.getX() - DessinPlateau.imageSize;
					y = caseRect.getY() + caseRect.getH() / 2;
				}
				g.drawImage(this.images.getIconeJoueur(id), x, y,
						DessinPlateau.imageSize, DessinPlateau.imageSize, this);

			}
		}
	}

	/**
	 * Dessine n'importe quelle case autre que les cases commerciales et les cases rues
	 * @param uneCase La case que l'on souhaite dessiner
	 * @param image L'image qui sera affichée pour cette case
	 * @param caseRect Le rectangle qui contiendra la case
	 * @param g L'objet graphics
	 */
	public void dessinerCase(Case uneCase, Image image, Rectangle caseRect,
			Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int textHeight = (fm.getHeight()) * 2; // -4 -> Notre "line height"
		g.setColor(Color.BLACK);
		g.drawRect(caseRect.getX(), caseRect.getY(), caseRect.getW(),
				caseRect.getH());

		int minDimension = (caseRect.getH() > caseRect.getW()) ? caseRect
				.getW() : caseRect.getH();
		int imageDimension = (minDimension + 5) - textHeight;

		Vecteur positionImage = Geom.getPositionCentre(caseRect, new Rectangle(
				0, 0, imageDimension, imageDimension));
		g.drawImage(image, positionImage.getX(), positionImage.getY(),
				imageDimension, imageDimension, this);
		String bottomString = "";
		if (uneCase instanceof CaseTaxe) {
			bottomString = ((CaseTaxe) uneCase).getTaxe() + "FF";
		}
		Fonts.drawTopAndBottomString(uneCase, caseRect, new Rectangle(0, 0, 0,
				0), g, uneCase.getNom(), bottomString);
	}

	/**
	 * Dessine une case rue
	 * @param uneCase La case rue que l'on souhaite dessiner
	 * @param caseRect Le rectangle qui contiendra la case
	 * @param g L'objet graphics
	 */
	public void dessinerRue(Case uneCase, Rectangle caseRect, Graphics g) {
		CaseRue c = (CaseRue) uneCase;
		g.setColor(c.getCouleur());
		// Calcul du rectangle de couleur pour les rues
		Rectangle couleurRueRect = new Rectangle(caseRect.x, caseRect.y,
				caseRect.w, caseRect.h / 4.0f);
		int position = uneCase.getPosition();
		Rectangle maisonRect = new Rectangle(couleurRueRect.x, couleurRueRect.y, couleurRueRect.w/4, couleurRueRect.h);
		Rectangle hotelRect = new Rectangle(couleurRueRect.x+couleurRueRect.w/2, couleurRueRect.y, couleurRueRect.w/4, couleurRueRect.h);
		Vecteur maisonStep = new Vecteur(couleurRueRect.w/4, 0);
				
		if (position > 10 && position < 20) {
			couleurRueRect.x = caseRect.x + 0.75f * caseRect.w;
			couleurRueRect.w = 0.25f * caseRect.w;
			couleurRueRect.h = caseRect.h;

			hotelRect = new Rectangle(couleurRueRect.x, couleurRueRect.y+couleurRueRect.h/2, couleurRueRect.w, couleurRueRect.h/4);
			maisonStep = new Vecteur(0, couleurRueRect.h/4);
			maisonRect = new Rectangle(couleurRueRect.x, couleurRueRect.y, couleurRueRect.w, couleurRueRect.h/4);
		}
		if (position > 20 && position < 30) {
			couleurRueRect.y = caseRect.y + 0.75f * caseRect.h;
			
			 maisonRect = new Rectangle(couleurRueRect.x, couleurRueRect.y, couleurRueRect.w/4, couleurRueRect.h);
			 hotelRect = new Rectangle(couleurRueRect.x+couleurRueRect.w/2, couleurRueRect.y, couleurRueRect.w/4, couleurRueRect.h);
			 maisonStep = new Vecteur(couleurRueRect.w/4, 0);
			
		}
		if (position > 30 && position < 40) {
			couleurRueRect.w = 0.25f * caseRect.w;
			couleurRueRect.h = caseRect.h;

			hotelRect = new Rectangle(couleurRueRect.x, couleurRueRect.y+couleurRueRect.h/2, couleurRueRect.w, couleurRueRect.h/4);
			maisonStep = new Vecteur(0, couleurRueRect.h/4);
			maisonRect = new Rectangle(couleurRueRect.x, couleurRueRect.y, couleurRueRect.w, couleurRueRect.h/4);
		}
		g.drawRect(couleurRueRect.getX(), couleurRueRect.getY(),
				couleurRueRect.getW(), couleurRueRect.getH());
		g.fillRect(couleurRueRect.getX(), couleurRueRect.getY(),
				couleurRueRect.getW(), couleurRueRect.getH());
		// Fin du dessin de rectangle de couleur pour les rues
		// Dessin des maisons

		if (((CaseRue) uneCase).getNbMaison() > 4) {
			g.drawImage(this.images.getIconeHotel(), hotelRect.getX(),
					hotelRect.getY(), DessinPlateau.imageSize,
					DessinPlateau.imageSize, this);
		} else {
			for(int i = 0; i< ((CaseRue) uneCase).getNbMaison(); i++) {
				g.drawImage(this.images.getIconeMaison(), maisonRect.getX()+maisonStep.getX()*i,
						maisonRect.getY()+maisonStep.getY()*i, maisonRect.getW(),
						maisonRect.getH(), this);
			}
		}

		// Fin du dessin des maisons
		String prix = c.getPrix() + "€";
		Fonts.drawTopAndBottomString(c, caseRect, couleurRueRect, g,
				c.getNom(), prix);

		// Finalement, on dessine la case
		g.setColor(Color.BLACK);
		g.drawRect(caseRect.getX(), caseRect.getY(), caseRect.getW(),
				caseRect.getH());

		/* ON DESSINE LE PROPRIETAIRE */
		for (int i = 0; i < this.jeu.getNbJoueurs(); i++) {
			if (((CaseRue) uneCase).getProprietaire() == this.jeu.getJoueurs().get(i).getId()) {
				int id = this.jeu.getJoueurs().get(i).getId();
				int x = 0;
				int y = 0;
				if (uneCase.estSituee("BOTTOM")) {
					x = caseRect.getX() + caseRect.getW() / 2;
					y = caseRect.getY() - DessinPlateau.imageSize;
				} else if (uneCase.estSituee("LEFT")) {
					x = caseRect.getX() + caseRect.getW();
					y = caseRect.getY() + caseRect.getH() / 2;
				} else if (uneCase.estSituee("TOP")) {
					x = caseRect.getX() + caseRect.getW() / 2;
					y = caseRect.getH();
					;
				} else if (uneCase.estSituee("RIGHT")) {
					x = caseRect.getX() - DessinPlateau.imageSize;
					y = caseRect.getY() + caseRect.getH() / 2;
				}
				g.drawImage(this.images.getIconeJoueur(id), x, y,
						DessinPlateau.imageSize, DessinPlateau.imageSize, this);

			}
		}
	}
}
