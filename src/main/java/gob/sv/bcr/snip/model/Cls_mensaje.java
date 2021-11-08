package gob.sv.bcr.snip.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="cls_mensajes")
public class Cls_mensaje {

	@Id
	private String _id;
	private String dsc;
	private Integer state;
	private Date created_at;	
	private Date modified_at;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getDsc() {
		return dsc;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public void setDsc(String dsc) {
		this.dsc = dsc;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getModified_at() {
		return modified_at;
	}
	public void setModified_at(Date modified_at) {
		this.modified_at = modified_at;
	}
		
	public Cls_mensaje(String _id, String dsc, Integer state, Date created_at, Date modified_at) {
	
		this._id = _id;
		this.dsc = dsc;
		this.state = state;
		this.created_at = created_at;
		this.modified_at = modified_at;
	}
	public Cls_mensaje() {
		
	}
	
	
}
