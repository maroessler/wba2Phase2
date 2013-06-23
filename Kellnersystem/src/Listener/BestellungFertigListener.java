package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;

import MobileKasse.Koch;

public class BestellungFertigListener implements ActionListener {
	
	String nachricht;
	Koch koch;
	public BestellungFertigListener(String nachricht,Koch koch){
		this.nachricht = nachricht;
		this.koch = koch;
	}

	@Override
	public void actionPerformed(ActionEvent arg0){
		String[] segs = nachricht.split( Pattern.quote( "|" ) );
		String bestellid = segs[1];
		
		try{
			koch.koch.Nodepublish("Kellner", nachricht);
//			
			for(int i=0;i<koch.buttons.size();i++){
			String[] tmp =koch.buttons.get(i).getText().split( Pattern.quote( "|" ) );
			System.out.println(bestellid);
			System.out.println(tmp[0]);
				if(tmp[0].equals(bestellid)){
					koch.panel.remove(koch.buttons.get(i));
					koch.buttons.remove(i);					
				}
			}
			koch.buttonsZeichnen();
		}catch(Exception a){
			System.out.println("Nachricht konnte nicht gepublished werden");
			
		}

		
	}

}
