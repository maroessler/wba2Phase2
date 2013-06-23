package MobileKasse;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MobilesKellnersystem {
	
	private JPanel content;
	private JFrame jf;
	
	public MobilesKellnersystem() throws Exception{
		
		fenster();
		this.jf = jf;
			 	
	}
	public void fenster(){
		
		jf = new JFrame("Kellnersystem");
		jf.setLayout(null);
		jf.setSize(800, 600);
		jf.setVisible(true);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		content = new JPanel();
		content.setLayout(null);
		content.setBounds(0, 0, 800, 600);
		content.setVisible(true);
		
		JButton kellner = new JButton("Kellner");
		JButton koch = new JButton("Koch");
		
		kellner.setBounds(300,180, 150, 100);
		kellner.setBackground(Color.CYAN);
		koch.setBounds(300,320,150,100);
		koch.setBackground(Color.MAGENTA);
		
		content.add(koch);
		content.add(kellner);
		jf.getContentPane().add(content);
		
		kellner.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
                try {
                	content.setVisible(true);
                	content.removeAll();
                	jf.repaint();
					new Kellner(jf,content,"kellner","test1234");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        }); 
		
		koch.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
               try {
            	   content.setVisible(false);
            	   jf.repaint();
				new Koch(jf,content,"koch","test1234");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            }
        }); 

	}

}
