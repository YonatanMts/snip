package gob.sv.bcr.snip.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import gob.sv.bcr.snip.model.Cls_mensaje;

@Repository
public interface IMensajesDao extends MongoRepository<Cls_mensaje,String>  {

}
