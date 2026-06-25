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
import javax.swing.table.DefaultTableModel;

import control.Controladora;
import logica.Persona;
import logica.Item;
import logica.Categoria;
import logica.Tipo;

public class Administrar {

	private JFrame frmAdministracion;
	
	private JTable tablaPersonas;
	private JButton btnAgregarPersona;
	private JButton btnEditarPersona;
	private JButton btnBorrarPersona;

	private JTable tablaItems;
	private JButton btnAgregarItem;
	private JButton btnEditarItem;
	private JButton btnBorrarItem;

	private JTable tablaCategorias;
	private JButton btnAgregarCategoria;
	private JButton btnBorrarCategoria;

	private JTable tablaTipos;
	private JButton btnAgregarTipo;
	private JButton btnBorrarTipo;
	private JButton btnConsultarItem;
	private JButton btnConsultarPersona;
	private JButton btnEditarCategoria;
	private JButton btnConsultarCategoria;
	private JButton btnEditarTipo;
	private JButton btnConsultarTipo;

	/**
	 * Constructor
	 */
	public Administrar() {
		initialize();
		llenarTablaPersonas();
		llenarTablaItems();
		llenarTablaCategorias();
		llenarTablaTipos();
		
		this.frmAdministracion.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAdministracion = new JFrame();
		frmAdministracion.setBounds(120, 120, 650, 450);
		frmAdministracion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAdministracion.getContentPane().setLayout(null);

		JLabel lblTitulo = new JLabel("Administración del Sistema");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
		lblTitulo.setBounds(0, 11, 634, 25);
		frmAdministracion.getContentPane().add(lblTitulo);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 47, 614, 353);
		frmAdministracion.getContentPane().add(tabbedPane);

		JPanel panelPersonas = new JPanel();
		panelPersonas.setLayout(null); 
		tabbedPane.addTab("Personas", null, panelPersonas, null);

		JScrollPane scrollPersonas = new JScrollPane();
		scrollPersonas.setBounds(10, 11, 460, 303);
		panelPersonas.add(scrollPersonas);

