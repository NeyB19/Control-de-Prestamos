package interfaz;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import control.Controladora;
import logica.Persona;
import logica.Item;
import logica.Categoria;
import logica.Tipo;

public class Reportes {

	private JFrame frmReportes;
	private JTabbedPane tabbedPane;
	
	private JTable tablaUsuarios;
	private JButton btnActualizarUsuarios;
	private JButton btnConsultarReporteUsuario;

	private JTable tablaItems;
	private JButton btnActualizarItems;

	private JTable tablaCategorias;
	private JButton btnActualizarCategorias;

	private JTable tablaTipos;
	private JButton btnActualizarTipos;

	/**
	 * Constructor
	 */
	public Reportes() {
		initialize();
		actualizarTablaUsuarios();
		actualizarTablaItems();
		actualizarTablaCategorias();
		actualizarTablaTipos();
		
		this.frmReportes.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReportes = new JFrame();
		frmReportes.setTitle("Reportes Generales");
		frmReportes.setBounds(150, 150, 700, 480);
		frmReportes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmReportes.getContentPane().setLayout(null);

		JLabel lblTitulo = new JLabel("Reportes del Sistema");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
		lblTitulo.setBounds(0, 11, 684, 25);
		frmReportes.getContentPane().add(lblTitulo);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 47, 664, 380);
		frmReportes.getContentPane().add(tabbedPane);

		// Usuarios

		JPanel panelUsuarios = new JPanel();
		panelUsuarios.setLayout(null); 
		tabbedPane.addTab("Usuarios", null, panelUsuarios, null);

		JScrollPane scrollUsuarios = new JScrollPane();
		scrollUsuarios.setBounds(10, 11, 480, 320);
		panelUsuarios.add(scrollUsuarios);

		tablaUsuarios = new JTable();
		tablaUsuarios.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Personas con préstamos registrados:" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		});
		scrollUsuarios.setViewportView(tablaUsuarios);

