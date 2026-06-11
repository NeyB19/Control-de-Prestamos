package control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logica.Categoria;
import logica.Item;
import logica.Persona;
import logica.Prestamo;
import logica.Tipo;
import logica.Alerta;

public class Controladora {

    private static Controladora instance;

    private Map<String, Persona> personasRegistradas;
    private Map<Integer, Item> itemsRegistrados;
    private Map<String, Categoria> categoriasRegistradas;
    private Map<String, Tipo> tiposRegistrados;
    private List<Prestamo> prestamosRegistrados;

    private Controladora() {
        this.personasRegistradas = new TreeMap<String, Persona>();
        this.itemsRegistrados = new TreeMap<Integer, Item>();
        this.categoriasRegistradas = new TreeMap<String, Categoria>();
        this.tiposRegistrados = new TreeMap<String, Tipo>();
        this.prestamosRegistrados = new ArrayList<Prestamo>();
    }

    public static Controladora getInstance() {
        if (instance == null) {
            instance = new Controladora();
        }
        
        return instance;
    }

    // MÉTODOS DE PRÉSTAMOS

    public void registrarPrestamo(Persona personaPrestamo, List<Item> items, Alerta alerta) {
        Prestamo nuevoPrestamo = new Prestamo(personaPrestamo, items, alerta);
        this.prestamosRegistrados.add(nuevoPrestamo);        
        personaPrestamo.getPrestamosRecibidos().add(nuevoPrestamo);        
        int cantidadItems = items.size();
        for (int i = 0; i < cantidadItems; i = i + 1) {
            Item itemActual = items.get(i);
            itemActual.marcarComoPrestado();
            itemActual.setRegistroPrestamo(nuevoPrestamo); 
        }
    }

    public void finalizarPrestamo(Prestamo prestamo) {
        if (prestamo != null) {            
            prestamo.finalizarPrestamo();            
            Persona p = prestamo.getPersonaPrestamo();
            p.getPrestamosRecibidos().remove(prestamo);            
            this.prestamosRegistrados.remove(prestamo);
        }
    }

    public void retornarItemDePrestamo(Prestamo p, Item i) {
        if (p != null) {
            if (i != null) {                
                p.retornarItem(i);                
                if (p.estaFinalizado() == true) {
                    this.finalizarPrestamo(p);
                }
            }
        }
    }

    public void agregarItemAPrestamo(Prestamo p, Item i) {
        if (p != null) {
            if (i != null) {                
                p.agregarItem(i);                
                i.marcarComoPrestado();
                i.setRegistroPrestamo(p);
            }
        }
    }

    public void eliminarItemDePrestamo(Prestamo p, Item i) {
        this.retornarItemDePrestamo(p, i);
    }
}