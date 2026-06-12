package logica;

import java.util.ArrayList;
import java.util.List;

public class Tipo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String nombre;
    private List<Item> itemsAsociados;
    
    // Constructor
    public Tipo(String nombre) {
        this.nombre = nombre;
        this.itemsAsociados = new ArrayList<Item>();
    }

    //Getters y Setters
    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Item> getItemsAsociados() {
		return itemsAsociados;
	}
    
    // Métodos
    
    public void reasignarItems(Tipo tipoGenerico) {
        if (tipoGenerico != null) {
            int total = this.itemsAsociados.size();            
            for (int i = 0; i < total; i = i + 1) {
                Item itemSinTipo = this.itemsAsociados.get(i);               
                itemSinTipo.setTipoFisico(tipoGenerico);                
                tipoGenerico.getItemsAsociados().add(itemSinTipo);
            }            
            this.itemsAsociados.clear();
        }
    }
    
    // Extras
    
    public String toString() {
        return this.nombre;
    }
}