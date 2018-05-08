package Interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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

public class PantallaMonedero extends JFrame {
	Monedero monedero;

	private JPanel contentPane;
	private JLabel lblDireccion;
	private JTable table;

	/**
	 * Create the frame.
	 * 
	 * @param mon
	 * @throws UnsupportedLookAndFeelException
	 */
	public PantallaMonedero(Monedero mon) throws UnsupportedLookAndFeelException {
		inicializar();
		this.monedero = mon;
		lblDireccion.setText(StringUtil.getStringDeclave(mon.clavePublica));

	}

	private void inicializar() throws UnsupportedLookAndFeelException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbllDireccion = new JLabel("Direccion:");
		lbllDireccion.setBounds(50, 21, 93, 21);
		contentPane.add(lbllDireccion);
		UIManager.setLookAndFeel(new com.jtattoo.plaf.mcwin.McWinLookAndFeel());
		lblDireccion = new JLabel("New label");
		lblDireccion.setBounds(177, 22, 202, 19);
		contentPane.add(lblDireccion);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 69, 323, 152);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"Motivo", "Emisor", "Receptor", "Valor", "Fecha"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(85);
		table.getColumnModel().getColumn(1).setPreferredWidth(85);
		table.getColumnModel().getColumn(2).setPreferredWidth(85);
		table.getColumnModel().getColumn(3).setPreferredWidth(85);
		table.getColumnModel().getColumn(4).setPreferredWidth(85);
	}
}