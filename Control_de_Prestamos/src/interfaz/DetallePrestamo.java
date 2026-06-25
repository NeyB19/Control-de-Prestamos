package interfaz;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import control.Controladora;
import logica.Item;

public class DetallePrestamo extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JLabel lblFechaPrestamo;
	private JLabel lblEstadoPrestamo;
	private JLabel lblUsuarioPrestamo;

	private JTable tablaItemsDetalle;
	private JButton btnAgregarItem;
	private JButton btnRetornarItem;
	private JButton btnEliminarItem;

	private JCheckBox chkAlertaActiva;
	private JComboBox<String> comboTipoAlerta;
	private JTextField txtMensajeAlerta;
	private JTextField txtMinutosRepeticion;
	
	private JButton btnGuardarPrestamo;
	private JButton btnCerrarVentana; 
	private String usuarioSeleccionado;

	private List<Integer> codigosItemsTemporales = new ArrayList<>();

	public DetallePrestamo(JFrame padre, String usuarioSeleccionado) {
		super(padre, "Registrar Nuevo Préstamo", true);
		this.usuarioSeleccionado = usuarioSeleccionado; 
		initialize();
		lblUsuarioPrestamo.setText("Solicitante: " + this.usuarioSeleccionado);
		lblEstadoPrestamo.setText("Estado: ACTIVO");
		lblFechaPrestamo.setText("Fecha: " + java.time.LocalDateTime.now().toString().substring(0, 10));
		this.setVisible(true);
	}
	
	public DetallePrestamo(JFrame padre, logica.Prestamo prestamoExistente) {
		super(padre, "Detalle de Préstamo", true);
		initialize();
		
		this.usuarioSeleccionado = prestamoExistente.getPersonaPrestamo().getNombreCompleto();
		lblUsuarioPrestamo.setText("Solicitante: " + usuarioSeleccionado);
		lblFechaPrestamo.setText("Fecha: " + prestamoExistente.getFechaCreacion().toString().substring(0, 10));
		lblEstadoPrestamo.setText(prestamoExistente.estaFinalizado() ? "Estado: FINALIZADO" : "Estado: ACTIVO");
		
		btnAgregarItem.setEnabled(false);
		btnEliminarItem.setEnabled(false);
		btnRetornarItem.setEnabled(!prestamoExistente.estaFinalizado()); 
		
		DefaultTableModel modelo = (DefaultTableModel) tablaItemsDetalle.getModel();
		for (logica.Item item : prestamoExistente.getItemsPrestados()) {
			modelo.addRow(new Object[] { item.getCodigo(), item.getNombre() });
		}
		
		if (prestamoExistente.tieneAlerta()) {
			chkAlertaActiva.setSelected(true);
			comboTipoAlerta.setSelectedItem(prestamoExistente.getRecordatorio().getTipoAlerta());
			txtMensajeAlerta.setText(prestamoExistente.getRecordatorio().getMensaje());
			txtMinutosRepeticion.setText(String.valueOf(prestamoExistente.getRecordatorio().getTiempoActivacion())); 
		}
		
		chkAlertaActiva.setEnabled(false);
		comboTipoAlerta.setEnabled(false);
		txtMensajeAlerta.setEditable(false);
		txtMinutosRepeticion.setEditable(false);
		btnGuardarPrestamo.setEnabled(false); 
		
		this.setVisible(true);
	}

	private void initialize() {
		this.setBounds(150, 150, 600, 500); 
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPanel.setLayout(null);
		this.getContentPane().add(contentPanel);

		lblUsuarioPrestamo = new JLabel("Solicitante: ");
		lblUsuarioPrestamo.setFont(new Font("Arial", Font.BOLD, 13));
		lblUsuarioPrestamo.setBounds(15, 15, 300, 18);
		contentPanel.add(lblUsuarioPrestamo);

		lblFechaPrestamo = new JLabel("Fecha: ");
		lblFechaPrestamo.setFont(new Font("Arial", Font.BOLD, 13));
		lblFechaPrestamo.setBounds(15, 38, 200, 18);
		contentPanel.add(lblFechaPrestamo);

		lblEstadoPrestamo = new JLabel("Estado: ");
		lblEstadoPrestamo.setFont(new Font("Arial", Font.BOLD, 13));
		lblEstadoPrestamo.setBounds(350, 15, 200, 18);
		contentPanel.add(lblEstadoPrestamo);

		JScrollPane scrollTabla = new JScrollPane();
		scrollTabla.setBounds(15, 75, 420, 180);
		contentPanel.add(scrollTabla);

		tablaItemsDetalle = new JTable();
		tablaItemsDetalle.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Código", "Nombre del Ítem" }
		));
		scrollTabla.setViewportView(tablaItemsDetalle);

		btnAgregarItem = new JButton("Agregar Ítem");
		btnAgregarItem.setBounds(450, 75, 120, 25);
		btnAgregarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarItem();				
			}
		});
		contentPanel.add(btnAgregarItem);

		btnRetornarItem = new JButton("Retornar Ítem");
		btnRetornarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				retornarItemExistente();
			}
		});
		btnRetornarItem.setBounds(450, 110, 120, 25);
		btnRetornarItem.setEnabled(false); 
		contentPanel.add(btnRetornarItem);

		btnEliminarItem = new JButton("Eliminar Ítem");
		btnEliminarItem.setBounds(450, 145, 120, 25);
		btnEliminarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminarItem();
			}
		});
		contentPanel.add(btnEliminarItem);

		chkAlertaActiva = new JCheckBox("Configurar Alerta");
		chkAlertaActiva.setFont(new Font("Arial", Font.BOLD, 13));
		chkAlertaActiva.setBounds(15, 275, 300, 22);
		contentPanel.add(chkAlertaActiva);

		JLabel lblTipo = new JLabel("Tipo de alerta:");
		lblTipo.setBounds(15, 310, 90, 20);
		contentPanel.add(lblTipo);

		comboTipoAlerta = new JComboBox<String>(new String[] { "Única", "Recurrente" });
		comboTipoAlerta.setBounds(100, 310, 140, 22);
		contentPanel.add(comboTipoAlerta);

		JLabel lblMinutos = new JLabel("Repetir (minutos después):");
		lblMinutos.setBounds(260, 310, 136, 20);
		contentPanel.add(lblMinutos);

		txtMinutosRepeticion = new JTextField();
		txtMinutosRepeticion.setBounds(406, 309, 100, 22);
		contentPanel.add(txtMinutosRepeticion);

		JLabel lblMensaje = new JLabel("Mensaje:");
		lblMensaje.setBounds(15, 345, 90, 20);
		contentPanel.add(lblMensaje);

		txtMensajeAlerta = new JTextField();
		txtMensajeAlerta.setBounds(100, 345, 380, 22);
		contentPanel.add(txtMensajeAlerta);
		
		btnCerrarVentana = new JButton("Cerrar");
		btnCerrarVentana.setFont(new Font("Arial", Font.PLAIN, 13));
		btnCerrarVentana.setBounds(250, 410, 155, 30); 
		btnCerrarVentana.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); 
			}
		});
		contentPanel.add(btnCerrarVentana);
		
		btnGuardarPrestamo = new JButton("Guardar Préstamo");
		btnGuardarPrestamo.setFont(new Font("Arial", Font.PLAIN, 13));
		btnGuardarPrestamo.setBounds(415, 410, 155, 30);
		btnGuardarPrestamo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarPrestamo();
			}
		});
		contentPanel.add(btnGuardarPrestamo);
	}

	private void agregarItem() {
		SeleccionarItem selector = new SeleccionarItem(this, codigosItemsTemporales);
		int codigoEscogido = selector.getCodigoItemSeleccionado();

		if (codigoEscogido != -1) {
			if (codigosItemsTemporales.contains(codigoEscogido)) {
				JOptionPane.showMessageDialog(this, "Este ítem ya está agregado en la lista actual", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				Item item = Controladora.getInstance().consultarItem(codigoEscogido);
				codigosItemsTemporales.add(codigoEscogido);
				
				DefaultTableModel modelo = (DefaultTableModel) tablaItemsDetalle.getModel();
				modelo.addRow(new Object[] { item.getCodigo(), item.getNombre() });
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void eliminarItem() {
		int fila = tablaItemsDetalle.getSelectedRow();
		if (fila != -1) {
			int codigo = Integer.parseInt(tablaItemsDetalle.getValueAt(fila, 0).toString());
			codigosItemsTemporales.remove(Integer.valueOf(codigo));
			
			DefaultTableModel modelo = (DefaultTableModel) tablaItemsDetalle.getModel();
			modelo.removeRow(fila);
		} else {
			JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para eliminarlo", "Atención", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void retornarItemExistente() {
	    int fila = tablaItemsDetalle.getSelectedRow();
	    if (fila != -1) {
	        int codigo = Integer.parseInt(tablaItemsDetalle.getValueAt(fila, 0).toString());
	        
	        int confirmacion = JOptionPane.showConfirmDialog(this, 
	            "¿Desea registrar el retorno de este ítem individual?", 
	            "Confirmar Devolución", JOptionPane.YES_NO_OPTION);
	            
	        if (confirmacion == JOptionPane.YES_OPTION) {
	            try {
	                logica.Item itemReal = Controladora.getInstance().consultarItem(codigo);
	                
	                if (itemReal != null) {
	                    itemReal.marcarComoDisponible(); 
	                    
	                    if (itemReal.getRegistroPrestamo() != null) {
	                        itemReal.getRegistroPrestamo().getItemsPrestados().remove(itemReal);
	                    }
	                    
	                    DefaultTableModel modelo = (DefaultTableModel) tablaItemsDetalle.getModel();
	                    modelo.removeRow(fila);
	                    
	                    Controladora.getInstance().guardarDatos();
	                    
	                    JOptionPane.showMessageDialog(this, "Ítem retornado y disponible con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
	                }
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(this, "Error al retornar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "Seleccione el ítem que desea retornar de la tabla", "Atención", JOptionPane.WARNING_MESSAGE);
	    }
	}

	private void guardarPrestamo() {
		if (this.usuarioSeleccionado == null || this.usuarioSeleccionado.isEmpty()) {
			if (lblUsuarioPrestamo.getText().contains("Solicitante: ")) {
				this.usuarioSeleccionado = lblUsuarioPrestamo.getText().replace("Solicitante: ", "").trim();
			}
		}

		if (this.usuarioSeleccionado == null || this.usuarioSeleccionado.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error interno: No se ha recuperado el solicitante del préstamo.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (codigosItemsTemporales.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe asociar al menos un ítem al préstamo", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String tipoAlerta = "";
		int minutesAlerta = 0;
		String mensajeAlerta = "";

		if (chkAlertaActiva.isSelected()) {
			tipoAlerta = comboTipoAlerta.getSelectedItem().toString();
			mensajeAlerta = txtMensajeAlerta.getText().trim();
			
			if (mensajeAlerta.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Debe ingresar un mensaje para la alerta", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				minutesAlerta = Integer.parseInt(txtMinutosRepeticion.getText().trim());
				if (minutesAlerta <= 0) throw new Exception();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Por favor ingrese un número de minutos válido y mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		try {
			Controladora.getInstance().registrarPrestamo(usuarioSeleccionado, codigosItemsTemporales, tipoAlerta, minutesAlerta, mensajeAlerta);
			JOptionPane.showMessageDialog(this, "Préstamo registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}