package com.ensa.monopoly.jeu;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ensa.monopoly.cartes.CarteChance;
import com.ensa.monopoly.cartes.CarteCommunaute;
import com.ensa.monopoly.cartes.Pioche;
import com.ensa.monopoly.cases.Case;
import com.ensa.monopoly.cases.CaseAllerPrison;
import com.ensa.monopoly.cases.CaseChance;
import com.ensa.monopoly.cases.CaseCommerciale;
import com.ensa.monopoly.cases.CaseCommunaute;
import com.ensa.monopoly.cases.CaseCompagnie;
import com.ensa.monopoly.cases.CaseDepart;
import com.ensa.monopoly.cases.CaseGare;
import com.ensa.monopoly.cases.CaseParcGratuit;
import com.ensa.monopoly.cases.CasePrison;
import com.ensa.monopoly.cases.CaseRue;
import com.ensa.monopoly.cases.CaseTaxe;
import com.ensa.monopoly.entites.Joueur;
import com.ensa.monopoly.entites.PotCommun;
import com.ensa.monopoly.ihm.ConteneurImages;

/**
 * 
 * Le plateau de jeu
 * 
 */
public class PlateauDeJeu {
	/**
	 * La liste des cases du plateau de jeu
	 */
	private ArrayList<Case> cases;
	/**
	 * La pioche de cartes
	 */
	private Pioche pioche;
	/**
	 * L'objet pot commun, qui est rapporté lorsqu'un joueur passe sur la case
	 * "parc gratuit"
	 */
	private PotCommun potCommun;

	/**
	 * Le nombre de cases sur le plateau
	 */
	private final static int nbCases = 40;
	/**
	 * La couleur de fond du plateau
	 */
	private Color bgColor = Color.black;

