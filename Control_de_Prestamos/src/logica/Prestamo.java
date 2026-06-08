package logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
}