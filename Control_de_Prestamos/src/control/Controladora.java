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

public class Controladora implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

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
        Tipo base = new Tipo("Genérico");
        this.tiposRegistrados.put("generico", base);
    }

    public static Controladora getInstance() {
        if (instance == null) {
            instance = new Controladora();
        }
        
        return instance;
    }

    // MÉTODOS DE PRÉSTAMOS

    public void registrarPrestamo(String nombrePersona, List<Integer> codigosItems, String tipoAlerta, int minutosAlerta, String mensajeAlerta) throws Exception {
        Persona persona = this.personasRegistradas.get(nombrePersona.trim());
        if (persona == null) {
            throw new Exception("Error: Esa persona no existe");
        }

        List<Item> itemsReales = new ArrayList<>();
        for (int i = 0; i < codigosItems.size(); i++) {
            Item item = this.itemsRegistrados.get(codigosItems.get(i));
            if (item != null) {
                if (item.isEstaPrestado()) {
                    throw new Exception("Error: El ítem ya está prestado");
                }
                itemsReales.add(item);
            }
        }

        if (itemsReales.isEmpty()) {
            throw new Exception("Error: Debe asociar al menos un ítem al préstamo.");
        }

        Alerta alerta = null;
        if (tipoAlerta != null && !tipoAlerta.trim().isEmpty()) {
            alerta = new Alerta(tipoAlerta, LocalDateTime.now().plusMinutes(minutosAlerta), mensajeAlerta, minutosAlerta);
        }

        Prestamo nuevoPrestamo = new Prestamo(persona, itemsReales, alerta);
        this.prestamosRegistrados.add(nuevoPrestamo);        
        persona.asociarPrestamo(nuevoPrestamo);       

        for (int i = 0; i < itemsReales.size(); i++) {
            Item itemActual = itemsReales.get(i);
            itemActual.vincularAPrestamo(nuevoPrestamo);
        }
    }

    public void finalizarPrestamo(int indicePrestamo) throws Exception {
        if (indicePrestamo < 0 || indicePrestamo >= this.prestamosRegistrados.size()) {
            throw new Exception("Error: El préstamo seleccionado no es válido");
        }
        
        Prestamo prestamo = this.prestamosRegistrados.get(indicePrestamo);
        prestamo.finalizarPrestamo();            
        Persona p = prestamo.getPersonaPrestamo();
        p.desasociarPrestamo(prestamo);            
        this.prestamosRegistrados.remove(indicePrestamo);
    }

    public void retornarItemDePrestamo(int indicePrestamo, int codigoItem) throws Exception {
        if (indicePrestamo < 0 || indicePrestamo >= this.prestamosRegistrados.size()) {
            throw new Exception("Error: El préstamo seleccionado no es válido");
        }
        
        Prestamo p = this.prestamosRegistrados.get(indicePrestamo);
        Item i = this.itemsRegistrados.get(codigoItem);
        if (i == null) {
            throw new Exception("Error: El ítem no existe");
        }
        
        if (p.getItemsPrestados().contains(i)) {                
            p.retornarItem(i);                
            if (p.estaFinalizado()) {
                this.finalizarPrestamo(indicePrestamo);
            }
        } else {
            throw new Exception("Error: El ítem no pertenece a este préstamo");
        }
    }

    public void agregarItemAPrestamo(int indicePrestamo, int codigoItem) throws Exception {
        if (indicePrestamo < 0 || indicePrestamo >= this.prestamosRegistrados.size()) {
            throw new Exception("Error: El préstamo seleccionado no es válido");
        }
        
        Prestamo p = this.prestamosRegistrados.get(indicePrestamo);
        Item i = this.itemsRegistrados.get(codigoItem);
        if (i == null || i.isEstaPrestado()) {
            throw new Exception("Error: El ítem no está disponible");
        }
        
        p.agregarItem(i);                
        i.vincularAPrestamo(p);
    }

    public void eliminarItemDePrestamo(int indicePrestamo, int codigoItem) throws Exception {
        this.retornarItemDePrestamo(indicePrestamo, codigoItem);
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
    
    
    public void agregarAlertaAPrestamo(int indicePrestamo, String tipoAlerta, int minutosAlerta, String mensajeAlerta) throws Exception {
        if (indicePrestamo < 0 || indicePrestamo >= this.prestamosRegistrados.size()) {
            throw new Exception("Error: Préstamo no válido");
        }
        Alerta nuevaAlerta = new Alerta(tipoAlerta, LocalDateTime.now().plusMinutes(minutosAlerta), mensajeAlerta, minutosAlerta);
        this.prestamosRegistrados.get(indicePrestamo).setRecordatorio(nuevaAlerta);
    }
    
    // MÉTODOS DE CONSULTAS

    public List<Prestamo> consultarPrestamosPorUsuario(String nombrePersona) throws Exception {
        Persona p = consultarPersona(nombrePersona);
        return p.getPrestamosRecibidos(); 
    }

    public List<Prestamo> consultarPrestamosPorItem(int codigoItem) throws Exception {
        Item itemBuscado = consultarItem(codigoItem);
        List<Prestamo> resultado = new ArrayList<Prestamo>();
        if (itemBuscado.isEstaPrestado() && itemBuscado.getRegistroPrestamo() != null) {
            resultado.add(itemBuscado.getRegistroPrestamo()); 
        }
        return resultado;
    }

    public List<Item> consultarItemsPorCategoria(String nombreCategoria) throws Exception {
        Categoria c = consultarCategoria(nombreCategoria);
        return c.getItemsAsociados(); 
    }

    public List<Item> consultarItemsPorTipo(String nombreTipo) throws Exception {
        Tipo t = consultarTipo(nombreTipo);
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

    public void registrarItem(String nombre, String descripcion, String nombreTipo, List<String> nombresCategorias) throws Exception {
        if (this.tiposRegistrados.size() <= 1) {
            throw new Exception("Debe registrar al menos un Tipo en el sistema antes de crear un item");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Error: El nombre del item es obligatorio.");
        }
        
        Tipo tipoFisico = this.tiposRegistrados.get(nombreTipo.toLowerCase());
        if (tipoFisico == null) {
            throw new Exception("Error: El tipo físico no existe");
        }
        
        Item nuevo = new Item(nombre, descripcion, tipoFisico);        
        if (nombresCategorias != null) {
            for (int j = 0; j < nombresCategorias.size(); j++) {
                Categoria cat = consultarCategoria(nombresCategorias.get(j));
                nuevo.agregarCategoria(cat);
            }
        }        
        this.itemsRegistrados.put(nuevo.getCodigo(), nuevo);        
        nuevo.vincularConTipoFisico();
        nuevo.vincularConCategorias();
    }

    public void modificarItem(int codigo, String nombre, String descripcion, String nombreTipoNuevo, List<String> nombresCategoriasNuevas) throws Exception {
        Item encontrado = consultarItem(codigo);
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("Error: El nombre no puede estar vacío");
        }
        
        Tipo tipoNuevo = this.tiposRegistrados.get(nombreTipoNuevo.toLowerCase());
        if (tipoNuevo == null) {
            throw new Exception("Error: El tipo físico especificado no existe");
        }        
        
        encontrado.desvincularDeTipo();
        encontrado.desvincularDeCategorias();            
        
        encontrado.setNombre(nombre);
        encontrado.setDescripcion(descripcion);
        encontrado.setTipoFisico(tipoNuevo);            
        encontrado.getCategorias().clear();
        
        if (nombresCategoriasNuevas != null) { 
            for (int k = 0; k < nombresCategoriasNuevas.size(); k++) {
                Categoria cat = consultarCategoria(nombresCategoriasNuevas.get(k));
                encontrado.agregarCategoria(cat);
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
    
    // CRUD CATEGORÍAS

    public List<Categoria> obtenerListadoCategorias() {
        ArrayList<Categoria> lista = new ArrayList<>(this.categoriasRegistradas.values());
        return lista;
    }

    public void registrarCategoria(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty() == true) {
            throw new Exception("Error: El nombre de la categoría no puede estar vacío");
        }
        
        String nombreC = nombre.toLowerCase();
        if (this.categoriasRegistradas.containsKey(nombreC) == true) {
            throw new Exception("Error: Ya existe una categoría registrada con ese nombre");
        }
        
        Categoria nueva = new Categoria(nombre);
        this.categoriasRegistradas.put(nombreC, nueva);
    }

    public void modificarCategoria(String nombreViejo, String nombreNuevo) throws Exception {
        Categoria encontrada = consultarCategoria(nombreViejo);
        if (nombreNuevo == null || nombreNuevo.trim().isEmpty() == true) {
            throw new Exception("Error: El nuevo nombre para la categoría no debe ser vacío");
        }
        String nombreC = nombreNuevo.toLowerCase();
        
        if (!nombreViejo.equalsIgnoreCase(nombreNuevo)) {
            if (this.categoriasRegistradas.containsKey(nombreC)) {
                throw new Exception("Error: Ya existe esa categoría");
            }
        }
        this.categoriasRegistradas.remove(nombreViejo.toLowerCase());        
        encontrada.setNombre(nombreNuevo);        
        this.categoriasRegistradas.put(nombreC, encontrada);
    }

    public void borrarCategoria(String nombre) throws Exception {
        Categoria encontrada = consultarCategoria(nombre);        
        encontrada.quitarCategoriaDeItems();        
        this.categoriasRegistradas.remove(nombre.toLowerCase());
    }
    
    public Categoria consultarCategoria(String nombre) throws Exception {
        Categoria encontrada = this.categoriasRegistradas.get(nombre.toLowerCase());
        if (encontrada == null) {
            throw new Exception("Error: Esa categoría no existe en el sistema");
        }
        return encontrada;
    }
    
    // CRUD TIPOS

    public List<Tipo> obtenerListadoTipos() {
        ArrayList<Tipo> lista = new ArrayList<>(this.tiposRegistrados.values());
        return lista;
    }

    public void registrarTipo(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty() == true) {
            throw new Exception("Error: El nombre del tipo físico no puede estar vacío.");
        }
        String nombreT = nombre.toLowerCase();
        if (this.tiposRegistrados.containsKey(nombreT) == true) {
            throw new Exception("Error: Ya existe ese tipo físico");
        }
        Tipo nuevo = new Tipo(nombre);
        this.tiposRegistrados.put(nombreT, nuevo);
    }

    public void modificarTipo(String nombreViejo, String nombreNuevo) throws Exception {
        Tipo encontrado = consultarTipo(nombreViejo);
        if (nombreNuevo == null || nombreNuevo.trim().isEmpty() == true) {
            throw new Exception("Error: El nuevo nombre para el tipo físico no es válido.");
        }        
        String nombreT = nombreNuevo.toLowerCase(); 
        
        if (!nombreViejo.equalsIgnoreCase(nombreNuevo)) {            
            if (this.tiposRegistrados.containsKey(nombreT)) {
                throw new Exception("Error: Ya existe ese tipo físico");
            }
        }        
        this.tiposRegistrados.remove(nombreViejo.toLowerCase());        
        encontrado.setNombre(nombreNuevo);        
        this.tiposRegistrados.put(nombreT, encontrado);
    }

    public void borrarTipo(String nombre) throws Exception {
        Tipo encontrado = consultarTipo(nombre);        
        Tipo tipoRespaldo = this.getTipoGenerico();        
        if (encontrado.equals(tipoRespaldo)) {
            throw new Exception("Error: El tipo 'Genérico' no se puede eliminar.");
        }       
        encontrado.reasignarItems(tipoRespaldo);        
        this.tiposRegistrados.remove(nombre.toLowerCase());
    }
    
    public Tipo consultarTipo(String nombre) throws Exception {
        Tipo encontrado = this.tiposRegistrados.get(nombre.toLowerCase());
        if (encontrado == null) {
            throw new Exception("Error: Ese tipo físico no existe en el sistema.");
        }
        return encontrado;
    }
    
    // MÉTODOS DE REPORTES

    public List<Persona> generarReporteUsuario() {
        ArrayList<Persona> lista = new ArrayList<>(this.personasRegistradas.values());
        this.ordenarUsuariosPorNombre(lista);
        return lista;
    }

    public List<Item> generarReporteItem() {
        ArrayList<Item> listaOrdenada = new ArrayList<>(this.itemsRegistrados.values());
        this.ordenarItemsPorNombre(listaOrdenada);
        return listaOrdenada;
    }

    public List<Categoria> generarReporteCategoria() {
        ArrayList<Categoria> lista = new ArrayList<>(this.categoriasRegistradas.values());
        this.ordenarCategoriasPorNombre(lista);
        return lista;
    }

    public List<Tipo> generarReporteTipo() {
        ArrayList<Tipo> lista = new ArrayList<>(this.tiposRegistrados.values());
        this.ordenarTiposPorNombre(lista);
        return lista;
    }

    // EXTRAS

    public Tipo getTipoGenerico() {
        return this.tiposRegistrados.get("generico");
    }
    
    public void ordenarItemsPorNombre(List<Item> lista) {
        int total = lista.size();
        for (int i = 0; i < total; i = i + 1) {
            for (int j = i + 1; j < total; j = j + 1) {
                Item itemA = lista.get(i);
                Item itemB = lista.get(j);                
                if (itemA.getNombre().compareToIgnoreCase(itemB.getNombre()) > 0) {
                    Item temporal = lista.get(i);
                    lista.set(i, lista.get(j));
                    lista.set(j, temporal);
                }
            }
        }
    }
    

    private void ordenarUsuariosPorNombre(List<Persona> lista) {
        int total = lista.size();
        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (lista.get(i).getNombreCompleto().compareToIgnoreCase(lista.get(j).getNombreCompleto()) > 0) {
                    Persona temporal = lista.get(i);
                    lista.set(i, lista.get(j));
                    lista.set(j, temporal);
                }
            }
        }
    }

    private void ordenarCategoriasPorNombre(List<Categoria> lista) {
        int total = lista.size();
        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (lista.get(i).getNombre().compareToIgnoreCase(lista.get(j).getNombre()) > 0) {
                    Categoria temporal = lista.get(i);
                    lista.set(i, lista.get(j));
                    lista.set(j, temporal);
                }
            }
        }
    }

    private void ordenarTiposPorNombre(List<Tipo> lista) {
        int total = lista.size();
        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (lista.get(i).getNombre().compareToIgnoreCase(lista.get(j).getNombre()) > 0) {
                    Tipo temporal = lista.get(i);
                    lista.set(i, lista.get(j));
                    lista.set(j, temporal);
                }
            }
        }
    }
    
    // G y S
    
    public List<Prestamo> getPrestamosRegistrados() {
        return this.prestamosRegistrados;
    }

    public Map<String, Persona> getPersonasRegistradas() {
        return this.personasRegistradas;
    }
    
    public Map<Integer, Item> getItemsRegistrados() {
        return this.itemsRegistrados;
    }
    
    public Map<String, Categoria> getCategoriasRegistradas() {
        return this.categoriasRegistradas;
    }

    public Map<String, Tipo> getTiposRegistrados() {
        return this.tiposRegistrados;
    }

    public Item consultarItem1(int codigo) throws Exception {
        if (!itemsRegistrados.containsKey(codigo)) {
            throw new Exception("El ítem no existe.");
        }
        return itemsRegistrados.get(codigo);
    }
    
    // PERSISTENCIA DE DATOS

    public void guardarDatos() {
        try {
            java.io.FileOutputStream file = new java.io.FileOutputStream("DatosPrestamos.dat");
            java.io.ObjectOutputStream stream = new java.io.ObjectOutputStream(file);                    
            stream.writeObject(instance);         
            stream.close();
            file.close();
            System.out.println("Datos del sistema guardados con éxito");
        } catch (java.io.IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public void cargarDatos() {
        try {
            java.io.FileInputStream file = new java.io.FileInputStream("DatosPrestamos.dat");
            java.io.ObjectInputStream stream = new java.io.ObjectInputStream(file);            
            instance = (Controladora) stream.readObject(); 
            stream.close();
            file.close();
            System.out.println("Datos del sistema cargados con éxito.");           
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Archivo de datos no encontrado. Generando entorno de pruebas...");
            generarDatosDePrueba();
        } catch (Exception e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void generarDatosDePrueba() {
        Persona p1 = new Persona("Luisa Meza", "8808-1711", "l.meza1@estudiantec.cr");
        Persona p2 = new Persona("Marcos Aguilar", "6888-2522", "m.aguilar2@estudiantec.cr");
        Persona p3 = new Persona("Geovanni González", "8788-2992", "geovanni.gonzalez@estudiantec.cr");
        Persona p4 = new Persona("Neyshell Blear", "7821-9101", "neyshell.blear@estudiantec.cr");
        Persona p5 = new Persona("Andrea Berlanga", "7011-9988", "andrea.b@estudiantec.cr");
        
        this.personasRegistradas.put(p1.getNombreCompleto(), p1);
        this.personasRegistradas.put(p2.getNombreCompleto(), p2);
        this.personasRegistradas.put(p3.getNombreCompleto(), p3);
        this.personasRegistradas.put(p4.getNombreCompleto(), p4);
        this.personasRegistradas.put(p5.getNombreCompleto(), p5);

        Categoria catFantasia = new Categoria("Fantasía");
        Categoria catEstrategia = new Categoria("Estrategia");
        Categoria catElectronicos = new Categoria("Electrónicos");
        Categoria catLiteratura = new Categoria("Literatura Clásica");
        Categoria catArte = new Categoria("Arte y Diseño");
        
        this.categoriasRegistradas.put("Fantasía", catFantasia);
        this.categoriasRegistradas.put("Estrategia", catEstrategia);
        this.categoriasRegistradas.put("Electrónicos", catElectronicos);
        this.categoriasRegistradas.put("Literatura Clásica", catLiteratura);
        this.categoriasRegistradas.put("Arte y Diseño", catArte);

        Tipo tipoLibro = new Tipo("Libro");
        Tipo tipoJuego = new Tipo("Juego de Mesa");
        Tipo tipoConsola = new Tipo("Consola de Videojuegos");
        Tipo tipoRevista = new Tipo("Revista Científica");
        Tipo tipoAccesorio = new Tipo("Accesorio Tecnológico");
        
        this.tiposRegistrados.put("Libro", tipoLibro);
        this.tiposRegistrados.put("Juego de Mesa", tipoJuego);
        this.tiposRegistrados.put("Consola de Videojuegos", tipoConsola);
        this.tiposRegistrados.put("Revista Científica", tipoRevista);
        this.tiposRegistrados.put("Accesorio Tecnológico", tipoAccesorio);

        Item item1 = new Item("Harry Potter y la Piedra Filosofal", "Edición ilustrada MinaLima", tipoLibro);
        item1.getCategorias().add(catFantasia);
        catFantasia.agregarItem(item1);
        item1.vincularConTipoFisico(); 
        this.itemsRegistrados.put(item1.getCodigo(), item1);

        Item item2 = new Item("Catan", "Juego base de tablero y comercio", tipoJuego);
        item2.getCategorias().add(catEstrategia);
        catEstrategia.agregarItem(item2);
        item2.vincularConTipoFisico();
        this.itemsRegistrados.put(item2.getCodigo(), item2);

        Item item3 = new Item("Nintendo Switch", "Consola portátil color neón", tipoConsola);
        item3.getCategorias().add(catElectronicos);
        catElectronicos.agregarItem(item3);
        item3.vincularConTipoFisico();
        this.itemsRegistrados.put(item3.getCodigo(), item3);

        Item item4 = new Item("Don Quijote de la Mancha", "Edición conmemorativa de la RAE", tipoLibro);
        item4.getCategorias().add(catLiteratura);
        catLiteratura.agregarItem(item4);
        item4.vincularConTipoFisico();
        this.itemsRegistrados.put(item4.getCodigo(), item4);

        Item item5 = new Item("Tableta Digitalizadora Wacom", "Modelo Intuos Pro para ilustración", tipoAccesorio);
        item5.getCategorias().add(catArte);
        catArte.agregarItem(item5);
        item5.vincularConTipoFisico();
        this.itemsRegistrados.put(item5.getCodigo(), item5);

        // Préstamo 1: Luisa tiene Harry Potter
        Alerta alerta1 = new Alerta("Única", LocalDateTime.now().plusMinutes(10), "Revisar si terminó de leer.", 0);
        List<Item> listaItems1 = new ArrayList<Item>();
        listaItems1.add(item1);
        Prestamo prestamo1 = new Prestamo(p1, listaItems1, alerta1);
        item1.vincularAPrestamo(prestamo1); 
        p1.asociarPrestamo(prestamo1);       
        this.prestamosRegistrados.add(prestamo1);

        // Préstamo 2: Marcos tiene Catan
        Alerta alerta2 = new Alerta("Recurrente", LocalDateTime.now().plusMinutes(30), "Verificar devolución en el grupo.", 30);
        List<Item> listaItems2 = new ArrayList<Item>();
        listaItems2.add(item2);
        Prestamo prestamo2 = new Prestamo(p2, listaItems2, alerta2);
        item2.vincularAPrestamo(prestamo2);
        p2.asociarPrestamo(prestamo2);
        this.prestamosRegistrados.add(prestamo2);

        // Préstamo 3: Geovanni tiene la Nintendo Switch
        Alerta alerta3 = new Alerta("Única", LocalDateTime.now().plusDays(2), "Confirmar estado de la batería al recibir.", 0);
        List<Item> listaItems3 = new ArrayList<Item>();
        listaItems3.add(item3);
        Prestamo prestamo3 = new Prestamo(p3, listaItems3, alerta3);
        item3.vincularAPrestamo(prestamo3);
        p3.asociarPrestamo(prestamo3);
        this.prestamosRegistrados.add(prestamo3);

        // Préstamo 4: Neyshell tiene Don Quijote
        Alerta alerta4 = new Alerta("Recurrente", LocalDateTime.now().plusDays(7), "Revisar avance de la lectura obligatoria.", 60);
        List<Item> listaItems4 = new ArrayList<Item>();
        listaItems4.add(item4);
        Prestamo prestamo4 = new Prestamo(p4, listaItems4, alerta4);
        item4.vincularAPrestamo(prestamo4);
        p4.asociarPrestamo(prestamo4);
        this.prestamosRegistrados.add(prestamo4);

        // Préstamo 5: Andrea tiene la tableta Wacom
        Alerta alerta5 = new Alerta("Única", LocalDateTime.now().plusHours(5), "Validar entrega del lápiz digital óptico.", 0);
        List<Item> listaItems5 = new ArrayList<Item>();
        listaItems5.add(item5);
        Prestamo prestamo5 = new Prestamo(p5, listaItems5, alerta5);
        item5.vincularAPrestamo(prestamo5);
        p5.asociarPrestamo(prestamo5);
        this.prestamosRegistrados.add(prestamo5);

        guardarDatos();
    }
}