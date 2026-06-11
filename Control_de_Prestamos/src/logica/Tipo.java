package logica;

import java.util.ArrayList;
import java.util.List;

public class Tipo {
    
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
    
    // Extras
    
    public String toString() {
        return this.nombre;
    }
}