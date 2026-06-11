package logica;

import java.time.LocalDateTime;
import java.util.List;

public class Prestamo {

    // Atributos
    private LocalDateTime fechaCreacion;

    // Relaciones
    private Persona personaPrestamo; 
    private List<Item> itemsPrestados;
    private Alerta recordatorio;

    // Constructor
    public Prestamo(Persona personaPrestamo, List<Item> items, Alerta recordatorio) {
        this.fechaCreacion = LocalDateTime.now(); 
        this.personaPrestamo = personaPrestamo;
        this.itemsPrestados = items;
        this.recordatorio = recordatorio;
    }

    // Getters y Setters

    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Persona getPersonaPrestamo() {
        return this.personaPrestamo;
    }

    public void setPersonaPrestamo(Persona persona) {
        this.personaPrestamo = persona;
    }

    public List<Item> getItemsPrestados() {
        return this.itemsPrestados;
    }

    public Alerta getRecordatorio() {
        return this.recordatorio;
    }

    public void setRecordatorio(Alerta alerta) {
        this.recordatorio = alerta;
    }

    // Métodos
    
    public void finalizarPrestamo() {
        int cantidadDeItems = this.itemsPrestados.size();     
        for (int i = 0; i < cantidadDeItems; i = i + 1) {            
            Item itemActual = this.itemsPrestados.get(i);
            itemActual.marcarComoDisponible();
            
        } 
    }
    
    public boolean estaFinalizado() {
        if (this.itemsPrestados.size() == 0) {
            return true;
        }
        
        return false;
    }
    
    public boolean tieneAlerta() {
        if (this.recordatorio != null) {
            return true;
        }
        
        return false;
    }

    public void retornarItem(Item i) {
        i.marcarComoDisponible();
        this.itemsPrestados.remove(i);
    }

    public void agregarItem(Item i) {
        this.itemsPrestados.add(i);
    }

    // Extra

    public String toString() {
        return "\nPrestado a: " + this.personaPrestamo.getNombreCompleto() +
               "\nFecha de creación: " + this.fechaCreacion +
               "\nCantidad de ítems: " + this.itemsPrestados.size() +
               "\n¿Tiene alerta?: " + (this.tieneAlerta() ? "Sí" : "No") +
               "\n-------------------------";
    }
}