		btnActualizarUsuarios = new JButton("Actualizar");
		btnActualizarUsuarios.setBounds(500, 11, 139, 23);
		btnActualizarUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarTablaUsuarios();
			}
		});
		panelUsuarios.add(btnActualizarUsuarios);

		btnConsultarReporteUsuario = new JButton("Consultar Reporte");
		btnConsultarReporteUsuario.setBounds(500, 45, 139, 23);
		btnConsultarReporteUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tablaUsuarios.getSelectedRow();
				
				if (filaSeleccionada != -1) {
					String nombreUsuario = tablaUsuarios.getValueAt(filaSeleccionada, 0).toString();
					new ReporteUsuarioEspecifico(frmReportes, nombreUsuario);
				} else {
					JOptionPane.showMessageDialog(frmReportes, 
						"Por favor, seleccione un usuario de la tabla.", 
						"Atención", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panelUsuarios.add(btnConsultarReporteUsuario);

		// Items
		JPanel panelItems = new JPanel();
		panelItems.setLayout(null);
		tabbedPane.addTab("Ítems", null, panelItems, null);

		JScrollPane scrollItems = new JScrollPane();
		scrollItems.setBounds(10, 11, 480, 320);
		panelItems.add(scrollItems);

		tablaItems = new JTable();
		tablaItems.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Código", "Nombre", "Prestado", "Usuario" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		});
		tablaItems.getColumnModel().getColumn(0).setPreferredWidth(80);
		tablaItems.getColumnModel().getColumn(1).setPreferredWidth(160);
		tablaItems.getColumnModel().getColumn(2).setPreferredWidth(90);
		tablaItems.getColumnModel().getColumn(3).setPreferredWidth(150);
		scrollItems.setViewportView(tablaItems);

		btnActualizarItems = new JButton("Actualizar");
		btnActualizarItems.setBounds(500, 11, 139, 23);
		btnActualizarItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarTablaItems();
			}
		});
		panelItems.add(btnActualizarItems);

		// Categorías
		JPanel panelCategorias = new JPanel();
		panelCategorias.setLayout(null);
		tabbedPane.addTab("Categorías", null, panelCategorias, null);

		JScrollPane scrollCategorias = new JScrollPane();
		scrollCategorias.setBounds(10, 11, 480, 320);
		panelCategorias.add(scrollCategorias);

		tablaCategorias = new JTable();
		tablaCategorias.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Categoría", "Ítem", "¿Está Prestado?", "Usuario" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		});
		scrollCategorias.setViewportView(tablaCategorias);

		btnActualizarCategorias = new JButton("Actualizar");
		btnActualizarCategorias.setBounds(500, 11, 139, 23);
		btnActualizarCategorias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarTablaCategorias();
			}
		});
		panelCategorias.add(btnActualizarCategorias);
		
		// Tipos
		
		JPanel panelTipos = new JPanel();
		panelTipos.setLayout(null);
		tabbedPane.addTab("Tipos", null, panelTipos, null);

		JScrollPane scrollTipos = new JScrollPane();
		scrollTipos.setBounds(10, 11, 480, 320);
		panelTipos.add(scrollTipos);

		tablaTipos = new JTable();
		tablaTipos.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Tipo Físico", "Ítem", "¿Está Prestado?", "Usuario" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		});
		scrollTipos.setViewportView(tablaTipos);

		btnActualizarTipos = new JButton("Actualizar");
		btnActualizarTipos.setBounds(500, 11, 139, 23);
		btnActualizarTipos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarTablaTipos();
			}
		});
		panelTipos.add(btnActualizarTipos);
	}


	private void actualizarTablaUsuarios() {
		DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
		modelo.setRowCount(0);
		try {
			for (Persona p : Controladora.getInstance().getPersonasRegistradas().values()) {
				boolean tienePrestamoActivo = false;				
				for (logica.Prestamo prestamo : p.getPrestamosRecibidos()) {
					if (!prestamo.estaFinalizado()) {
						tienePrestamoActivo = true;
						break;
					}
				}
				
				if (tienePrestamoActivo) {
					modelo.addRow(new Object[] { p.getNombreCompleto() });
				}
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar pestaña de usuarios: " + ex.getMessage());
		}
	}

	private void actualizarTablaItems() {
		DefaultTableModel modelo = (DefaultTableModel) tablaItems.getModel();
		modelo.setRowCount(0);
		try {
			for (Item item : Controladora.getInstance().getItemsRegistrados().values()) {
				String estado = item.isEstaPrestado() ? "Sí" : "No";
				String usuario = "Disponible";
				
				if (item.isEstaPrestado() && item.getRegistroPrestamo() != null) {
					usuario = item.getRegistroPrestamo().getPersonaPrestamo().getNombreCompleto();
				}
				
				modelo.addRow(new Object[] { item.getCodigo(), item.getNombre(), estado, usuario });
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar pestaña de ítems: " + ex.getMessage());
		}
	}

	private void actualizarTablaCategorias() {
		DefaultTableModel modelo = (DefaultTableModel) tablaCategorias.getModel();
		modelo.setRowCount(0);
		try {
			for (Categoria cat : Controladora.getInstance().getCategoriasRegistradas().values()) {
				for (Item item : cat.getItemsAsociados()) {
					String estado = item.isEstaPrestado() ? "Sí" : "No";
					String usuario = "Disponible";
					
					if (item.isEstaPrestado() && item.getRegistroPrestamo() != null) {
						usuario = item.getRegistroPrestamo().getPersonaPrestamo().getNombreCompleto();
					}
					
					modelo.addRow(new Object[] { cat.getNombre(), item.getNombre(), estado, usuario });
				}
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar pestaña de categorías: " + ex.getMessage());
		}
	}

	private void actualizarTablaTipos() {
		DefaultTableModel modelo = (DefaultTableModel) tablaTipos.getModel();
		modelo.setRowCount(0);
		try {
			for (Tipo tipo : Controladora.getInstance().getTiposRegistrados().values()) {
				for (Item item : tipo.getItemsAsociados()) {
					String estado = item.isEstaPrestado() ? "Sí" : "No";
					String usuario = "Disponible";
					
					if (item.isEstaPrestado() && item.getRegistroPrestamo() != null) {
						usuario = item.getRegistroPrestamo().getPersonaPrestamo().getNombreCompleto();
					}
					
					modelo.addRow(new Object[] { tipo.getNombre(), item.getNombre(), estado, usuario });
				}
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar pestaña de tipos: " + ex.getMessage());
		}
	}
}