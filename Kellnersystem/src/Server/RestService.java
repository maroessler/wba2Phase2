package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import Schnittstellen.XMPPSchnittstelle;

import com.kellnersystem.artikelliste.Artikelliste;
import com.kellnersystem.main.Kellnersystem;

@Path( "/" )
public class RestService {
	
	File mainXmlFile = new File("Main.xml");
	JAXBContext mainJc;
	Marshaller mainM; 
	Unmarshaller mainUm;
	Kellnersystem mainKs;
	
	File artikelXmlFile = new File("Artikel.xml");
	JAXBContext artikelJc;
	Marshaller artikelMarshaller; 
	Unmarshaller artikelUnmarshaller;
	Artikelliste artikelAl;
	
	//xmpp
	
	
	
	
	public RestService() throws Exception{
		mainJc = JAXBContext.newInstance(Kellnersystem.class);
		mainM = mainJc.createMarshaller();
		mainM.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		mainUm = mainJc.createUnmarshaller();
		
		
		if(!mainXmlFile.exists()) {
			   mainM.marshal(new Artikelliste(), mainXmlFile);
		}
		mainKs = (Kellnersystem) mainUm.unmarshal(mainXmlFile);
		
		artikelJc = JAXBContext.newInstance(Artikelliste.class);
		artikelMarshaller = artikelJc.createMarshaller();
		artikelMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		artikelUnmarshaller = artikelJc.createUnmarshaller();
		
		if(!artikelXmlFile.exists()) {
			artikelMarshaller.marshal(new Artikelliste(), artikelXmlFile);
		}
		artikelAl = (Artikelliste) artikelUnmarshaller.unmarshal(artikelXmlFile);
		
		
	}
	
	@POST
	@Path("/artikel")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String postArtikel(Artikelliste al) throws Exception {
		artikelAl.getArtikel().add(al.getArtikel().get(0));
		artikelMarshaller.marshal(artikelAl, artikelXmlFile);
		return artikelAl.toString();
	}
	
	@PUT
	@Path("/artikel/{artikelId}")
	@Produces(MediaType.TEXT_XML)
	@Consumes(MediaType.TEXT_XML)
	public String putArtikel(Artikelliste al, @PathParam("artikelId") int artikelId) throws JAXBException {
		Artikelliste.Artikel ar = al.getArtikel().get(0);
		artikelAl.getArtikel(artikelId).setArtikelName(ar.getArtikelName());
		artikelAl.getArtikel(artikelId).setPreis(ar.getPreis());
		artikelAl.getArtikel(artikelId).setArtikelId(ar.getArtikelId());
		artikelMarshaller.marshal(artikelAl, artikelXmlFile);
		return al.toString() + " wurde geaendert.";
	}
	
	@GET
	@Path("/artikel")
	@Produces(MediaType.TEXT_XML)
	public Artikelliste getArtikel() {
		return artikelAl;
	}
	
	@GET
	@Path("/tisch")
	@Produces(MediaType.TEXT_XML)
	public Kellnersystem getTisch() throws JAXBException, IOException {
		return mainKs;
	}
	
	@POST
	@Path("/tisch/{tischNummer}/bestellung")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String postBestellung(Kellnersystem ks, @PathParam("tischNummer") int tischNummer) throws Exception  {
		Kellnersystem.Tisch.Bestellungen.Bestellung be = ks.getTisch().get(0).getBestellungen().getBestellung().get(0);
		be.setNummer();
		try {
			mainKs.getTisch(tischNummer).getBestellungen().getBestellung().add(be);
		} catch (NullPointerException e) {
			Kellnersystem.Tisch ti = new Kellnersystem.Tisch();
			ti.setNummer(tischNummer);
			ti.setBestellungen(new Kellnersystem.Tisch.Bestellungen());
			ti.getBestellungen().getBestellung().add(be);
			mainKs.getTisch().add(ti);
		}
		mainM.marshal(mainKs, mainXmlFile);
		
		//XMPP
		XMPPSchnittstelle xmpserver = new XMPPSchnittstelle("Server","test1234");
		xmpserver.login();
		boolean vorhanden = false;
		try{
			if(xmpserver.getNode("Küche")!=null){
				vorhanden = true;
				}
			}catch(Exception e){
				vorhanden = false;
			}
		if(!vorhanden)xmpserver.NodeErzeugen("Küche");
		
		xmpserver.Nodepublish("Küche",""+tischNummer+"|"+be.getNummer());
//		System.out.println(""+tischNummer+"|"+be.getNummer());
			
		
		
		return be.toString() + " wurde eingetragen!";
	}
	
	@GET
	@Path("/tisch/{tischNummer}/bestellung")
	@Produces(MediaType.TEXT_XML)
	public Kellnersystem zahlvorgang(@PathParam("tischNummer") int tischNummer) throws JAXBException, IOException {
		Kellnersystem ks = new Kellnersystem();
		ks.getTisch().add(mainKs.getTisch(tischNummer));
		return ks;
	}
	
	@DELETE
	@Path("/tisch/{tischNummer}/bestellung/")
	@Produces(MediaType.TEXT_XML)
	@Consumes(MediaType.TEXT_XML)
	public void AlleArtikelBezahlt(@PathParam("tischNummer") int tischNummer) throws JAXBException, FileNotFoundException {
			int indexTisch = mainKs.getIndex(tischNummer);
			mainKs.getTisch().remove(indexTisch);
			mainM.marshal(mainKs, mainXmlFile);
	}
	
	@GET
	@Path("/tisch/{tischNummer}/bestellung/{bestellungNummer}")
	@Produces(MediaType.TEXT_XML)
	public Kellnersystem getOne(@PathParam("tischNummer") int tischNummer, @PathParam("bestellungNummer") int bestellungNummer) throws JAXBException, FileNotFoundException {
		Kellnersystem.Tisch ti = mainKs.getTisch(tischNummer);
		Kellnersystem.Tisch.Bestellungen.Bestellung be = ti.getBestellungen().getBestellung(bestellungNummer);
		Kellnersystem ks = new Kellnersystem();
		ks.addBestellungToNewKellnersystem(be);
		return ks;
	}

	@DELETE
	@Path("/tisch/{tischNummer}/bestellung/{bestellNummer}")
	@Produces(MediaType.TEXT_XML)
	@Consumes(MediaType.TEXT_XML)
	public void Bestellungaendern(@PathParam("tischNummer") int tischNummer, @PathParam("bestellNummer") int bestellNummer) throws JAXBException, FileNotFoundException {
			int indexTisch = mainKs.getIndex(tischNummer);
			int indexBestellung = mainKs.getTisch().get(indexTisch).getBestellungen().getIndex(bestellNummer);
			mainKs.getTisch().get(indexTisch).getBestellungen().getBestellung().remove(indexBestellung);
			if(mainKs.getTisch().get(indexTisch).getBestellungen().getBestellung().size() == 0) {
				mainKs.getTisch().remove(indexTisch);
			}
			mainM.marshal(mainKs, mainXmlFile);
	}
}