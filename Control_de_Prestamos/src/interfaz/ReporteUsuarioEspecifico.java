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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import control.Controladora;
import logica.Persona;
import logica.Prestamo;
import logica.Item;

public class ReporteUsuarioEspecifico extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tablaDetallePrestamos;
	private JButton btnCerrar;
	private String nombreUsuario;
	
	private JLabel lblNombre;
	private JLabel lblTelefono;
	private JLabel lblCorreo;

	/**
	 * Constructor
	 */
	public ReporteUsuarioEspecifico(JFrame padre, String nombreUsuario) {
		super(padre, "Reporte de Usuario", true); 
		this.nombreUsuario = nombreUsuario;
		initialize();
		cargarDatosConsultados();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the dialog.
	 */
	private void initialize() {
		this.setBounds(200, 200, 560, 420);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel panelContenido = new JPanel();
		panelContenido.setBorder(new EmptyBorder(15, 15, 15, 15));
		panelContenido.setLayout(null); 
		this.getContentPane().add(panelContenido);

		lblNombre = new JLabel("Nombre: " + nombreUsuario);
		lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
		lblNombre.setBounds(15, 15, 510, 22);
		panelContenido.add(lblNombre);

		lblTelefono = new JLabel("Teléfono: "); 
		lblTelefono.setFont(new Font("Arial", Font.BOLD, 13));
		lblTelefono.setBounds(15, 42, 240, 18);
		panelContenido.add(lblTelefono);

		lblCorreo = new JLabel("Correo: "); 
		lblCorreo.setFont(new Font("Arial", Font.BOLD, 13));
		lblCorreo.setBounds(265, 42, 260, 18);
		panelContenido.add(lblCorreo);

		JScrollPane scrollTabla = new JScrollPane();
		scrollTabla.setBounds(15, 80, 510, 240); 
		panelContenido.add(scrollTabla);

		tablaDetallePrestamos = new JTable();
		tablaDetallePrestamos.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Código", "Nombre Ítem", "Descripción", "Tipo" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) { return false; }
		});
		scrollTabla.setViewportView(tablaDetallePrestamos);

		btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Arial", Font.PLAIN, 13));
		btnCerrar.setBounds(385, 335, 140, 28);
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); 
			}
		});
		panelContenido.add(btnCerrar);
	}

	private void cargarDatosConsultados() {
		try {
			Persona persona = Controladora.getInstance().getPersonasRegistradas().get(nombreUsuario);
			
			if (persona != null) {
				lblTelefono.setText("Teléfono: " + persona.getTelefono());
				lblCorreo.setText("Correo: " + persona.getCorreoElectronico());
				
				DefaultTableModel modelo = (DefaultTableModel) tablaDetallePrestamos.getModel();
				modelo.setRowCount(0); 
				for (Prestamo prestamo : persona.getPrestamosRecibidos()) {
					for (Item item : prestamo.getItemsPrestados()) {
						modelo.addRow(new Object[] {
							item.getCodigo(),
							item.getNombre(),
							item.getDescripcion(),
							item.getTipoFisico().getNombre() 
						});
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error al generar el reporte detallado del usuario: " + ex.getMessage());
		}
	}
}