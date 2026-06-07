package logica;

public class Categoria {
    
    // Atributos
    private String nombre;

    // Constructor
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    //Getters y Setters
    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Extras

    public String toString() {
        return this.nombre;
    }
}