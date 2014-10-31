package fr.xmlstyle.exchangeengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import android.os.Environment;

public class ObjectXMLHandler{
	static org.jdom2.Document document;
	static String etat;
	static Element racine;
	public static void ouverture(){
		SAXBuilder sxb = new SAXBuilder();
		File fichier = Environment.getExternalStorageDirectory();
		fichier = new File(fichier+"/Objets.xml");
		try{
			document = sxb.build(fichier);
		}
		catch(Exception e){}
		racine = document.getRootElement();
	}
	public static ArrayList<ObjectInfo> lecture(){
		ouverture();
		ArrayList<ObjectInfo> listObjets = new ArrayList<ObjectInfo>();
		List<Element> list = racine.getChildren("objet");
		Iterator<Element> i = list.iterator();
		while(i.hasNext()){
			Element courant = i.next();
			String proprietaire = courant.getChildText("proprietaire");
			List<Element> listO = courant.getChildren("information");
			Iterator<Element> j = listO.iterator();
			while(j.hasNext()){
				Element courantO = j.next();
				ObjectInfo objetC = new ObjectInfo();
				objetC.setproprietaire(proprietaire);
				objetC.setcategorie(courantO.getChildText("categorie"));
				objetC.setcouleur(courantO.getChildText("couleur"));
				objetC.setechange(courantO.getChildText("echange"));
				objetC.setmel(courantO.getChildText("mel"));
				objetC.setnumero(courantO.getChildText("numero"));
				objetC.settitre(courantO.getChildText("titre"));
				objetC.setzone(courantO.getChildText("zone"));
				objetC.seturl(courantO.getChildText("url"));
				listObjets.add(objetC);
			}
		}
		return listObjets;
	}
	public static boolean ajouter(ObjectInfo newObject){
		ouverture();
		List<Element> list = racine.getChildren("objet");
		Iterator<Element> i = list.iterator();
		Element courant = null;
		boolean proprioExist = false;
		
		Element proprietaire = new Element("proprietaire");
		proprietaire.addContent(newObject.getproprietaire());
		Element information = new Element("information");
		Element mail = new Element("mel");
		mail.addContent(newObject.getmel());
		information.addContent(mail);
		Element titre = new Element("titre");
		titre.addContent(newObject.gettitre());
		information.addContent(titre);
		Element categorie = new Element("categorie");
		categorie.addContent(newObject.getcategorie());
		information.addContent(categorie);
		Element couleur = new Element("couleur");
		couleur.addContent(newObject.getcouleur());
		information.addContent(couleur);
		Element echange = new Element("echange");
		echange.addContent(newObject.getechange());
		information.addContent(echange);
		Element zone = new Element("zone");
		zone.addContent(newObject.getzone());
		information.addContent(zone);
		Element numero = new Element("numero");
		numero.addContent(newObject.getnumero());
		information.addContent(numero);
		Element url = new Element("url");
		url.addContent(newObject.geturl());
		information.addContent(url);
		
		while(i.hasNext()){
			courant = i.next();
			if(courant.getChild("proprietaire").getText().equals(newObject.getproprietaire())){
				proprioExist = true;
				courant.addContent(information);
			}
		}
		if(!proprioExist){
			Element objet = new Element("objet");
			objet.addContent(proprietaire);
			objet.addContent(information);
			racine.addContent(objet);
		}
		enregistrer();
		return true;
	}
	private static void enregistrer(){
		File fichier = Environment.getExternalStorageDirectory();
		fichier = new File(fichier+"/Objets.xml");
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		try {
			sortie.output(document,  new FileOutputStream(fichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}