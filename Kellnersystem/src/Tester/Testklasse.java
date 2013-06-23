package Tester;
import Server.*;
import Schnittstellen.*;
import com.kellnersystem.artikelliste.Artikelliste;
import com.kellnersystem.main.Kellnersystem;

public class Testklasse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		   Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel a1 = new Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel();
		   Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel a2 = new Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel();
		   Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel a3 = new Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel();
		   Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel a4 = new Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel();

		   a1.setArtikelId(777);
		   a1.setMenge(1);
		   a2.setArtikelId(8);
		   a2.setMenge(2);
		   a3.setArtikelId(9);
		   a3.setMenge(4);
		   a4.setArtikelId(10);
		   a4.setMenge(3);
		   
		   Kellnersystem.Tisch.Bestellungen.Bestellung bs = new Kellnersystem.Tisch.Bestellungen.Bestellung();
		   bs.setArtikellist(new Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist());
		   
		   bs.getArtikellist().getArtikel().add(a1);
		   bs.getArtikellist().getArtikel().add(a2);
		   bs.getArtikellist().getArtikel().add(a3);
		   bs.getArtikellist().getArtikel().add(a4);
		   
		   RestSchnittstelle rs = new RestSchnittstelle();
		   
		   rs.postBestellung(bs, 3);
		   rs.postBestellung(bs, 3);
		   rs.deleteBestellung(3, 2);
		   rs.postBestellung(bs, 2);
		   rs.postBestellung(bs, 3);
		   
		   Kellnersystem.Tisch.Bestellungen be = rs.getBestellungen(3);
		   
			
			
			for(int i = 0; i< be.getBestellung().size(); i++) {
				System.out.println("Bestellung Nr " + be.getBestellung().get(i).getNummer());
				for(int j = 0; j < be.getBestellung().get(i).getArtikellist().getArtikel().size(); j++) {
					System.out.println("Artikel-ID: " + be.getBestellung().get(i).getArtikellist().getArtikel().get(j).getArtikelId() + " Menge: " + be.getBestellung().get(i).getArtikellist().getArtikel().get(j).getMenge());
				}
				System.out.println("---------------------------");
			}
		   
		   
		   
		   Artikelliste.Artikel ar = new Artikelliste.Artikel();
		   ar.setArtikelId(1);
		   ar.setArtikelName("Butter");
		   ar.setPreis(1.20);
		   
		   //rs.postArtikel(ar);
		   
		   ar.setPreis(2.50);
		   ar.setArtikelId(1);
		   ar.setArtikelName("Cola");
		   rs.putArtikel(ar, 1);

	}

}
