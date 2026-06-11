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

    private static Controladora instance = null;

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
    
    public List<Persona> obtenerListadoPersonas() {
        ArrayList<Persona> lista = new ArrayList<>(this.personasRegistradas.values());
        return lista;
    }
  
    public boolean validarEmail(String email) {
        if (email != null) {
            if (email.contains("@")) {
                if (email.contains(".")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void registrarPersona(String nombre, String telefono, String correo) throws Exception {
        if (nombre == null || nombre.trim().isEmpty() == true) {
            throw new Exception("Error: El nombre completo es obligatorio.");
        }
        if (this.validarEmail(correo) == false) {
            throw new Exception("Error: El formato del correo electrónico no es válido.");
        }
        
        String nombreP = nombre.toLowerCase();
        if (this.personasRegistradas.containsKey(nombreP) == true) {
            throw new Exception("Error: Ya existe una persona registrada con ese nombre.");
        }
        
        Persona nueva = new Persona(nombre, telefono, correo);
        this.personasRegistradas.put(nombreP, nueva);
    }

    public void modificarPersona(String nombre, String telefono, String correo) throws Exception {
        Persona encontrada = consultarPersona(nombre);        
        if (this.validarEmail(correo) == false) {
            throw new Exception("Error: El formato del correo electrónico no es válido.");
        }  
        encontrada.setTelefono(telefono);
        encontrada.setCorreoElectronico(correo);
    }

    public void borrarPersona(String nombre) throws Exception {
        Persona personaAEliminar = consultarPersona(nombre);
        if (personaAEliminar.getPrestamosRecibidos().isEmpty() == false) {
            throw new Exception("Error: No se puede eliminar a la persona porque tiene préstamos activos.");
        }
        this.personasRegistradas.remove(nombre.toLowerCase());
    }
    
    public Persona consultarPersona(String nombre) throws Exception {
        Persona encontrada = this.personasRegistradas.get(nombre.toLowerCase());
        if (encontrada == null) {
            throw new Exception("Error: La persona con ese nombre no existe en el sistema.");
        }
        return encontrada;
    }
    
    // CRUD ÍTEMS 

    public List<Item> obtenerListadoItems() {
        ArrayList<Item> lista = new ArrayList<>(this.itemsRegistrados.values());
        return lista;
    }
    
    public void registrarItem(String nombre, String descripcion, Tipo tipoFisico, List<Categoria> categorias) throws Exception {
        if (nombre == null || nombre.trim().isEmpty() == true) {
            throw new Exception("Error: El nombre del item es obligatorio.");
        }
        if (tipoFisico == null) {
            throw new Exception("Error: El item debe tener un tipo fisico asignado.");
        }
        Item nuevo = new Item(nombre, descripcion, tipoFisico);        
        if (categorias != null) {
            int totalCategorias = categorias.size();
            for (int j = 0; j < totalCategorias; j = j + 1) {
                nuevo.agregarCategoria(categorias.get(j));
            }
        }        
        this.itemsRegistrados.put(nuevo.getCodigo(), nuevo);        
        nuevo.vincularConTipoFisico();
        nuevo.vincularConCategorias();
    }

    public void modificarItem(int codigo, String nombre, String descripcion, Tipo tipoNuevo, List<Categoria> categoriasNuevas) throws Exception {
        Item encontrado = consultarItem(codigo);
        if (nombre == null || nombre.trim().isEmpty() == true) {
            throw new Exception("Error: El nombre no puede estar vacío.");
        }
        if (tipoNuevo == null) {
            throw new Exception("Error: El item no puede quedarse sin tipo físico.");
        }        
        encontrado.desvincularDeTipo();
        encontrado.desvincularDeCategorias();            
        
        encontrado.setNombre(nombre);
        encontrado.setDescripcion(descripcion);
        encontrado.setTipoFisico(tipoNuevo);            
        encontrado.getCategorias().clear();
        if (categoriasNuevas != null) { 
            int totalNuevas = categoriasNuevas.size();
            for (int k = 0; k < totalNuevas; k = k + 1) {
                encontrado.agregarCategoria(categoriasNuevas.get(k));
            }
        }
        encontrado.vincularConTipoFisico();
        encontrado.vincularConCategorias();
    }

    public void borrarItem(int codigo) throws Exception {
        Item encontrado = consultarItem(codigo);
        if (encontrado.isEstaPrestado() == true) {
            throw new Exception("Error: No se puede eliminar el item porque se encuentra prestado actualmente.");
        }        
        encontrado.desvincularDeTipo();
        encontrado.desvincularDeCategorias();            
        this.itemsRegistrados.remove(codigo);
    }
    
    public Item consultarItem(int codigo) throws Exception {
        Item encontrado = this.itemsRegistrados.get(codigo);
        if (encontrado == null) {
            throw new Exception("Error: El item con ese código no existe en el sistema.");
        }
        return encontrado;
    }
}