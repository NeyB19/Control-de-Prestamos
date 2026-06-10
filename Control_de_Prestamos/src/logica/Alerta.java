package logica;

import java.time.LocalDateTime;

public class Alerta {

    // Atributos
    private String tipoAlerta;             
    private LocalDateTime tiempoActivacion; // Próxima hora en la que debe activarse
    private String mensaje;                 
    private boolean enviada;                
    private int minutosParaRepetir;         // Cada cuantos minutos se repite 

    // Constructor 
    public Alerta(String tipoAlerta, LocalDateTime tiempoActivacion, String mensaje, int minutosParaRepetir) {
        this.tipoAlerta = tipoAlerta;
        this.tiempoActivacion = tiempoActivacion;
        this.mensaje = mensaje;
        this.minutosParaRepetir = minutosParaRepetir;
        this.enviada = false;
    }

    // Getters y Setters

    public String getTipoAlerta() {
        return this.tipoAlerta;
    }

    public void setTipoAlerta(String tipo) {
        this.tipoAlerta = tipo;
    }

    public LocalDateTime getTiempoActivacion() {
        return this.tiempoActivacion;
    }

    public void setTiempoActivacion(LocalDateTime tiempo) {
        this.tiempoActivacion = tiempo;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean getEnviada() {
        return this.enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }

    public int getMinutosParaRepetir() {
        return this.minutosParaRepetir;
    }

    public void setMinutosParaRepetir(int minutos) {
        this.minutosParaRepetir = minutos;
    }

    // Métodos

     // Le suma a la fecha de activación la cantidad de minutos.
   
    public void programarSiguienteRepeticion() {
        if (this.tipoAlerta.equals("Recurrente") == true) {
            long minutosASumar = this.minutosParaRepetir;
            LocalDateTime nuevaFechaFutura = this.tiempoActivacion.plusMinutes(minutosASumar);
            this.tiempoActivacion = nuevaFechaFutura;
            this.enviada = false;
        }
    }

    // Extra

    public String toString() {
        return "\nTipo de Alerta: " + this.tipoAlerta +
               "\nMensaje: " + this.mensaje +
               "\nHora de Activación: " + this.tiempoActivacion +
               "\n¿Ya se envió?: " + (this.enviada ? "Sí" : "No") +
               "\n-------------------------";
    }
}