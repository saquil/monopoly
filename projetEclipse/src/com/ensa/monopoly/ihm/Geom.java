package com.ensa.monopoly.ihm;

import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.primitives.Rectangle;
import com.ensa.monopoly.primitives.Vecteur;

/**
 * 
 * Classe contenant des fonctions utilitaires géomtriques basiques pour
 * faciliter le dessin Ces fonctions s'adaptent au redimensionnement de l'écran
 */
public class Geom {
	/**
	 * Permet de connaitre le centre euclidien d'une case
	 * 
	 * @param uneCase
	 *            La case que l'on souhaite traduire en coordonées X/Y
	 * @return Un vecteur contenant la position centrale de la case
	 */
	public static Vecteur positionCase(Case uneCase) {
		Rectangle rectDest = Geom.getCaseRectangle(uneCase);
		float targetX = (rectDest.getX() + rectDest.getW() / 2 - (DessinPlateau.imageSize / 2));
		float targetY = (rectDest.getY() + rectDest.getH() / 2 - (DessinPlateau.imageSize / 2));
		Vecteur target = new Vecteur(targetX, targetY).toRelativeCoordinates();
		return target;
	}

	/**
	 * Permet d'obtenir un rectangle (X/Y, W/H) à partir d'une case
	 * 
	 * @param uneCase
	 *            La case à partir de laquelle on souhaite extraire le rectangle
	 * @return Un rectangle contenant les coordonnées de la case
	 */
	public static Rectangle getCaseRectangle(Case uneCase) {

		float windowWidth = Fenetre.globalWidth;
		float windowHeight = Fenetre.globalHeight;

		float horizontalCaseW = ((float) windowWidth / 24.0f) * 2.0f;
		float horizontalLargeCaseW = ((float) windowWidth / 24.0f) * 3.0f;
		float horizontalCaseH = ((float) windowHeight / 24.0f) * 3.0f;

		float verticalCaseH = ((float) windowHeight / 24.0f) * 2.0f;
		float verticalCaseW = horizontalLargeCaseW;
		float verticalLargeCaseH = ((float) windowHeight / 24.0f) * 3.0f;

		int position = uneCase.getPosition();

		// Calcul de l'origine de la case
		Vecteur vOrigineCase = new Vecteur();
		if (uneCase.estSituee("BOTTOM")) {
			vOrigineCase.x = (position == 10) ? 0 : (windowWidth
					- horizontalLargeCaseW - (position * horizontalCaseW));
			vOrigineCase.y = windowHeight - horizontalCaseH;
		} else if (uneCase.estSituee("LEFT")) {
			vOrigineCase.x = 0;
			vOrigineCase.y = (position == 20) ? 0 : windowHeight
					- verticalLargeCaseH - ((position - 10) * verticalCaseH);
		} else if (uneCase.estSituee("TOP")) {
			vOrigineCase.y = 0;
			vOrigineCase.x = horizontalLargeCaseW + (position - 20 - 1)
					* horizontalCaseW;
		} else {
			vOrigineCase.x = windowWidth - horizontalLargeCaseW;
			vOrigineCase.y = (position == 20) ? 0 : verticalLargeCaseH
					+ ((position - 30 - 1) * verticalCaseH);
		}
		// Trouver la largeur / hauteur de chaque case en fonction de son index
		float caseWidth = 0;
		float caseHeight = 0;
		if (position > 0 && position < 10 || position > 20 && position < 30) {
			caseWidth = horizontalCaseW;
			caseHeight = horizontalCaseH;
		} else if (position > 10 && position < 20 || position > 30
				&& position < 40) {
			caseWidth = verticalCaseW;
			caseHeight = verticalCaseH;
		} else {
			caseWidth = horizontalLargeCaseW;
			caseHeight = verticalLargeCaseH;
		}
		Rectangle r = new Rectangle(vOrigineCase.x, vOrigineCase.y, caseWidth,
				caseHeight);
		return r;
	}

	/**
	 * Positionne un element au centre d'un rectangle et retourne les coordonées
	 * 
	 * @param container
	 *            L'objet contenant
	 * @param object
	 *            Le contenu
	 * @return Un vecteur comportant la position centrée du contenu dans le
	 *         contenant
	 */
	public static Vecteur getPositionCentre(Rectangle container,
			Rectangle object) {
		Vecteur res = new Vecteur();
		res.x = ((container.w - object.w) / 2.0f) + container.x;
		res.y = ((container.h - object.h) / 2.0f) + container.y;
		return res;
	}

}
