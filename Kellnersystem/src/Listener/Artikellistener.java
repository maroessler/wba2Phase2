package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Schnittstellen.RestSchnittstelle;

import com.kellnersystem.main.Kellnersystem.Tisch.Bestellungen.Bestellung;
import com.kellnersystem.main.Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel;

public class Artikellistener implements ActionListener{
	
	int artikelid;
	Bestellung bestellung;
	RestSchnittstelle rs = new RestSchnittstelle();
	public Artikellistener(int artikelid,Bestellung bestellung){
		this.artikelid = artikelid;
		this.bestellung = bestellung;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
		if(bestellung.getArtikellist().getArtikel().size()==0){
				Artikel artikel = new Artikel();
				artikel.setArtikelId(artikelid);
				artikel.setMenge(1);
				bestellung.getArtikellist().getArtikel().add(artikel);
				System.out.println("Liste ist leer");
		}else{
		boolean vorhanden = false;
		for(int i=0;i<bestellung.getArtikellist().getArtikel().size();i++){			
			if(bestellung.getArtikellist().getArtikel().get(i).getArtikelId()==artikelid){
				bestellung.getArtikellist().getArtikel().get(i).setMenge(bestellung.getArtikellist().getArtikel().get(i).getMenge()+1);				
				vorhanden = true;
			}
		}
		if(!vorhanden){
			Artikel artikel = new Artikel();
			artikel.setArtikelId(artikelid);
			artikel.setMenge(1);
			bestellung.getArtikellist().getArtikel().add(artikel);
		}
		}
	}
}
