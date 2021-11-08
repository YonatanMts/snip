package gob.sv.bcr.snip.model;

import java.util.Date;

public class LbtrRequest {
	
	
	private String descripcionMensaje;	
	private String nombreUsuario;	
	private String nombreTopic;	
	private Date fechaMensaje;
	
	public String getDescripcionMensaje() {
		return descripcionMensaje;
	}
	public void setDescripcionMensaje(String descripcionMensaje) {
		this.descripcionMensaje = descripcionMensaje;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getNombreTopic() {
		return nombreTopic;
	}
	public void setNombreTopic(String nombreTopic) {
		this.nombreTopic = nombreTopic;
	}
	public Date getFechaMensaje() {
		return fechaMensaje;
	}
	public void setFechaMensaje(Date fechaMensaje) {
		this.fechaMensaje = fechaMensaje;
	}
	public LbtrRequest(String descripcionMensaje, String nombreUsuario, String nombreTopic, Date fechaMensaje) {
		this.descripcionMensaje = descripcionMensaje;
		this.nombreUsuario = nombreUsuario;
		this.nombreTopic = nombreTopic;
		this.fechaMensaje = fechaMensaje;
	}
	public LbtrRequest() {
		super();
	}		

}
