package com.ensa.monopoly.ihm;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import com.ensa.monopoly.jeu.Jeu;

/**
 * 
 * Classe de gestion des fenetre
 *
 */
public class Fenetre extends JFrame {
	private static final long serialVersionUID = 1091579666915103712L;
	/**
	 * La largeur de la fenetre
	 */
	public static int globalWidth = 700;
	/**
	 * La hauteur de la fenêtre
	 */
	public static int globalHeight = 700;

	/**
	 * Le delta entre la largeur réelle de la fenetre et la zone de dessin
	 */
	public static int winDeltaW = -1000;
	/**
	 * Le delta entre la hauteur réelle de la fenetre et la zone de dessin
	 */
	public static int winDeltaH = -1000;


	/**
	 * Le plateau de dessin destiné à afficher les informations
	 */
	private DessinPlateau UI;

	public DessinPlateau getUI() {
		return UI;
	}

	public void setUI(DessinPlateau uI) {
		UI = uI;
	}

	public Jeu getJeu() {
		return this.monopoly;
	}

	public void setJeu(Jeu j) {
		this.monopoly = j;
	}

	private Jeu monopoly;

	/**
	 * Le constructeur de la fenetre de jeu et le point d'entrée de l'application graphique
	 */
	public Fenetre(Jeu jeu) {
		setSize(700, 700);
		this.monopoly = jeu;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUI(new DessinPlateau(this.monopoly));
		this.add(this.getUI());
		this.pack();
		this.setVisible(true);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				float W = (float) e.getComponent().getWidth();
				float H = (float) e.getComponent().getHeight();
				if (Fenetre.winDeltaW == -1000) {
					Fenetre.winDeltaW = (int) W - Fenetre.globalWidth;
					Fenetre.winDeltaH = (int) H - Fenetre.globalHeight;
				}
				if (W < 700 + Fenetre.winDeltaW || H < 700 + Fenetre.winDeltaH) {
					W = 700 + Fenetre.winDeltaW;
					H = 700 + Fenetre.winDeltaH;
				}
				Fenetre.globalWidth = (int) W - Fenetre.winDeltaW;
				Fenetre.globalHeight = (int) H - Fenetre.winDeltaH;
				setSize((int) W, (int) H);

			}
		});
	}

}
