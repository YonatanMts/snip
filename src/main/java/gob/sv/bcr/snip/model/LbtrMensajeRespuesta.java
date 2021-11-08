package gob.sv.bcr.snip.model;

public class LbtrMensajeRespuesta {
	
	private String Mensaje;
	private String Datos;
	public String getMensaje() {
		return Mensaje;
	}
	public void setMensaje(String mensaje) {
		Mensaje = mensaje;
	}
	public String getDatos() {
		return Datos;
	}
	public void setDatos(String datos) {
		Datos = datos;
	}
	public LbtrMensajeRespuesta(String mensaje, String datos) {
	
		Mensaje = mensaje;
		Datos = datos;
	}
	public LbtrMensajeRespuesta() {
		
	}
	
	

}
