package com.ensa.monopoly.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.primitives.Rectangle;
import com.ensa.monopoly.primitives.Vecteur;

/**
 * 
 * Classe de gestion de l'affichage des polices
 * 
 */
public class Fonts {
	/**
	 * Affiche une chaine de caractere dynamiquement � l'aide de l'objet
	 * graphics
	 * 
	 * @param g
	 *            L'objet graphics pour dessiner la chaine
	 * @param s
	 *            Le contenu de la chaine de caract�re
	 * @param x
	 *            La position x en coordon�es euclidienne de la chaine de
	 *            caract�re
	 * @param y
	 *            La position y en coordon�es euclidienne de la chaine de
	 *            caract�re
	 * @param width
	 *            La largeur maximale pour cette chaine de caract�re
	 */
	public static void drawString(Graphics g, String s, int x, int y, int width) {
		FontMetrics fm = g.getFontMetrics();
		int lineHeight = fm.getHeight() - 4; // -4 -> Notre "line height"
		String[] words = s.split(" ");
		// Cet �l�ment va contenir notre liste de mot. Chaque element du tableau
		// sera une chaine de caract�re qui s'affichera correctement
		// dans l'espace width
		ArrayList<String> wordList = new ArrayList<String>();
		String currentWord = "";
		int currentSize = 0;
		for (int i = 0; i < words.length; i++) {
			String word = words[i] + " ";
			int wordWidth = fm.stringWidth(word + " ");
			// On ajoute necessairement le mot � la liste
			currentWord += word;
			currentSize += wordWidth;
			// Si la taille du mot depasse la taille autoris�e
			// Si c'est le dernier mot de la liste
			// Si la taille de ce mot suivi du prochain
			// On l'ajoute � notre liste d'affichage ligne � ligne
			if (currentSize > width
					|| i == words.length - 1
					|| ((i < words.length - 1) && currentSize
							+ fm.stringWidth(words[i + 1] + " ") > width)) {
				wordList.add(currentWord);
				currentWord = "";
				currentSize = 0;
			}
		}
		// On parcourt la liste des mots et on les affiche ligne par ligne, en
		// partant de notre position (x,y)
		for (int i = 0; i < wordList.size(); i++) {
			String word = wordList.get(i);
			// On effectue correctement nos retours � la ligne
			int placeY = y + i * lineHeight;
			// On centre notre mot dans l'espace width
			int placeX = x + (width - fm.stringWidth(word)) / 2;
			g.drawString(word, placeX, placeY);
		}
	}

	/**
	 * Fixe la taille de la police de caract�res
	 * @param g L'objet graphics 
	 * @param size La taille souhait� en pixels pour la police
	 * @param nolimit Si ce param�tre vaut TRUE, alors la taille de la police sera moins limit� vers les hautes valeurs
	 * @return
	 */
	public static float setFontSize(Graphics g, float size, boolean nolimit) {
		// Maximisation de l'affichage du nom des rues en fonction de la taille
		// ecran
		// Les taille de police et facteurs ont �t� obtenus apr�s des tests

		int windowWidth = Fenetre.globalWidth;
		int windowHeight = Fenetre.globalHeight;
		float fontFactor = ((float) windowWidth * (float) windowHeight)
				/ (600 * 600);
		if (nolimit == false) {
			fontFactor = (size * fontFactor * 0.8f > 11) ? 11 : (size
					* fontFactor * 0.8f);
		} else {
			fontFactor = (size * fontFactor * 0.8f > 14) ? 14 : (size
					* fontFactor * 0.8f);
		}
		// Calcul et affichage du nom de la rue
		Graphics2D g2d = (Graphics2D) g;
		Font font = new Font("Arial", Font.PLAIN, (int) fontFactor);
		g2d.setFont(font);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		return fontFactor;
	}

	/**
	 * Dessine une chaine caract�re en haut et en bas d'un rectangle graphique 
	 * @param uneCase La case � partir de laquelle on souhaite afficher la chaine
	 * @param caseRect Le rectangle de la case ou l'on souhaite ecrire
	 * @param offsetRect La marge interieure pour afficher la chaine
	 * @param g L'objet graphics
	 * @param s1 La chaine de caract�re que l'on souhaite afficher en haut de la case
	 * @param s2 L'objet graphics que l'on souhaite afficher en bas de la case
	 */
	public static void drawTopAndBottomString(Case uneCase, Rectangle caseRect,
			Rectangle offsetRect, Graphics g, String s1, String s2) {

		float fontFactor = Fonts.setFontSize(g, 12, false);
		// Calcul de la position X/Y de la chaine de caractere du haut et du bas
		int position = uneCase.getPosition();
		Vecteur vTopString = new Vecteur(caseRect.x, caseRect.y);
		Vecteur vBottomString = new Vecteur(0, 0);
		g.setColor(Color.BLACK);
		float fontCaseWidth = caseRect.w;
		if (position > 0 && position < 10) {
			vTopString.x += 3;
			vTopString.y += offsetRect.h + 10;
			float offset = caseRect.h - offsetRect.h - fontFactor;
			vBottomString.y = vTopString.y + offset;
		} else if (position > 10 && position < 20) {
			vTopString.x += 3;
			vTopString.y += 10;
			vBottomString.y = vTopString.y + caseRect.h - fontFactor;
			fontCaseWidth = caseRect.w - offsetRect.w;
		} else if (position > 20 && position < 30) {
			vTopString.x += 3;
			vTopString.y += 10;
			float offset = caseRect.h - offsetRect.h - fontFactor;
			vBottomString.y = vTopString.y + offset;
		} else {
			vTopString.x += offsetRect.w + 3;
			vTopString.y += 10;
			fontCaseWidth = caseRect.w - offsetRect.w;
			vBottomString.y = vTopString.y + caseRect.h - fontFactor;
		}
		// Les prix sont align�s en X avec le nom des rues
		vBottomString.x = vTopString.x;
		Fonts.drawString(g, s1, vTopString.getX(), vTopString.getY(),
				(int) fontCaseWidth);

		Fonts.drawString(g, s2, vBottomString.getX(), vBottomString.getY(),
				(int) fontCaseWidth);
	}

}
