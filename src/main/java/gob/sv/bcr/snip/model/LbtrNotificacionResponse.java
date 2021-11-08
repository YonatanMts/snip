package gob.sv.bcr.snip.model;

import java.util.Date;
import java.util.List;

public class LbtrNotificacionResponse {
	
	private String nombreTopic;
	private Date fechaGeneracion;
	private String resultado;
	private List<LbtrNotificacion> notificaciones;
	public String getNombreTopic() {
		return nombreTopic;
	}
	public void setNombreTopic(String nombreTopic) {
		this.nombreTopic = nombreTopic;
	}
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	public List<LbtrNotificacion> getNotificaciones() {
		return notificaciones;
	}
	public void setNotificaciones(List<LbtrNotificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}	
	
	
	public LbtrNotificacionResponse(String nombreTopic, Date fechaGeneracion, String resultado,
			List<LbtrNotificacion> notificaciones) {
		
		this.nombreTopic = nombreTopic;
		this.fechaGeneracion = fechaGeneracion;
		this.resultado = resultado;
		this.notificaciones = notificaciones;
	}
	public LbtrNotificacionResponse() {
		
	}
	
}
