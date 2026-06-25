package interfaz;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import control.Controladora;

public class GestionarPrestamos {

	private JFrame frmGestionDePrestamos;
	private JTable tablaPrestamos;
	
	private JButton btnNuevoPrestamo;
	private JButton btnDetallePrestamo;
	private JButton btnFinalizarPrestamo;

	/**
	 * Constructor 
	 */
	public GestionarPrestamos() {
		initialize();
		this.frmGestionDePrestamos.setVisible(true);
		actualizarTablaPrestamos();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGestionDePrestamos = new JFrame();
		frmGestionDePrestamos.setBounds(100, 100, 650, 450);
		frmGestionDePrestamos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panelContenido = new JPanel();
		panelContenido.setBorder(new EmptyBorder(18, 18, 18, 18));
		panelContenido.setLayout(null); 
		frmGestionDePrestamos.getContentPane().add(panelContenido);

		JLabel lblTitulo = new JLabel("Gestión de Préstamos");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 11, 634, 25);
		panelContenido.add(lblTitulo);

		JScrollPane scrollTabla = new JScrollPane();
		scrollTabla.setBounds(10, 55, 460, 330);
		panelContenido.add(scrollTabla);

		tablaPrestamos = new JTable();
		tablaPrestamos.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Fecha", "Persona", "¿Tiene Alerta?" }
		));
		scrollTabla.setViewportView(tablaPrestamos);

		btnNuevoPrestamo = new JButton("Nuevo");
		btnNuevoPrestamo.setFont(new Font("Arial", Font.PLAIN, 14));
		btnNuevoPrestamo.setBounds(485, 55, 130, 30);
		btnNuevoPrestamo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SeleccionarPersona selector = new SeleccionarPersona(frmGestionDePrestamos);
				String personaEscogida = selector.getPersonaSeleccionada();

				if (personaEscogida != null) {
				    new DetallePrestamo(frmGestionDePrestamos, personaEscogida);
				    actualizarTablaPrestamos(); 
				}
			}
		});
		panelContenido.add(btnNuevoPrestamo);

		btnDetallePrestamo = new JButton("Detalle");
		btnDetallePrestamo.setFont(new Font("Arial", Font.PLAIN, 14));
		btnDetallePrestamo.setBounds(485, 100, 130, 30);
		btnDetallePrestamo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tablaPrestamos.getSelectedRow();
				if (filaSeleccionada != -1) {
					try {
						logica.Prestamo prestamo = Controladora.getInstance().getPrestamosRegistrados().get(filaSeleccionada);
						new DetallePrestamo(frmGestionDePrestamos, prestamo);
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(frmGestionDePrestamos, "Error al abrir detalle: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
					}
				} else {
					javax.swing.JOptionPane.showMessageDialog(frmGestionDePrestamos, 
						"Seleccione un préstamo de la tabla para ver su detalle", 
						"Atención", javax.swing.JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panelContenido.add(btnDetallePrestamo);

		btnFinalizarPrestamo = new JButton("Finalizar");
		btnFinalizarPrestamo.setFont(new Font("Arial", Font.PLAIN, 14));
		btnFinalizarPrestamo.setBounds(485, 145, 130, 30);
		btnFinalizarPrestamo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tablaPrestamos.getSelectedRow();
				if (filaSeleccionada != -1) {
					finalizarPrestamoSeleccionado();
				} else {
					javax.swing.JOptionPane.showMessageDialog(frmGestionDePrestamos, 
						"Seleccione el préstamo que desea finalizar.", 
						"Atención", javax.swing.JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panelContenido.add(btnFinalizarPrestamo);
	}

	private void actualizarTablaPrestamos() {
		DefaultTableModel modelo = (DefaultTableModel) tablaPrestamos.getModel();
		modelo.setRowCount(0); 
		try {
			for (logica.Prestamo p : Controladora.getInstance().getPrestamosRegistrados()) {
				String fecha = p.getFechaCreacion().toString().substring(0, 10);
				String persona = p.getPersonaPrestamo().getNombreCompleto();
				String tieneAlerta = p.tieneAlerta() ? "Sí (" + p.getRecordatorio().getTipoAlerta() + ")" : "No";
				
				modelo.addRow(new Object[] { fecha, persona, tieneAlerta });
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar tabla: " + ex.getMessage());
		}
	}
	
	private void finalizarPrestamoSeleccionado() {
		int filaSeleccionada = tablaPrestamos.getSelectedRow();
		
		if (filaSeleccionada != -1) {
			String nombrePersonaFila = tablaPrestamos.getValueAt(filaSeleccionada, 1).toString();
			String fechaFila = tablaPrestamos.getValueAt(filaSeleccionada, 0).toString();
			
			int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
				frmGestionDePrestamos, 
				"¿Desea marcar este préstamo como Finalizado? Esto liberará todos sus ítems y lo eliminará del sistema.", 
				"Confirmar Cierre", 
				javax.swing.JOptionPane.YES_NO_OPTION
			);
			
			if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
				try {
					logica.Prestamo prestamoAEliminar = null;
					for (logica.Prestamo p : Controladora.getInstance().getPrestamosRegistrados()) {
						String fechaP = p.getFechaCreacion().toString().substring(0, 10);
						String nombreP = p.getPersonaPrestamo().getNombreCompleto();
						
						if (nombreP.equals(nombrePersonaFila) && fechaP.equals(fechaFila)) {
							prestamoAEliminar = p;
							break;
						}
					}
					if (prestamoAEliminar != null) {
						prestamoAEliminar.finalizarPrestamo();
						Controladora.getInstance().getPrestamosRegistrados().remove(prestamoAEliminar);
						prestamoAEliminar.getPersonaPrestamo().desasociarPrestamo(prestamoAEliminar);
						actualizarTablaPrestamos(); 
						
						javax.swing.JOptionPane.showMessageDialog(
							frmGestionDePrestamos, 
							"El préstamo ha sido finalizado y eliminado con éxito. Los items están disponibles", 
							"Éxito", 
							javax.swing.JOptionPane.INFORMATION_MESSAGE
						);
					} else {
						throw new Exception("No se encontró el préstamo en la base de datos");
					}
					
				} catch (Exception ex) {
					javax.swing.JOptionPane.showMessageDialog(
						frmGestionDePrestamos, 
						"Error al finalizar el préstamo: " + ex.getMessage(), 
						"Error", 
						javax.swing.JOptionPane.ERROR_MESSAGE
					);
				}
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(
				frmGestionDePrestamos, 
				"Por favor, seleccione el préstamo que desea finalizar en la tabla.", 
				"Atención", 
				javax.swing.JOptionPane.WARNING_MESSAGE
			);
		}
	}
}