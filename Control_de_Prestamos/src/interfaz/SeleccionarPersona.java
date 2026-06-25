package interfaz;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import control.Controladora;
import logica.Persona;

public class SeleccionarPersona extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tablaPersonasSelector;
	private JButton btnConfirmar;
	private JButton btnCancelar;
	
	private String personaSeleccionada = null;

	/**
	 * Constructor
	 */
	public SeleccionarPersona(JFrame padre) {
		super(padre, "Seleccionar Solicitante", true); 
		setTitle("");
		initialize();
		llenarTablaPersonas(); 
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the dialog.
	 */
	private void initialize() {
		this.setBounds(150, 150, 520, 380);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel panelContenido = new JPanel();
		panelContenido.setBorder(new EmptyBorder(15, 15, 15, 15));
		panelContenido.setLayout(null); 
		this.getContentPane().add(panelContenido);

		JLabel lblInstruccion = new JLabel("Seleccione el usuario que solicita el préstamo:");
		lblInstruccion.setFont(new Font("Arial", Font.BOLD, 14));
		lblInstruccion.setBounds(15, 15, 470, 22);
		panelContenido.add(lblInstruccion);

		JScrollPane scrollTabla = new JScrollPane();
		scrollTabla.setBounds(15, 50, 470, 220);
		panelContenido.add(scrollTabla);

		tablaPersonasSelector = new JTable();
		tablaPersonasSelector.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Nombre Completo", "Teléfono", "Correo" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false; 
			}
		});
		scrollTabla.setViewportView(tablaPersonasSelector);

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setFont(new Font("Arial", Font.PLAIN, 13));
		btnConfirmar.setBounds(230, 290, 120, 30);
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablaPersonasSelector.getSelectedRow();
				if (fila != -1) {
					personaSeleccionada = tablaPersonasSelector.getValueAt(fila, 0).toString();
					dispose();
				} else {
					JOptionPane.showMessageDialog(SeleccionarPersona.this, 
						"Por favor, seleccione un usuario de la lista.", 
						"Atención", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panelContenido.add(btnConfirmar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));
		btnCancelar.setBounds(365, 290, 120, 30);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personaSeleccionada = null; 
				dispose();
			}
		});
		panelContenido.add(btnCancelar);
	}

	private void llenarTablaPersonas() {
		DefaultTableModel modelo = (DefaultTableModel) tablaPersonasSelector.getModel();
		modelo.setRowCount(0); 
		
		try {
			for (Persona persona : Controladora.getInstance().getPersonasRegistradas().values()) {
				modelo.addRow(new Object[] {
					persona.getNombreCompleto(),
					persona.getTelefono(),
					persona.getCorreoElectronico()
				});
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar selector de personas: " + ex.getMessage());
		}
	}
	public String getPersonaSeleccionada() {
		return this.personaSeleccionada;
	}
}