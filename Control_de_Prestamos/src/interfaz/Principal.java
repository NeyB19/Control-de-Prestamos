package interfaz;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import control.Controladora;

public class Principal {

	private JFrame frmControlDePrestamos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.frmControlDePrestamos.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmControlDePrestamos = new JFrame();
		frmControlDePrestamos.setBounds(100, 100, 500, 350);
		frmControlDePrestamos.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		frmControlDePrestamos.addWindowListener(new WindowAdapter() {
			
			public void windowOpened(WindowEvent e) {
				Controladora.getInstance().cargarDatos();
			}

			public void windowClosing(WindowEvent e) {
				Controladora.getInstance().guardarDatos();
				System.exit(0);
			}
		});
		
		JPanel panelContenido = new JPanel();
		panelContenido.setBorder(new EmptyBorder(18, 18, 18, 18));
		panelContenido.setLayout(null);
		frmControlDePrestamos.getContentPane().add(panelContenido);

		JLabel lblTitulo = new JLabel("Sistema de Préstamos");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 30, 484, 30);
		panelContenido.add(lblTitulo);

		int anchoBoton = 230;
		int altoBoton = 40;
		int xCentrado = (484 - anchoBoton) / 2;

		JButton btnAdministrar = new JButton("Administrar");
		btnAdministrar.setFont(new Font("Arial", Font.PLAIN, 15));
		btnAdministrar.setBounds(xCentrado, 90, anchoBoton, altoBoton); 
		btnAdministrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Administrar();
			}
		});
		panelContenido.add(btnAdministrar);

		JButton btnPrestamos = new JButton("Gestionar Préstamos");
		btnPrestamos.setFont(new Font("Arial", Font.PLAIN, 15));
		btnPrestamos.setBounds(xCentrado, 150, anchoBoton, altoBoton); 
		btnPrestamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GestionarPrestamos();
			}
		});
		panelContenido.add(btnPrestamos);

		JButton btnReportes = new JButton("Ver Reportes");
		btnReportes.setFont(new Font("Arial", Font.PLAIN, 15));
		btnReportes.setBounds(xCentrado, 210, anchoBoton, altoBoton); 
		btnReportes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Reportes();
			}
		});
		panelContenido.add(btnReportes);
	}
}