	/**
	 * Chargement du plateau de jeu à partir d'un fichier XML
	 */
	public PlateauDeJeu() {
		this.cases = new ArrayList<Case>();
		this.potCommun = new PotCommun();
		try {
			File fXmlFile = new File("resources/skin1.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			Element colorContainer = (Element) doc.getElementsByTagName(
					"bgcolor").item(0);
			Element imageContainer = (Element) doc.getElementsByTagName(
					"images").item(0);
			Element casesContainer = (Element) doc
					.getElementsByTagName("cases").item(0);
			Element carteChanceContainer = (Element) doc.getElementsByTagName(
					"cartes_chance").item(0);
			Element carteCommunauteContainer = (Element) doc
					.getElementsByTagName("cartes_communaute").item(0);

			// Couleur de fond du plateau
			int red = Integer.parseInt(colorContainer.getAttribute("red"));
			int green = Integer.parseInt(colorContainer.getAttribute("green"));
			int blue = Integer.parseInt(colorContainer.getAttribute("blue"));
			this.setBgColor(new Color(red, green, blue));

			// Chargement des cartes
			this.pioche = new Pioche();
			// Chargement des cartes chance
			NodeList xmlCartesChanceList = carteChanceContainer
					.getElementsByTagName("carte_chance");
			for (int temp = 0; temp < xmlCartesChanceList.getLength(); temp++) {
				Node xmlCarte = xmlCartesChanceList.item(temp);
				if (xmlCarte.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) xmlCarte;
					String nom = getTagValue("nom", eElement);
					int effetArgent = Integer.parseInt(getTagValue(
							"effet_argent", eElement));
					int effetPosition = Integer.parseInt(getTagValue(
							"effet_position", eElement));
					CarteChance nouvelleCarteChance = new CarteChance(nom,
							effetArgent, effetPosition);
					this.pioche.ajouterCarteChance(nouvelleCarteChance);
				}
			}

			// Chargement des cartes communaute
			NodeList xmlCartesCommunauteList = carteCommunauteContainer
					.getElementsByTagName("carte_communaute");
			for (int temp = 0; temp < xmlCartesCommunauteList.getLength(); temp++) {
				Node xmlCarte = xmlCartesCommunauteList.item(temp);
				if (xmlCarte.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) xmlCarte;
					String nom = getTagValue("nom", eElement);
					int effetArgent = Integer.parseInt(getTagValue(
							"effet_argent", eElement));
					int effetPosition = Integer.parseInt(getTagValue(
							"effet_position", eElement));
					CarteCommunaute nouvelleCarteCommunaute = new CarteCommunaute(
							nom, effetArgent, effetPosition);
					this.pioche.ajouterCarteCommunaute(nouvelleCarteCommunaute);
				}
			}
			// Chargement des images
			NodeList xmlImageList = imageContainer
					.getElementsByTagName("image");
			ConteneurImages.icones = new HashMap<String, Image>();
			for (int temp = 0; temp < xmlImageList.getLength(); temp++) {
				Node xmlCase = xmlImageList.item(temp);
				if (xmlCase.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) xmlCase;
					String imageId = eElement.getAttribute("id");
					String imageSrc = getTagValue("src", eElement);
					try {
						ConteneurImages.icones.put(imageId,
								ImageIO.read(new File(imageSrc)));
					} catch (IOException e) {
						System.out.println("Impossible de charger l'image '"
								+ imageSrc + "'");
					}
				}
			}

			// Chargement des cases
			NodeList xmlCaseList = casesContainer.getElementsByTagName("case");
			for (int temp = 0; temp < xmlCaseList.getLength(); temp++) {
				Node xmlCase = xmlCaseList.item(temp);
				if (xmlCase.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) xmlCase;
					String className = eElement.getAttribute("class");

					if (className.equals("CaseRue")) {
						String nom = getTagValue("nom", eElement);
						int prix = Integer.parseInt(getTagValue("prix",
								eElement));
						Color couleur = Color.decode(getTagValue("couleur",
								eElement));
						this.cases.add(new CaseRue(temp, nom, couleur, prix));
					} else if (className.equals("CaseCommunaute")) {
						this.cases.add(new CaseCommunaute(temp, getTagValue(
								"nom", eElement)));
					} else if (className.equals("CaseTaxe")) {
						int prix = Integer.parseInt(getTagValue("prix",
								eElement));
						this.cases.add(new CaseTaxe(temp, getTagValue("nom",
								eElement), prix));
					} else if (className.equals("CaseChance")) {
						this.cases.add(new CaseChance(temp, getTagValue("nom",
								eElement)));
					} else if (className.equals("CaseGare")) {
						int prix = Integer.parseInt(getTagValue("prix",
								eElement));
						this.cases.add(new CaseGare(temp, getTagValue("nom",
								eElement), prix));
					} else if (className.equals("CasePrison")) {
						this.cases.add(new CasePrison(temp, getTagValue("nom",
								eElement)));
					} else if (className.equals("CaseCompagnie")) {
						int prix = Integer.parseInt(getTagValue("prix",
								eElement));
						this.cases.add(new CaseCompagnie(temp, getTagValue(
								"nom", eElement), prix));
					} else if (className.equals("CaseParcGratuit")) {
						this.cases.add(new CaseParcGratuit(temp, getTagValue(
								"nom", eElement)));
					} else if (className.equals("CaseAllerPrison")) {
						this.cases.add(new CaseAllerPrison(temp, getTagValue(
								"nom", eElement)));
					} else if (className.equals("CaseDepart")) {
						this.cases.add(new CaseDepart(temp, getTagValue("nom",
								eElement)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Retourne TRUE si un quartier est sous hypotheque
	 * @param caseRue Une rue qui fait partie du quartier qu'on veut tester
	 * @return TRUE si une des rues du quartier est sous hypotheque
	 */
	public boolean quartierSousHypotheque(CaseRue caseRue) {
		boolean quartierSousHypotheque = false;
		for (Case uneCase : getCases()) {
			if (uneCase instanceof CaseRue) {
				if (((CaseRue) uneCase).getCouleur() == caseRue.getCouleur()) {
					if (((CaseRue) uneCase).isSousHypotheque()) {
						quartierSousHypotheque = true;
					}
				}
			}
		}
		return quartierSousHypotheque;
	}

	/**
	 * Retourne une liste potentielles de cartes hypothequables (si le joueur est en mauvaise posture)
	 * @param unJoueur Le joueur sur lequel on va tester les proprietés
	 * @return Une liste de cases hypothequables
	 */
	public ArrayList<CaseCommerciale> casesHypothequables(Joueur unJoueur) {
		ArrayList<CaseCommerciale> resultat = new ArrayList<CaseCommerciale>();
		for (int i = 0; i < cases.size(); i++) {
			if (cases.get(i) instanceof CaseCommerciale
					&& ((CaseCommerciale) cases.get(i)).getProprietaire() == unJoueur
							.getId()) {
				resultat.add((CaseCommerciale) cases.get(i));
			}
		}
		return resultat;
	}

	/**
	 * Retourne une liste de cases que le joueur peut négocier (terrains nus)
	 * @param unJoueur Le joueur qui possede eventuellement des terrains nus
	 * @return La liste des terrains nus possedés par le joueur et donc commercialisables
	 */
	public ArrayList<CaseCommerciale> casesCommercialisables(Joueur unJoueur) {
		ArrayList<CaseCommerciale> resultat = new ArrayList<CaseCommerciale>();
		for (int i = 0; i < cases.size(); i++) {
			if (cases.get(i) instanceof CaseCommerciale) {
				CaseCommerciale caseCommerciale = ((CaseCommerciale) cases
						.get(i));
				if (caseCommerciale.getProprietaire() == unJoueur.getId()) {
					// La case appartient au joueur
					if (caseCommerciale instanceof CaseRue
							&& ((CaseRue) caseCommerciale).getNbMaison() == 0) {
						resultat.add(caseCommerciale);
					} else if (!(caseCommerciale instanceof CaseRue)) {
						resultat.add(caseCommerciale);
					}
				}
			}
		}
		return resultat;
	}
/**
 * Retourne une liste de cases sur lesquelles le joueur peut construire des habitations
 * @param unJoueur Le joueur pour lequel on va tester les proprietés
 * @return La liste des cases sur lequelles le joueur peut construire des maisons
 */
	public ArrayList<CaseCommerciale> casesConstructibles(Joueur unJoueur) {
		ArrayList<CaseCommerciale> resultat = new ArrayList<CaseCommerciale>();
		for (int i = 0; i < cases.size(); i++) {
			if (cases.get(i) instanceof CaseRue) {
				CaseRue caseRue = ((CaseRue) cases.get(i));
				if (caseRue.getProprietaire() == unJoueur.getId()) {
					if (this.quartierMonopolise(caseRue, unJoueur) == true) {
						ArrayList<CaseRue> quartier = this
								.getCasesQuartier(((CaseRue) cases.get(i)));

						int min = 5;
						for (int j = 0; j < quartier.size(); j++) {
							min = (quartier.get(j).getNbMaison() < min) ? quartier
									.get(j).getNbMaison() : min;
						}
						// On est obligés de construire les maisons de manière
						// uniforme
						// 5 Maisons (un hotel représente le maximum)
						for (int j = 0; j < quartier.size(); j++) {
							if (quartier.get(j).getNbMaison() <= min
									&& quartier.get(j).getNbMaison() < 5) {
								// array_unique
								boolean bItemExiste = false;
								for (int k = 0; k < resultat.size(); k++) {
									if (resultat.get(k).getPosition() == quartier
											.get(j).getPosition()) {
										bItemExiste = true;
									}
								}
								if (!bItemExiste) {
									resultat.add(quartier.get(j));
								}

							}
						}
					}
				}
			}
		}
		return resultat;
	}

	/**
	 * Retourne pour une rue donnée la liste des cases correspondant au meme quartier
	 * @param uneCase La case qui fait partie du quartier que l'on veut tester
	 * @return La liste des cases faisant partie du quartier de la case testée
	 */
	public ArrayList<CaseRue> getCasesQuartier(CaseRue uneCase) {
		ArrayList<CaseRue> resultat = new ArrayList<CaseRue>();
		for (int i = 0; i < cases.size(); i++) {
			if (cases.get(i) instanceof CaseRue) {
				if (((CaseRue) cases.get(i)).getCouleur().equals(
						uneCase.getCouleur())) {
					resultat.add(((CaseRue) cases.get(i)));
				}
			}
		}
		return resultat;
	}
/**
 * Permet de savoir si un joueur possede toutes les proprietés d'un même quartier
 * @param caseRue Une rue du quartier que l'on veut tester
 * @param joueur Le joueur que l'on veut tester
 * @return Retourne TRUE si le joueur possède toutes les rues du quartier
 */
	public boolean quartierMonopolise(CaseRue caseRue, Joueur joueur) {
		boolean estProprietaire = true;
		ArrayList<CaseRue> casesQuartier = this.getCasesQuartier(caseRue);
		for (int i = 0; i < casesQuartier.size(); i++) {
			if ((casesQuartier.get(i)).getProprietaire() != joueur.getId()) {
				estProprietaire = false;
			}
		}
		return estProprietaire;
	}
/**
 * Permet de savoir si l'une des compagnies est sous hypotheque
 * @return Renvoie TRUE si l'une des comapgnies est sous hypotheque
 */
	public boolean compagnieSousHypotheque() {
		boolean bSousHypotheque = false;
		for (Case uneCase : getCases()) {
			if (uneCase instanceof CaseCompagnie
					&& ((CaseCompagnie) uneCase).isSousHypotheque()) {
				bSousHypotheque = true;
			}
		}
		return bSousHypotheque;
	}
/**
 * Retourne le nombre de compagnies possedées par un joueur
 * @param joueurId L'id unique du joueur que l'on veut tester
 * @return Le nombre de compagnies possedées par le joueur
 */
	public int nombreCompagniesPossedees(int joueurId) {
		int nbCompagnies = 0;
		for (Case uneCase : getCases()) {
			if (uneCase instanceof CaseCompagnie
					&& ((CaseCompagnie) uneCase).getProprietaire() == joueurId) {
				nbCompagnies++;
			}
		}
		return nbCompagnies;
	}

	/**
	 * Permet de connaitre le nombre de gares que possède un joueur
	 * @param joueurId L'identifiant du joueur que l'on souhaite tester
	 * @return Le nombre de gares que possède un joueur
	 */
	public int nombreGarePossedees(int joueurId) {
		int nbGares = 0;
		for (Case uneCase : getCases()) {
			if (uneCase instanceof CaseGare
					&& ((CaseGare) uneCase).getProprietaire() == joueurId) {
				nbGares++;
			}
		}
		return nbGares;
	}

	/**
	 * Permet de savoir si l'une des gares est sous hypotheque
	 * @return Retourne TRUE si l'une des gare est sous hypotheque
	 */
	public boolean gareSousHypotheque() {
		boolean bSousHypotheque = false;
		for (Case uneCase : getCases()) {
			if (uneCase instanceof CaseGare
					&& ((CaseGare) uneCase).isSousHypotheque()) {
				bSousHypotheque = true;
			}
		}
		return bSousHypotheque;
	}



	/** 
	 * Retourne la valeur d'un tag en lecture d'un fichier XML
	 * @param sTag Le nom du tag
	 * @param eElement La node XML
	 * @return La valeur du tag
	 */
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

	public ArrayList<Case> getCases() {
		return this.cases;
	}

	public int getNbcases() {
		return nbCases;
	}

	public Case getCase(int needle) {
		if (needle > -1 && needle < 40) {
			return this.cases.get(needle);
		}
		return null;
	}
	

	public PotCommun getPotCommun() {
		return potCommun;
	}

	public void setPotCommun(PotCommun potCommun) {
		this.potCommun = potCommun;
	}

	public Pioche getPioche() {
		return pioche;
	}

	public void setPioche(Pioche pioche) {
		this.pioche = pioche;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}



}