		tablaPersonas = new JTable();
		tablaPersonas.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Nombre Completo", "Teléfono", "Correo Electrónico" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) { return false; }
		});
		scrollPersonas.setViewportView(tablaPersonas);

		btnAgregarPersona = new JButton("Agregar");
		btnAgregarPersona.setBounds(480, 11, 119, 23);
		btnAgregarPersona.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// falta
			}
		});
		panelPersonas.add(btnAgregarPersona);

		btnEditarPersona = new JButton("Editar");
		btnEditarPersona.setBounds(480, 45, 119, 23);
		panelPersonas.add(btnEditarPersona);

		btnBorrarPersona = new JButton("Borrar");
		btnBorrarPersona.setBounds(480, 79, 119, 23);
		panelPersonas.add(btnBorrarPersona);
		
		btnConsultarPersona = new JButton("Consultar");
		btnConsultarPersona.setBounds(480, 113, 119, 22);
		panelPersonas.add(btnConsultarPersona);

		JPanel panelItems = new JPanel();
		panelItems.setLayout(null);
		tabbedPane.addTab("Ítems", null, panelItems, null);

		JScrollPane scrollItems = new JScrollPane();
		scrollItems.setBounds(10, 11, 460, 303);
		panelItems.add(scrollItems);

		tablaItems = new JTable();
		tablaItems.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Código", "Nombre", "Descripción", "Tipo", "Estado" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) { return false; }
		});
		scrollItems.setViewportView(tablaItems);

		btnAgregarItem = new JButton("Agregar");
		btnAgregarItem.setBounds(480, 11, 119, 23);
		btnAgregarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// falta
			}
		});
		panelItems.add(btnAgregarItem);

		btnEditarItem = new JButton("Editar");
		btnEditarItem.setBounds(480, 45, 119, 23);
		panelItems.add(btnEditarItem);

		btnBorrarItem = new JButton("Borrar");
		btnBorrarItem.setBounds(480, 79, 119, 23);
		panelItems.add(btnBorrarItem);
		
		btnConsultarItem = new JButton("Consultar");
		btnConsultarItem.setBounds(480, 113, 119, 22);
		panelItems.add(btnConsultarItem);

		JPanel panelCategorias = new JPanel();
		panelCategorias.setLayout(null);
		tabbedPane.addTab("Categorías", null, panelCategorias, null);

		JScrollPane scrollCategorias = new JScrollPane();
		scrollCategorias.setBounds(10, 11, 460, 303);
		panelCategorias.add(scrollCategorias);

		tablaCategorias = new JTable();
		tablaCategorias.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Nombre" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) { return false; }
		});
		scrollCategorias.setViewportView(tablaCategorias);

		btnAgregarCategoria = new JButton("Agregar");
		btnAgregarCategoria.setBounds(480, 11, 119, 23);
		panelCategorias.add(btnAgregarCategoria);

		btnBorrarCategoria = new JButton("Borrar");
		btnBorrarCategoria.setBounds(480, 45, 119, 23);
		panelCategorias.add(btnBorrarCategoria);
		
		btnEditarCategoria = new JButton("Editar");
		btnEditarCategoria.setBounds(480, 79, 119, 22);
		panelCategorias.add(btnEditarCategoria);
		
		btnConsultarCategoria = new JButton("Consultar");
		btnConsultarCategoria.setBounds(480, 112, 119, 22);
		panelCategorias.add(btnConsultarCategoria);

		JPanel panelTipos = new JPanel();
		panelTipos.setLayout(null);
		tabbedPane.addTab("Tipos", null, panelTipos, null);

		JScrollPane scrollTipos = new JScrollPane();
		scrollTipos.setBounds(10, 11, 460, 303);
		panelTipos.add(scrollTipos);

		tablaTipos = new JTable();
		tablaTipos.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Nombre" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int col) { return false; }
		});
		scrollTipos.setViewportView(tablaTipos);

		btnAgregarTipo = new JButton("Agregar");
		btnAgregarTipo.setBounds(480, 11, 119, 23);
		panelTipos.add(btnAgregarTipo);

		btnBorrarTipo = new JButton("Borrar");
		btnBorrarTipo.setBounds(480, 45, 119, 23);
		panelTipos.add(btnBorrarTipo);
		
		btnEditarTipo = new JButton("Editar");
		btnEditarTipo.setBounds(480, 79, 119, 22);
		panelTipos.add(btnEditarTipo);
		
		btnConsultarTipo = new JButton("Consultar");
		btnConsultarTipo.setBounds(480, 112, 119, 22);
		panelTipos.add(btnConsultarTipo);
	}


	private void llenarTablaPersonas() {
		DefaultTableModel modelo = (DefaultTableModel) tablaPersonas.getModel();
		modelo.setRowCount(0);
		try {
			for (Persona p : Controladora.getInstance().getPersonasRegistradas().values()) {
				modelo.addRow(new Object[] { p.getNombreCompleto(), p.getTelefono(), p.getCorreoElectronico() });
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar los datos de las personas: " + ex.getMessage());
		}
	}

	private void llenarTablaItems() {
		DefaultTableModel modelo = (DefaultTableModel) tablaItems.getModel();
		modelo.setRowCount(0);
		try {
			java.util.List<Item> listaOrdenada = new java.util.ArrayList<>(Controladora.getInstance().getItemsRegistrados().values());			
			Controladora.getInstance().ordenarItemsPorNombre(listaOrdenada);			
			for (Item item : listaOrdenada) {
				String estado = item.isEstaPrestado() ? "Prestado" : "Disponible";
				modelo.addRow(new Object[] { 
					item.getCodigo(), 
					item.getNombre(), 
					item.getDescripcion(), 
					item.getTipoFisico().getNombre(), 
					estado 
				});
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar los ítems: " + ex.getMessage());
		}
	}

	private void llenarTablaCategorias() {
		DefaultTableModel modelo = (DefaultTableModel) tablaCategorias.getModel();
		modelo.setRowCount(0);
		try {
			for (Categoria cat : Controladora.getInstance().getCategoriasRegistradas().values()) {
				modelo.addRow(new Object[] { cat.getNombre() });
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar las categorías: " + ex.getMessage());
		}
	}

	private void llenarTablaTipos() {
		DefaultTableModel modelo = (DefaultTableModel) tablaTipos.getModel();
		modelo.setRowCount(0);
		try {
			for (Tipo tipo : Controladora.getInstance().getTiposRegistrados().values()) {
				modelo.addRow(new Object[] { tipo.getNombre() });
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar los tipos físicos: " + ex.getMessage());
		}
	}
}