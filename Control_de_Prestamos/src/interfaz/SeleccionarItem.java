package interfaz;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import control.Controladora;
import logica.Item;

public class SeleccionarItem extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tablaItemsSelector;
	private JButton btnConfirmar;
	private JButton btnCancelar;	
	private int codigoItemSeleccionado = -1;
	private List<Integer> excluidos;

	public SeleccionarItem(JDialog padre, List<Integer> excluidos) {
		super(padre, "Seleccionar Ítem para Préstamo", true);
		this.excluidos = excluidos;
		initialize();
		llenarTablaItems();
		this.setVisible(true);
	}

	private void initialize() {
		this.setBounds(150, 150, 520, 380);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel panelContenido = new JPanel();
		panelContenido.setBorder(new EmptyBorder(15, 15, 15, 15));
		panelContenido.setLayout(null);
		this.getContentPane().add(panelContenido);

		JLabel lblInstruccion = new JLabel("Seleccione un ítem disponible para agregar:");
		lblInstruccion.setFont(new Font("Arial", Font.BOLD, 14));
		lblInstruccion.setBounds(15, 15, 470, 22);
		panelContenido.add(lblInstruccion);

		JScrollPane scrollTabla = new JScrollPane();
		scrollTabla.setBounds(15, 50, 470, 220);
		panelContenido.add(scrollTabla);

		tablaItemsSelector = new JTable();
		tablaItemsSelector.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Código", "Nombre", "Descripción", "Tipo" }
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false; 
			}
		});
		scrollTabla.setViewportView(tablaItemsSelector);

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setFont(new Font("Arial", Font.PLAIN, 13));
		btnConfirmar.setBounds(230, 290, 120, 30);
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tablaItemsSelector.getSelectedRow();
				if (fila != -1) {
					codigoItemSeleccionado = Integer.parseInt(tablaItemsSelector.getValueAt(fila, 0).toString());
					dispose();
				} else {
					JOptionPane.showMessageDialog(SeleccionarItem.this, 
						"Por favor, seleccione un ítem de la tabla.", 
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
				codigoItemSeleccionado = -1; 
				dispose();
			}
		});
		panelContenido.add(btnCancelar);
	}

	private void llenarTablaItems() {
		DefaultTableModel modelo = (DefaultTableModel) tablaItemsSelector.getModel();
		modelo.setRowCount(0); 
		
		try {
			for (Item item : Controladora.getInstance().getItemsRegistrados().values()) {
				if (!item.isEstaPrestado() && !excluidos.contains(item.getCodigo())) {
					modelo.addRow(new Object[] {
						item.getCodigo(),
						item.getNombre(),
						item.getDescripcion(),
						item.getTipoFisico().getNombre()
					});
				}
			}
		} catch (Exception ex) {
			System.out.println("Error al cargar los ítems: " + ex.getMessage());
		}
	}

	public int getCodigoItemSeleccionado() {
		return this.codigoItemSeleccionado;
	}
}