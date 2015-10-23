package com.ensa.monopoly.ihm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.cases.CaseCommerciale;
import com.ensa.monopoly.cases.CaseCompagnie;
import com.ensa.monopoly.cases.CaseGare;
import com.ensa.monopoly.cases.CaseRue;
import com.ensa.monopoly.entites.Joueur;

/**
 * 
 * Classe de gestion des messages popups
 * 
 */
public class MessagePopup extends JDialog {
	private static final long serialVersionUID = -7080961933301246652L;

	/**
	 * Pose une question et récupère la réponse
	 * 
	 * @param question
	 *            La question que l'on souhaite poser
	 * @return Un booléen contenant la réponse
	 */
	public boolean question(String question) {
		this.setModal(false);
		if (question.length() > 0) {
			String[] reponses = { "Oui", "Non" };
			Integer reponse = JOptionPane.showOptionDialog(this, question,
					"Question", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, reponses, reponses[0]);
			if (reponse == 2) {
				System.exit(0);
			} else if (reponse == 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Permet d'afficher les boites de dialogues lors de l'achat d'une maison
	 * 
	 * @param uneCase
	 *            La case concernée par l'achat
	 * @param question
	 *            La question particulière que l'on souhaite associer à l'achat
	 * @return
	 */
	public boolean messageAchat(Case uneCase, String question) {
		this.setModal(false);

		// On supprime tout de panel
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(new BorderLayout());

		JTextField couleur = new JTextField();
		JTextArea texteCarte = new JTextArea();

		couleur.setEditable(false);
		texteCarte.setEditable(false);

		if (uneCase instanceof CaseRue) {
			CaseRue caseRue = (CaseRue) uneCase;
			couleur.setBackground(caseRue.getCouleur());
			this.getContentPane().add(couleur, BorderLayout.NORTH);
			texteCarte.append(caseRue.getNom() + "\nPrix : "
					+ caseRue.getPrix() + "\n\n" + question);
		} else if (uneCase instanceof CaseGare) {
			CaseGare caseGare = (CaseGare) uneCase;
			texteCarte.append(caseGare.getNom() + "\nPrix : "
					+ caseGare.getPrix() + "\n\n" + question);
		} else if (uneCase instanceof CaseCompagnie) {
			CaseCompagnie caseCompagnie = (CaseCompagnie) uneCase;
			texteCarte.append(caseCompagnie.getNom() + "\nPrix : "
					+ caseCompagnie.getPrix() + "\n\n" + question);
		}
		this.getContentPane().add(texteCarte, BorderLayout.SOUTH);

		String[] reponses = { "Oui", "Non" };
		Integer reponse = JOptionPane.showOptionDialog(null,
				this.getContentPane(), "Achat",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, reponses, reponses[0]);
		if (reponse == 2) {
			System.exit(0);
		} else if (reponse == 1) {
			return false;
		}
		return true;
	}

	/**
	 * Une fenetre popup qui affiche un message modal
	 * 
	 * @param message
	 */
	public void message(String message) {
		this.setModal(false);
		if (message.length() > 0) {
			String[] options = { "OK" };
			Integer option = JOptionPane.showOptionDialog(this, message,
					"Message", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 1) {
				System.exit(0);
			}
		}
	}

	/**
	 * Une fenetre qui permet de recuperer le prix proposé par un joueur aux
	 * encheres
	 * 
	 * @param message
	 * @return un prix
	 */
	public Integer getPrixProposition(String message) {
		this.setModal(false);
		String nombre = JOptionPane.showInputDialog(this, message);
		if (nombre != null && nombre.length() > 0) {
			return Integer.parseInt(nombre);
		}
		return 0;
	}

	/**
	 * Une fenetre permettant de choisir le nombre de joueur
	 * 
	 * @return Le nombre de joueurs choisi
	 */
	public Integer choixNombreJoueurs() {
		this.setModal(false);
		Integer[] listNbJoueurs = { 2, 3, 4 };
		Integer nbJoueurs = (Integer) JOptionPane.showInputDialog(this,
				"Choisissez le nombre de joueurs", "Nombre de joueurs",
				JOptionPane.PLAIN_MESSAGE, null, listNbJoueurs, 2);
		return nbJoueurs;
	}

	/**
	 * Une fenetre permettant de choisir le scenario (pour mettre en avant les
	 * différentes situations critiques de jeu)
	 * 
	 * @return Le scenario choisi
	 */
	public Integer choixScenario() {
		this.setModal(false);
		Integer[] nbScenario = { 1, 2, 3 };
		Integer nbJoueurs = (Integer) JOptionPane
				.showInputDialog(
						this,
						"Choisissez votre scenario\n1 - Partie normale\n2 - Difficulté 1\n3 - Difficulté 2",
						"Choix du scenario", JOptionPane.PLAIN_MESSAGE, null,
						nbScenario, 1);
		return nbJoueurs;
	}

	/**
	 * Selectionne un joueur avec lequel effectuer une transaction
	 * 
	 * @param unJoueur
	 *            Le joueur que souhaite faire la transaction
	 * @param listeJoueurs
	 *            La liste globale des joueurs
	 * @return Un joueur selectionné pour effectuer une transaction
	 */
	public int selectionJoueur(Joueur unJoueur, ArrayList<Joueur> listeJoueurs) {
		ArrayList<String> resultat = new ArrayList<String>();
		for (int i = 0; i < listeJoueurs.size(); i++) {
			if (listeJoueurs.get(i).getId() != unJoueur.getId()) {
				resultat.add("Joueur " + listeJoueurs.get(i).getId());
			}
		}
		String choixJoueur = (String) JOptionPane.showInputDialog(this,
				"Choisissez le joueur avec qui effectuer une transaction",
				"Choix du joueur", JOptionPane.PLAIN_MESSAGE, null,
				resultat.toArray(), 1);
		int idJoueur = Character.digit(choixJoueur.charAt(7), 10);
		return idJoueur;
	}

	/**
	 * Une fenetre permettant de terminer le tour
	 * 
	 * @return Le choix du joueur
	 */
	public String terminerLeTour(
			ArrayList<CaseCommerciale> casesConstructibles,
			ArrayList<CaseCommerciale> casesCommercialisables) {
		this.setModal(false);
		ArrayList<String> actionFinale = new ArrayList<String>();
		actionFinale.add("0 - Terminer mon tour");
		if (casesConstructibles.size() > 0) {
			actionFinale.add("1 - Construire des maisons");
		}
		if (casesCommercialisables.size() > 0) {
			actionFinale.add("2 - Effectuer une transaction avec un autre joueur");
		}

		actionFinale.add("3 - Quitter le jeu");
		String action = (String) JOptionPane.showInputDialog(this,
				"Choisissez une action", "Que souhaitez vous faire ?",
				JOptionPane.PLAIN_MESSAGE, null, actionFinale.toArray(), 1);
		return action;
	}

	/**
	 * Permet de selectionner une liste de proprietés, soit pour acheter une
	 * maison, les vendre ou effectuer une transaction
	 * 
	 * @param casesCommerciales La liste des cases possedées par le joueur
	 * @param joueur Le joueur en question
	 * @param title Le titre à afficher pour cette action
	 * @param action L'action qui valide le choix
	 * @param type Le type d'action (ACHAT | VENTE | TRANSACTION)
	 * @return Une liste d'entiers comportant les indexes des cases pour lesquels le joueur a effectué une selection
	 */
	public ArrayList<Integer> selectionPropriete(
			ArrayList<CaseCommerciale> casesCommerciales, Joueur joueur,
			String title, String action, String type) {

		this.setModal(false);
		ArrayList<Integer> idsProprietesVendre = new ArrayList<Integer>();

		Integer[] idsProprietes = new Integer[casesCommerciales.size()];
		String[] nomsProprietes = new String[casesCommerciales.size()];
		if (type.equals("ACHAT")) {
			for (int i = 0; i < casesCommerciales.size(); i++) {
				CaseRue r = (CaseRue) casesCommerciales.get(i);
				idsProprietes[i] = r.getPosition();
				nomsProprietes[i] = r.getNom() + " (" + r.prixMaison()
						+ "€ par maison / 4 maisons pour constuire un hôtel)";
			}
		} else if (type.equals("VENTE")) {
			for (int i = 0; i < casesCommerciales.size(); i++) {
				CaseRue r = (CaseRue) casesCommerciales.get(i);
				idsProprietes[i] = r.getPosition();
				nomsProprietes[i] = r.getNom() + " (Valeur marchande "
						+ r.valeurHypotheque() + "€)";
			}
		} else if (type.equals("TRANSACTION")) {
			for (int i = 0; i < casesCommerciales.size(); i++) {
				CaseCommerciale c = (CaseCommerciale) casesCommerciales.get(i);
				idsProprietes[i] = c.getPosition();
				nomsProprietes[i] = c.getNom() + " (Valeur marchande "
						+ c.valeurHypotheque() + "€)";
			}
		}

		JCheckBox checkBox;
		JPanel panelTerrains = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 1);
		panelTerrains.setLayout(gridLayout);

		for (int i = 0; i < nomsProprietes.length; i++) {
			checkBox = new JCheckBox(nomsProprietes[i]);
			checkBox.setSelected(false);
			panelTerrains.add(checkBox);
		}

		if (title.length() > 0) {
			String[] options = { action };
			Integer option = JOptionPane.showOptionDialog(this, panelTerrains,
					title, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 1) {
				System.exit(0);
			}

			if (option == 0) {
				for (int i = 0; i < casesCommerciales.size(); i++) {
					JCheckBox checkboxSelection = (JCheckBox) panelTerrains
							.getComponent(i);
					if (checkboxSelection.isSelected()) {
						idsProprietesVendre.add(casesCommerciales.get(i)
								.getPosition());
					}
				}
			}
		}

		return idsProprietesVendre;
	}
}