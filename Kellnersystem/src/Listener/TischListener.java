package Listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import MobileKasse.Kellner;
import MobileKasse.OF_Bestellung;

import com.kellnersystem.artikelliste.Artikelliste;
import com.kellnersystem.main.Kellnersystem;
import com.kellnersystem.main.Kellnersystem.Tisch.Bestellungen.Bestellung;


public class TischListener implements ActionListener{
	
	String TischNummer;
	Kellner kellner;
	List<Integer> checkedBS = new ArrayList<Integer>();
	List<JCheckBox> boxen = new ArrayList<JCheckBox>();
	int bestellNummer = 0;
	JButton neueBestellung = new JButton("neue Bestellung");
	JLabel top;
	Bestellung bestellung;
	public TischListener(String button,Kellner kellner){
		this.TischNummer = button;
		this.kellner = kellner;
		top  = new JLabel("Bestellung Tisch Nr.: "+TischNummer);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		kellner.panel.removeAll();
		int tischNummer = Integer.parseInt(TischNummer);
		boolean tisch;
		try{
			Kellnersystem.Tisch.Bestellungen bestellungen = kellner.rskellner.getBestellungen(tischNummer);
			int x = 150;
			int y = 0;			
			int i = 0;
			for(Bestellung bs : bestellungen.getBestellung()){
				bestellNummer = bs.getNummer();
				boxen.add(new JCheckBox(""+bestellNummer+"      "+getSumme(kellner.artikel,bs)+"Euro"));
				boxen.get(i).setBounds(x, y+=40, 200, 50);
				kellner.panel.add(boxen.get(i));
				i++;			
			}
			kellner.panel.repaint();
			
			tisch = true;
		}catch(Exception e){
			tisch = false;
		}
		
		
		kellner.panel.setLayout(null);
		JButton zurueck = new JButton("Zurück");
		zurueck.setBounds(0, 0, 150, 50);		
		neueBestellung.setBounds(645, 0, 150, 50); //520
		top.setBounds(325, 0, 150, 50);
		if(tisch){
			
			JButton bezahlen = new JButton("Alle Bezahlen");
			bezahlen.setBounds(645, 520, 150, 50);
			bezahlen.addActionListener(new ActionListener() {
				 
	            public void actionPerformed(ActionEvent e)
	            {
	            	boolean selectedbox = false;
	            	for(JCheckBox bx : boxen){
	            		if(bx.isSelected()){
	            			selectedbox = true;
	            			String[] segs = bx.getLabel().split( Pattern.quote( " " ) );
	            			int bestell_id = Integer.parseInt(segs[0]);
	            			kellner.rskellner.deleteBestellung(Integer.parseInt(TischNummer),bestell_id);
	            		}            		
	            	}
	            	if(!selectedbox){
	            		kellner.rskellner.deleteBestellung(Integer.parseInt(TischNummer));
	            	}
	            	kellner.zeichneOberflaeche();
	            	kellner.jf.repaint();
	            	
	            }
	        });
			
			kellner.panel.add(bezahlen);
		}
		zurueck.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	kellner.zeichneOberflaeche();            	
            }
        });
		neueBestellung.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	kellner.panel.removeAll();
            	OF_Bestellung al = new OF_Bestellung(Integer.parseInt(TischNummer),kellner);
            	kellner.panel.add(al.artikelliste());
            	kellner.panel.repaint();
            }
        });
		
		
		kellner.panel.add(top);
		kellner.panel.add(zurueck);
		kellner.panel.add(neueBestellung);
		
		kellner.panel.repaint();
		kellner.jf.repaint();
	}
	
	 private double getSumme(Artikelliste al, Kellnersystem.Tisch.Bestellungen.Bestellung bs) {
		  double summe = 0;
		  for(Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel artikel : bs.getArtikellist().getArtikel()) {
			  summe += al.getArtikel(artikel.getArtikelId()).getPreis() * artikel.getMenge();
		  }
		  return summe;   
		 }

}
