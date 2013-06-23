package MobileKasse;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Listener.Artikellistener;
import Schnittstellen.RestSchnittstelle;

import com.kellnersystem.main.Kellnersystem.Tisch.Bestellungen.Bestellung;
import com.kellnersystem.main.Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist;

public class OF_Bestellung {
	
	List<JButton> buttons = new ArrayList<JButton>();
	public Bestellung bestellung = new Bestellung();
	int tischNummer;
	Kellner kellner;
	public OF_Bestellung(int tischNummer,Kellner kellner){
		this.tischNummer = tischNummer;
		this.kellner = kellner;
		bestellung.setArtikellist(new Artikellist());
	}
	
	public JPanel artikelliste(){
		
		JPanel panel = new JPanel();
		panel.setSize(800,600);
		panel.setLayout(null);
		//Get alle artikel
		int artikelanzahl = kellner.artikelliste.size();

		int x = 50;
		int y = 50;
		JLabel top = new JLabel("Artikelliste");
		top.setBounds(0, 0, 800, 70);
		top.setFont(new Font("",Font.PLAIN,20));
		top.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(top);
		JButton ok = new JButton("Bestellen");
		ok.setBounds(645, 520, 150, 50);
		ok.setFont(new Font("",Font.PLAIN,20));
		panel.add(ok);
			for(int i=0;i<artikelanzahl;i++){
				buttons.add(new JButton(kellner.artikelliste.get(i).getArtikelName()));
				if(i%4==0){
					y+=60;
					x = 0;
				}
				buttons.get(i).setBounds(x+=140, y, 120, 40);
				buttons.get(i).addActionListener(new Artikellistener(kellner.artikelliste.get(i).getArtikelId(),bestellung));
				panel.add(buttons.get(i));
			}
			
			ok.addActionListener(new ActionListener() {
				 
	            public void actionPerformed(ActionEvent e)
	            {
	            	RestSchnittstelle rs = new RestSchnittstelle();
	            	rs.postBestellung(bestellung, tischNummer);
	            	kellner.zeichneOberflaeche();
	            }
	        });
			
		return panel;
	}

}
