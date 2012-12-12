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

		if(params.size() != 6 && params.size() != 1) {
			System.out.println("ERROR IN MESSAGE: param count: " + params.size());
			setSubject("Spottauksesi oli virheellinen");
			content = setContentToError(params);
		}
		
		//Jos k�ytt�j� l�hett�� pelk�n osoitteen, niin siin� ei oo parsittavaa
		else if(params.get(0).equals("<kuva>")) {
			System.out.println("PICTURE IN MESSAGE");
			setSubject("Kiitos spottauksesta!");
			content = setContentToThanks(params);
		}
		else if(params.size()==1) {
			System.out.println("JUST ONE PARAMETER IN MESSAGE");
			setSubject("Lis�tietoja hissist� osoitteessa " + params.get(0));
			content = setContentToInfo(params);
		}
		
		//Jos kyseess� tavallinen spottaus
		//jos valmistajan nimi on virheellinen (sis. � tai �, niin v�litet��n tieto vastausmetodille
		else if(params.get(0).equals("HISSI")) {
			System.out.println("SPOTTING IN MESSAGE");
			boolean errors = isManufacturerOK(params.get(2)) || isYearOK(params.get(3));
			System.out.println("ERRORS: " + errors);
			content = setContentToSpotting(params, errors);
		}
		
		//Jos kyseess� on tietojen t�ydennys
		else if(isValidID(params.get(0))) {
			System.out.println("ADDITION IN MESSAGE, STARTING WITH ID");
			content = setContentToComplement(params);
		}
		else if(!isValidID(params.get(0))) {
			System.out.println("ERROR IN MESSAGE");
			setSubject("Spottauksesi oli virheellinen");
			content = setContentToError(params);
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

		content += "Oikeiden tietojen osalta j�t� kohta tyhj�ksi!\n";
		content += "Muista kirjata hissin tunnistenumero 758933 viestin alkuun, esimerkin mukaisesti.";

		content +="Terveisin,\nElevator Spotting - your friend in life�s ups and downs.";
		return content;
	}
	
	private String setContentToSpotting(List<String> params, boolean errors) {
		String content ="";
		content += "Hei, rane68!\n\nSpottaamasi hissi on nyt lis�tty ElevatorSpottingiin tiedoilla:\n";
		content += "Osoite: " + params.get(1) + "\n";
		if(errors && (isManufacturerOK(params.get(2)))) { content += "*";
		content += "Valmistaja: " + params.get(2) + "\n";
		if(errors && isYearOK(params.get(3))) content += "*";
		content += "Valmistusvuosi: " + params.get(3) + "\n";
		content += "Kerrosten lukum��r�: " + params.get(4) + "\n";
		content += "Omat kommenttisi: \"" + params.get(5) + "\"\n\n";
		if(errors = true) {
			content += "*) T�hdell� merkityt tiedot ovat mahdollisesti virheellisi�. Tarkista hissin ";
			if(errors && isManufacturerOK(params.get(2))) content += "valmistaja";
			if(errors && isYearOK(params.get(3))) content += " ja valmistusvuosi";
			content += "!\n\n";
			}
		}
		content += "Jos tiedot ovat virheellisi�, voit korjata ne vastaamalla t�h�n viestiin n�in:\n";
		content += "758933#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";

		content += "Oikeiden tietojen osalta j�t� kohta tyhj�ksi!\n";
		content += "Muista kirjata hissin tunnistenumero 758933 viestin alkuun, esimerkin mukaisesti.\n\n";

		content += "Kiitos spottauksesta!\nElevator Spotting - your friend in life�s ups and downs.";

		return content;
	}
	
	//testaa, onko hissin valmistusvuosiluku valid, eli onko se numero ja onko se isompi kuin 1800.
	private boolean isYearOK(String y) {
		try {
			int year = Integer.parseInt(y);
			return year >= 1800;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	//testaa, onko hissin valmistajan nimi oikea. Jos siin� on �-kirjain, se ei ole.
	private boolean isManufacturerOK(String v) {
		return v.contains("�") || v.contains("�");
	}

	private String setContentToComplement(List<String> params) {
		String content ="";
		content += "Hei!\n\nKiitos t�ydennyksest� hissin " + params.get(0) + " tietoihin!:\n";
		content += "Hissi on nyt tallennettu seuraavin tiedoin:\n";
		content += "Osoite: ";
		content += (params.get(1).isEmpty() || params.get(1).equals(" ") ? "Lemink�isenkatu 7B, 02400 Espoo" : params.get(1)) + "\n";
		content += "Valmistaja: ";
		content += (params.get(2).isEmpty() || params.get(2).equals(" ") ? "KONE" : params.get(2)) + "\n";
		content += "Valmistusvuosi: ";
		content += (params.get(3).isEmpty() || params.get(3).equals(" ") ? "1989" : params.get(3)) + "\n";
		content += "Kerrosten lukum��r�: ";
		content += (params.get(4).isEmpty() || params.get(4).equals(" ") ? "12" : params.get(4)) + "\n";
		content += "Omat kommenttisi: \"";
		content += (params.get(5).isEmpty() || params.get(5).equals(" ") ? 
				"Aika korkea ja ruma hissi, en muista montako kerrosta siin� oli." : params.get(5)) + "\"\n\n";

		content += "Jos tiedot ovat virheellisi�, voit korjata ne vastaamalla t�h�n viestiin n�in:\n";
		content += params.get(0) +"#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";

		content += "Oikeiden tietojen osalta j�t� kohta tyhj�ksi!\n";
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
		for(int i = 0; i < params.size(); i++) {
			if(i == params.size()-1) {
				content += params.get(i);
			}
			else content += params.get(i) + "#";
		}
		content += "\n\nSpottausviesti tulee l�hett�� muodossa:\n";
		content += "HISSI#osoite#valmistaja#valmistusvuosi#kerrosten lukum��r�#omat kommenttisi\n\n";
		content += "Niiden tietojen osalta, joita en tied�, voit j�tt�� kyseisen kohdan tyhj�ksi.\n\n";
		content +="Yst�v�llisin terveisin,\nElevator Spotting - your friend in life�s ups and downs.";
		return content;
	}
	

	//"testataan, l�ytyyk� parametrina annettu id j�rjestelm�n tietokannasta", eli k�yt�nn�ss� onko se numero
	private boolean isValidID (String id) {
		try {
			int i = Integer.parseInt(id);
			return true;
 		} catch(NumberFormatException e) {
 			return false;
	}
		
	}
}
