package Listener;
import java.util.regex.Pattern;

import javax.swing.JButton;

import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

import MobileKasse.Koch;

import com.kellnersystem.main.Kellnersystem.Tisch.Bestellungen.Bestellung;
import com.kellnersystem.main.Kellnersystem.Tisch.Bestellungen.Bestellung.Artikellist.Artikel;
public class ItemEventCoordinator  implements ItemEventListener<Item> 
      {		
			Koch koch;
			public ItemEventCoordinator(Koch koch){
				this.koch = koch;
			}
          @Override
          public void handlePublishedItems(ItemPublishEvent<Item> items)
          {	
        	  String bestellung_txt = "";
        	  int tischid;
        	  int bestellid;
        	  for(int i=0;i<items.getItems().size();i++){
            	  //System.out.println(item.getId());
        		  String[] segs = items.getItems().get(i).getId().split( Pattern.quote( "|" ) );
        		  tischid = Integer.parseInt(segs[0]);
        		  bestellid = Integer.parseInt(segs[1]);
        		  Bestellung bs = koch.rskoch.getBestellung(tischid, bestellid);
        		  for(Artikel artikel : bs.getArtikellist().getArtikel()){
        			  bestellung_txt += artikel.getMenge()+"x " +koch.artikel.getArtikel(artikel.getArtikelId()).getArtikelName()+  " | ";
        		  }
               	  koch.buttons.add(new JButton(bestellid+"| "+bestellung_txt));
            	  koch.buttons.get(koch.buttons.size()-1).addActionListener(new BestellungFertigListener(items.getItems().get(i).getId(),koch));            	  
              } 
        	  koch.buttonsZeichnen();
        }
	}