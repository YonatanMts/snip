package gob.sv.bcr.snip.model;

import java.util.Date;

public class LbtrNotificacion {
	
	private String descripcion;
	private Date fecha;
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public LbtrNotificacion(String descripcion, Date fecha) {
		this.descripcion = descripcion;
		this.fecha = fecha;
	}
	public LbtrNotificacion() {
		
	}	
	

}
