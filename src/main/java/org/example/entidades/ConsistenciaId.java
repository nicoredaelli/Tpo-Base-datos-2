package org.example.entidades;



public class ConsistenciaId {
    private String entidad;
    private int seq;

    // Constructor
    public ConsistenciaId(String entidad, int seq) {
        this.entidad = entidad;
        this.seq = seq;
    }

    // Getters y Setters
    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
