package Schnittstellen;

import java.io.File;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import com.kellnersystem.artikelliste.Artikelliste;
import com.kellnersystem.main.Kellnersystem;
import com.sun.jersey.api.client.*;

public class RestSchnittstelle
{
	static String restURI = "http://localhost:8080";
	static JAXBContext jc;
	static Marshaller marshaller; 
	static Unmarshaller unmarshaller; 
	
	public void deleteBestellung(int tischNummer, int bestellNummer) {
		String uri = restURI + "/tisch/" + tischNummer + "/bestellung/" + bestellNummer;
		WebResource wrs = Client.create().resource(uri);
		wrs.delete();
	}
	public void deleteBestellung(int tischNummer) {
		String uri = restURI + "/tisch/" + tischNummer + "/bestellung";
		WebResource wrs = Client.create().resource(uri);
		wrs.delete();
	}
	public void postBestellung(Kellnersystem.Tisch.Bestellungen.Bestellung bs, int tischId) {
		String uri = restURI + "/tisch/" + tischId + "/bestellung/";
		WebResource wrs = Client.create().resource( uri );
		Kellnersystem ks = new Kellnersystem();
		ks.getTisch().add(new Kellnersystem.Tisch());
		ks.getTisch().get(0).setBestellungen(new Kellnersystem.Tisch.Bestellungen());
		ks.getTisch().get(0).getBestellungen().getBestellung().add(bs);
		String result = wrs.type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).post(String.class, ks );
		//System.out.println(result);
	}
	public Kellnersystem getTisch() {
		String uri = restURI + "/tisch";
		WebResource wrs = Client.create().resource(uri);
		return wrs.accept(MediaType.TEXT_XML).get(Kellnersystem.class);
	}
   
	public Kellnersystem.Tisch.Bestellungen getBestellungen(int tischNummer) {
		String uri = restURI + "/tisch/" + tischNummer + "/bestellung";
		WebResource wrs = Client.create().resource( uri );
		File ergebnisGetZahlvorgang = wrs.accept(MediaType.TEXT_XML).get(File.class);
		//System.out.println("" + ergebnisGetZahlvorgang.toString());
		Kellnersystem ts = wrs.accept(MediaType.TEXT_XML).get(Kellnersystem.class);
		return ts.getTisch().get(0).getBestellungen();
	}
	public Kellnersystem.Tisch.Bestellungen.Bestellung getBestellung(int tischNummer, int bestellNummer) {
		String uri = restURI + "/tisch/" + tischNummer + "/bestellung/" + bestellNummer;
		WebResource wrs = Client.create().resource(uri);
		Kellnersystem ks = wrs.accept(MediaType.TEXT_XML).get(Kellnersystem.class);
		return ks.getTisch().get(0).getBestellungen().getBestellung().get(0);
	}
	public void postArtikel(Artikelliste.Artikel ar) {
		String uri = restURI + "/artikel/";
		WebResource wrs = Client.create().resource(uri);
		Artikelliste al = new Artikelliste();
		al.getArtikel().add(ar);
		String result =wrs.type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).post(String.class, al);
		//System.out.println(result);
	}
	public void putArtikel(Artikelliste.Artikel ar, int artikelId) {
		String uri = restURI + "/artikel/" + artikelId;
		WebResource wrs = Client.create().resource(uri);
		Artikelliste al = new Artikelliste();
		al.getArtikel().add(ar);
		wrs.type(MediaType.TEXT_XML).accept(MediaType.TEXT_XML).put(String.class, al);
	}
	public Artikelliste getArtikel() {
		String uri = restURI + "/artikel/";
		WebResource wrs = Client.create().resource(uri);
		return wrs.accept(MediaType.TEXT_XML).get(Artikelliste.class);
	}
}
