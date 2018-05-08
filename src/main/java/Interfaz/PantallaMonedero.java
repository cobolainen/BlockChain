package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import blockChain.Monedero;
import blockChain.StringUtil;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import java.awt.Font;

public class PantallaMonedero extends JFrame {
	Monedero monedero;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * 
	 * @param mon
	 * @throws UnsupportedLookAndFeelException
	 */
	public PantallaMonedero(Monedero mon) throws UnsupportedLookAndFeelException {
		inicializar();
		this.monedero = mon;
		monedero.getBalance();
	}

	private void inicializar() throws UnsupportedLookAndFeelException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 792, 608);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 756, 213);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblBalance = new JLabel("BALANCE");
		lblBalance.setBounds(28, 58, 79, 14);
		panel.add(lblBalance);
		
		JLabel lblNewLabel = new JLabel("CBC");
		lblNewLabel.setBounds(28, 98, 138, 31);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("EQUIVALEN A:");
		lblNewLabel_1.setBounds(226, 58, 98, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblEur = new JLabel("EUR");
		lblEur.setBounds(226, 98, 138, 31);
		panel.add(lblEur);
		
		JLabel lblAcciones = new JLabel("ACCIONES");
		lblAcciones.setBounds(637, 58, 68, 14);
		panel.add(lblAcciones);
		
		JButton btnNewButton = new JButton("ENVIAR");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBounds(494, 102, 89, 23);
		btnNewButton.setBackground(new Color (9, 241, 202));
		panel.add(btnNewButton);
		
		JButton btnRecibir = new JButton("RECARGAR");
		btnRecibir.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRecibir.setForeground(Color.WHITE);
		btnRecibir.setBounds(607, 102, 98, 23);
		btnRecibir.setBackground(new Color (9, 241, 202));
		btnRecibir.setFocusPainted(false);
		panel.add(btnRecibir);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(10, 224, 756, 334);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("ULTIMAS TRANSACCIONES");
		lblNewLabel_2.setBounds(29, 25, 160, 24);
		panel_1.add(lblNewLabel_2);
		
		JList list = new JList();
		list.setBounds(29, 75, 671, 234);
		panel_1.add(list);
		UIManager.setLookAndFeel(new com.jtattoo.plaf.mcwin.McWinLookAndFeel());
	}
}
