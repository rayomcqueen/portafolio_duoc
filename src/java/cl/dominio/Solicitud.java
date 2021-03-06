/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.dominio;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author cristian
 */
public class Solicitud implements Serializable{
    private int idSolicitud;
    private Timestamp fechaSolicitud;
    private byte activa;
    private byte solicitudEspecial;
    private int diasPrestamo;

    public Solicitud(int idSolicitud, Timestamp fechaSolicitud, byte activa, byte solicitudEspecial, int diasPrestamo) {
        this.idSolicitud = idSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.activa = activa;
        this.solicitudEspecial = solicitudEspecial;
        this.diasPrestamo = diasPrestamo;
    }

    public Solicitud() {
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Timestamp getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Timestamp fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public byte getActiva() {
        return activa;
    }

    public void setActiva(byte activa) {
        this.activa = activa;
    }
    
    public byte getSolicitudEspecial() {
        return solicitudEspecial;
    }

    public void setSolicitudEspecial(byte solicitudEspecial) {
        this.solicitudEspecial = solicitudEspecial;
    }

    public int getDiasPrestamo() {
        return diasPrestamo;
    }

    public void setDiasPrestamo(int diasPrestamo) {
        this.diasPrestamo = diasPrestamo;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.idSolicitud;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Solicitud other = (Solicitud) obj;
        if (this.idSolicitud != other.idSolicitud) {
            return false;
        }
        return true;
    }

}
