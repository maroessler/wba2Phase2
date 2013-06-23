package Listener;

import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

import MobileKasse.Kellner;

public class KellnerListener implements ItemEventListener<Item>{
	
	Kellner kellner;
	public KellnerListener(Kellner kellner){
		this.kellner = kellner;
	}

	@Override
	public void handlePublishedItems(ItemPublishEvent<Item> items) {
		
		for(Item item : items.getItems()){
			String nachricht = item.getId();
			String[] segs = nachricht.split( Pattern.quote( "|" ) );
			nachricht = "Die Bestellung: "+segs[1]+" von Tisch: "+segs[0]+" ist fertig!";
			JOptionPane.showMessageDialog(null, nachricht);
		}
	}

}
