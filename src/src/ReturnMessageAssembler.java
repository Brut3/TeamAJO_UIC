package src;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReturnMessageAssembler {

	protected static String SENDER_ADDRESS = "test@elevatorspot.appspotmail.com";

	private String subject = "Kiitos spottauksesta!";
	
	public Message assembleMessage(Address recipient, String msgSubject, String msgBody) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Message msg = null;

		try {
			InternetAddress recipientAdress = new InternetAddress(recipient.toString());
			msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(SENDER_ADDRESS, "TEST"));      
			msg.addRecipient(Message.RecipientType.TO, recipientAdress);
			msg.setSubject(msgSubject);
			msg.setText(msgBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public String generateMessageContent(List<String> params) {

		//TODO Tanne voi lisailla parametrien kasittelyyn liittyvaa logiikkaa.


		String content = "";

		if(params.size() != 6 || params.size() != 1) {
			setSubject("Spottauksesi oli virheellinen");
			content = setContentToError(params);
		}
		
		//Jos k�ytt�j� l�hett�� pelk�n osoitteen, niin siin� ei oo parsittavaa
		else if(params.get(0).equals("<kuva>")) {
			setSubject("Kiitos spottauksesta!");
			content = setContentToThanks(params);
		}
		else if(params.size()==1) {
			setSubject("Lis�tietoja hissist� osoitteessa " + params.get(0));
			content = setContentToInfo(params);
		}
		
		//Jos kyseess� tavallinen spottaus
		else if(params.get(0).equals("HISSI")) {
			content = setContentToSpotting(params, false);
		}
		
		//Jos kyseess� on tietojen t�ydennys
		else if(isValidID(params.get(0))) {
			content = setContentToComplement(params);
		}
		
		return content;
	}

	

	public String generateSubject() {
		return subject;
	}
	
	private void setSubject(String newSubject) {
		subject = newSubject;
	}
	
	private String setContentToInfo(List<String> params) {
		String content = "";
		content += "Hei!\n\nAntamastasi osoitteesta l�ytyi yksi hissi. Sen tiedot ovat t�ss�:\n";
		content += "Osoite: " + params.get(0) +"\n";
		content += "Valmistaja: KONE\nValmistusvuosi: 1989\n";
		content += "Kerroksia: \n";
		content += "K�ytt�jien kommentit:\n";
		content += "\"Aika korkea ja ruma hissi, en muista montako kerrosta siin� oli.\" -rane68,\n";
		content += "\"Kiihdytys oli aivan liian nopea, minulle tuli paha olo!\" -Pipsa<3\n\n";
		
		content += "Jos tiedot ovat virheellisi�, voit korjata ne vastaamalla t�h�n viestiin n�in:\n\n";
		content += "758933#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";

		content += "Oikeiden tietojen osalta j�t� kohta tyhj�ksi! Muista kirjata hissin tunnistenumero" +//TODO voidaanko k�ske� j�tt�� tyhj�ksi?
				" 758933 viestin alkuun, esimerkin mukaisesti.";

		content +="Terveisin,\nElevator Spotting - your friend in life�s ups and downs.";
		return content;
	}
	
	private String setContentToSpotting(List<String> params, boolean errors) {
		String content ="";
		content += "Hei, rane68!\n\nSpottaamasi hissi on nyt lis�tty ElevatorSpottingiin tiedoilla:\n";
		content += "Osoite: " + params.get(1) + "\n";
		content += "Valmistaja: " + params.get(2) + "\n";
		content += "Valmistusvuosi: " + params.get(3) + "\n";
		content += "Kerrosten lukum��r�: " + params.get(4) + "\n";
		content += "Omat kommenttisi: \"" + params.get(5) + "\"\n\n";

		content += "Jos tiedot ovat virheellisi�, voit korjata ne vastaamalla t�h�n viestiin n�in:\n";
		content += "758933#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";

		content += "Muista kirjata hissin tunnistenumero 758933 viestin alkuun, esimerkin mukaisesti.\n\n";

		content += "Kiitos spottauksesta!\nElevator Spotting - your friend in life�s ups and downs.";

		return content;
	}
	
	private String setContentToComplement(List<String> params) {
		String content ="";
		content += "Hei!\n\nKiitos t�ydennyksest� hissin " + params.get(0) + " tietoihin!:\n";
		content += "Hissi on nyt tallennettu seuraavin tiedoin:\n";
		content += "Osoite: " + params.get(1) + "\n";
		content += "Valmistaja: " + params.get(2) + "\n";
		content += "Valmistusvuosi: " + params.get(3) + "\n";
		content += "Kerrosten lukum��r�: " + params.get(4) + "\n";
		content += "Omat kommenttisi: \"" + params.get(5) + "\"\n\n";

		content += "Jos tiedot ovat virheellisi�, voit korjata ne vastaamalla t�h�n viestiin n�in:\n";
		content += params.get(0) +"#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";

		content += "Muista kirjata hissin tunnistenumero "+ params.get(0) +" viestin alkuun, esimerkin mukaisesti.\n\n";

		content += "Kiitos spottauksesta!\nElevator Spotting - your friend in life�s ups and downs.";
		return content;
	}
	
	private String setContentToThanks(List<String> params) {
		String content ="";
		content += "Hei, rane68! Kiitos spottauksesta!\n\n";
		content += "Mit� muuta tied�t hissist�? Vastaa t�h�n viestiin muodossa:\n\n";
		content += "78392#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";
		content += "Muista kirjata hissin tunnistenumero 78392 viestin alkuun, esimerkin mukaisesti.\n\n";
		content +="Yst�v�llisin terveisin,\nElevator Spotting - your friend in life�s ups and downs.";
		return content;
	}
	
	private String setContentToError(List<String> params) {
		String content ="";
		content += "Hei!\n\n";
		content += "Saimme viestisi, mutta siin� oli jotain vikaa. Viestisi oli t�m�:\n\n";
		for(String param : params) {
			content += param + "#";
		}
		content += "\n\nSpottausviesti tulee l�hett�� muodossa:\n";
		content += "HISSI#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";
		content +="Yst�v�llisin terveisin,\nElevator Spotting - your friend in life�s ups and downs.";
		return content;
	}
	
	private boolean isValidID (String id) {
		//"testataan, l�ytyyk� parametrina annettu id j�rjestelm�n tietokannasta"
		return true;
	}
}
