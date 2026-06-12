package logica;

import java.util.ArrayList;
import java.util.List;

public class Persona implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private String nombreCompleto;
    private String telefono;
    private String correoElectronico;
    
    private List<Prestamo> prestamosRecibidos;

    // Constructor
    public Persona(String nombreCompleto, String telefono, String correoElectronico) {
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.prestamosRecibidos = new ArrayList<Prestamo>();
    }

    //Getters y Setters
    
    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return this.correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public List<Prestamo> getPrestamosRecibidos() {
		return prestamosRecibidos;
	}
    
    // Métodos
    
    public void asociarPrestamo(Prestamo p) {
        if (p != null) {
            this.prestamosRecibidos.add(p);
        }
    }
    
    public void desasociarPrestamo(Prestamo p) {
        if (p != null) {
            this.prestamosRecibidos.remove(p);
        }
    }
    
    // Extras

	public String toString() {
        return "\nNombre: " + this.nombreCompleto +
               "\nTeléfono: " + this.telefono +
               "\nCorreo: " + this.correoElectronico +
               "\n-------------------------";
    }
}