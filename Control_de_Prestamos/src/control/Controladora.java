package control;

import java.time.LocalDateTime;
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
    
    // MÉTODOS DE ALERTAS 
    
    public List<Alerta> verificarAlertasAlEntrar() {
        List<Alerta> pendientesDisparadas = new ArrayList<Alerta>();
        LocalDateTime ahora = LocalDateTime.now();
        
        int totalPrestamos = this.prestamosRegistrados.size();
        for (int i = 0; i < totalPrestamos; i = i + 1) {
            Prestamo prestamoActual = this.prestamosRegistrados.get(i);
            
            if (prestamoActual.tieneAlerta() == true) {
                Alerta alertaActual = prestamoActual.getRecordatorio();
                
                if (alertaActual.evaluarYProcesarAlerta(ahora) == true) {
                    pendientesDisparadas.add(alertaActual);
                }
            }
        }        
        return pendientesDisparadas;
    }
    
    public void agregarAlertaAPrestamo(Prestamo p, Alerta a) {
        if (p != null) {
            p.setRecordatorio(a);
        }
    }
    
    // MÉTODOS DE CONSULTAS

    public List<Prestamo> consultarPrestamosPorUsuario(Persona p) {
        return p.getPrestamosRecibidos(); 
    }

    public List<Prestamo> consultarPrestamosPorItem(Item itemBuscado) {
        List<Prestamo> resultado = new ArrayList<Prestamo>();
        if (itemBuscado.isEstaPrestado() == true && itemBuscado.getRegistroPrestamo() != null) {
            resultado.add(itemBuscado.getRegistroPrestamo()); 
        }
        return resultado;
    }

    public List<Item> consultarItemsPorCategoria(Categoria c) {
        return c.getItemsAsociados(); 
    }

    public List<Item> consultarItemsPorTipo(Tipo t) {
        return t.getItemsAsociados(); 
    }
    
    // CRUD PERSONAS
    
    public void registrarPersona(Persona p) {
        if (p != null) {
            this.personasRegistradas.put(p.getNombreCompleto().toLowerCase(), p);
        }
    }

    public void modificarPersona(Persona p) {
        Persona encontrada = this.consultarPersona(p.getNombreCompleto());
        if (encontrada != null) {
            encontrada.setTelefono(p.getTelefono());
            encontrada.setCorreoElectronico(p.getCorreoElectronico());
        }
    }

    public void borrarPersona(String nombre) {
        this.personasRegistradas.remove(nombre.toLowerCase());
    }

    public Persona consultarPersona(String nombre) {
        return this.personasRegistradas.get(nombre.toLowerCase());
    }
    
    // CRUD ÍTEMS 

    public void registrarItem(Item i) {
        if (i != null) {
            this.itemsRegistrados.put(i.getCodigo(), i);
            i.getTipoFisico().getItemsAsociados().add(i);
            int totalCategorias = i.getCategorias().size();
            for (int j = 0; j < totalCategorias; j = j + 1) {
                i.getCategorias().get(j).getItemsAsociados().add(i);
            }
        }
    }

    public void modificarItem(Item i) {
        Item encontrado = this.consultarItem(i.getCodigo());
        
        if (encontrado != null) {
            encontrado.desvincularDeTipo();
            encontrado.desvincularDeCategorias();            
            encontrado.setNombre(i.getNombre());
            encontrado.setDescripcion(i.getDescripcion());
            encontrado.setTipoFisico(i.getTipoFisico());            
            encontrado.getCategorias().clear();
            int totalNuevas = i.getCategorias().size();
            for (int k = 0; k < totalNuevas; k = k + 1) {
                encontrado.agregarCategoria(i.getCategorias().get(k));
            }
            encontrado.vincularConTipoFisico();
            encontrado.vincularConCategorias();
        }
    }

    public void borrarItem(int codigo) {
        Item i = this.consultarItem(codigo);
        if (i != null) {
            i.getTipoFisico().getItemsAsociados().remove(i);
            
            int totalCategorias = i.getCategorias().size();
            for (int j = 0; j < totalCategorias; j = j + 1) {
                i.getCategorias().get(j).getItemsAsociados().remove(i);
            }            
            this.itemsRegistrados.remove(codigo);
        }
    }

    public Item consultarItem(int codigo) {
        return this.itemsRegistrados.get(codigo);
    }
}