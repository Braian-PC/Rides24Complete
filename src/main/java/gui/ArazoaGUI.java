package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import businessLogic.BLFacade;
import domain.Booking;
import domain.Complaint;

public class ArazoaGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButtonJun=null;
	private JButton jButtonErrekla=null;
	protected JLabel jLabelSelectOption;
	private static BLFacade appFacadeInterface;

	public static BLFacade getBusinessLogic() {
		return appFacadeInterface;
	}

	public static void setBussinessLogic(BLFacade afi) {
		appFacadeInterface = afi;
	}
	
	public  ArazoaGUI(String nork, String nori, Booking booking) {
		
		setBussinessLogic(LoginGUI.getBusinessLogic());
		
		Date gaur = new Date();

		this.setSize(495, 290);
		
		
		final String ETIKETA = "Etiquetas";
		
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle(ETIKETA).getString("ArazoaGUI.Arazoa"));
		jLabelSelectOption.setBounds(180, 11, 240, 36);
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		jButtonJun = new JButton();
		jButtonJun.setBounds(40, 70, 240, 50);
		jButtonJun.setText(ResourceBundle.getBundle(ETIKETA).getString("ArazoaGUI.Eznaizaurkeztu"));
		jButtonJun.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				appFacadeInterface.erreklamazioaBidali(new Complaint(nori, nork, gaur, booking, "Ez da agertu",false));
				jButtonCloseActionPerformed();
			}
		});
		
		jButtonErrekla = new JButton();
		jButtonErrekla.setBounds(40, 70, 240, 50);
		jButtonErrekla.setText(ResourceBundle.getBundle(ETIKETA).getString("BezeroGUI.Erreklamatu"));
		jButtonErrekla.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new ErreklamazioakGUI(nork, nori, booking);
				a.setVisible(true);
				jButtonCloseActionPerformed();
			}
		});
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(3, 1, 0, 0));
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonJun);
		jContentPane.add(jButtonErrekla);
		setContentPane(jContentPane);
		
	}
	

	private void jButtonCloseActionPerformed() {
		this.setVisible(false);
	}
}
