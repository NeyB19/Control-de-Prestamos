package logica;

import java.util.ArrayList;
import java.util.List;

public class Item {
 
	// Atributos
	private int codigo;
	private static int consecutivo = 1;
	private String nombre;
	private String descripcion;
	private boolean estaPrestado;
 
	private Tipo tipoFisico; 
	private List<Categoria> categorias;
	private Prestamo registroPrestamo;

	// Constructor
	public Item(String nombre, String descripcion, Tipo tipoFisico) {
		codigo = consecutivo++;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tipoFisico = tipoFisico;
		this.estaPrestado = false;
		this.categorias = new ArrayList<Categoria>();
		this.registroPrestamo = null;
	}

	 //Getters y Setters
	
	 public int getCodigo() {
	     return this.codigo;
	 }
	
	 public String getNombre() {
	     return this.nombre;
	 }
	
	 public void setNombre(String nombre) {
	     this.nombre = nombre;
	 }
	
	 public String getDescripcion() {
	     return this.descripcion;
	 }
	
	 public void setDescripcion(String descripcion) {
	     this.descripcion = descripcion;
	 }
	
	 public Tipo getTipoFisico() {
	     return this.tipoFisico;
	 }
	
	 public void setTipoFisico(Tipo tipoFisico) {
	     this.tipoFisico = tipoFisico;
	 }
	 
	 public List<Categoria> getCategorias() {
	     return this.categorias;
	 }
 
	 public boolean isEstaPrestado() {
	     return this.estaPrestado;
	 }
	 
	 public Prestamo getRegistroPrestamo() {
	     return registroPrestamo;
	 }
	
	 public void setRegistroPrestamo(Prestamo registroPrestamo) {
	     this.registroPrestamo = registroPrestamo;
	 }
	
	 // Métodos
	 
	 public void marcarComoPrestado() {
	     this.estaPrestado = true;
	 }
	
	 public void marcarComoDisponible() {
	     this.estaPrestado = false;
	 }
	
	  public void agregarCategoria(Categoria categoria) {
	     this.categorias.add(categoria);
	 }
	
	 public void eliminarDeCategoria(Categoria categoria) {
	     this.categorias.remove(categoria);
	 }
	
	// Extra
	 
	 public String toString() {
	     return "\nCódigo: " + this.codigo +
	            "\nNombre: " + this.nombre +
	            "\nDescripción: " + this.descripcion +
	            "\nEstado: " + (this.estaPrestado ? "Prestado" : "Disponible") + // (condición ? si_es_verdadero : si_es_falso)
	            "\n-------------------------";
	 }
}