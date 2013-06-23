package Schnittstellen;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.Subscription;

import Listener.ItemEventCoordinator;
import Listener.KellnerListener;
import MobileKasse.Kellner;
import MobileKasse.Koch;

public class XMPPSchnittstelle {
	
	 private XMPPConnection connection;
	 private PubSubManager mgr;
	 private int zaehler = 0;
	 private String username;
	 private String password;
	 
	 public XMPPSchnittstelle(String username, String password){
		this.username = username;
		this.password = password;
	 }
	
	public void login() throws XMPPException

    {

	    ConnectionConfiguration config = new ConnectionConfiguration("localhost",5222);
	    connection = new XMPPConnection(config);
	    connection.connect();
	    connection.login(username, password);
	    mgr = new PubSubManager(connection);
    }
	
	public void disconnect(){
		connection.disconnect();
	}
	
	public void NodeErzeugen(String name)throws Exception{
		  
		  LeafNode leaf = mgr.createNode(name);

	      ConfigureForm form = new ConfigureForm(FormType.submit);
	      form.setAccessModel(AccessModel.open);
	      form.setDeliverPayloads(false);
	      form.setSubscribe(true);
	      form.setNotifyRetract(true);
	      form.setPersistentItems(true);
	      form.setPublishModel(PublishModel.open);     

	      leaf.sendConfigurationForm(form);
	}
	public void NodeLöschen(String nameNode)throws Exception{
		mgr.deleteNode(nameNode);
	}
	
	public void Nodepublish(String nameNode,String nachricht)throws Exception{
		
		LeafNode node = mgr.getNode(nameNode);
		node.publish(new Item(nachricht));
		
	}
	public void Nodesubscribe(String nameNode,Koch koch)throws Exception{
		
		LeafNode node = mgr.getNode(nameNode);
		node.addItemEventListener(new ItemEventCoordinator(koch));
		node.subscribe(username+"@localhost");
		
	      
	}
	public void Nodesubscribe(String nameNode,Kellner kellner)throws Exception{
		
		LeafNode node = mgr.getNode(nameNode);
		mgr.getNode(nameNode).addItemEventListener(new KellnerListener(kellner));		
		node.subscribe(username+"@localhost");
		
	      
	}
	public List<Subscription> subscriptions()throws Exception{		
		return mgr.getSubscriptions();		
	}
	
	public LeafNode getNode(String nameNode)throws Exception{
		return mgr.getNode(nameNode);
	}
	public void listenerAnmelden(String nameNode,Koch koch) throws Exception{
		mgr.getNode(nameNode).addItemEventListener(new ItemEventCoordinator(koch));
		
	}
	public void listenerAnmelden(String nameNode,Kellner kellner) throws Exception{
			mgr.getNode(nameNode).addItemEventListener(new KellnerListener(kellner));		
	}
	public void NodeUnSubscribe(String nameNode)throws Exception{
		LeafNode node = mgr.getNode(nameNode);
		node.unsubscribe(username+"@localhost");
		
	}
	public List<Item> getMessages(String nameNode)throws Exception{
		LeafNode node = mgr.getNode(nameNode);
		return node.getItems();
		
	}
	public void deleteItem(String nameNode,String id)throws Exception{
		LeafNode node = mgr.getNode(nameNode);
		node.deleteItem(id);
	}
	public void deleteNode(String nameNode)throws Exception{
		mgr.deleteNode(nameNode);
	}
	
}
