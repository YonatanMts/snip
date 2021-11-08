package gob.sv.bcr.snip.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gob.sv.bcr.snip.configuration.SnipConfiguration;
import gob.sv.bcr.snip.model.Cls_mensaje;
import gob.sv.bcr.snip.model.LbtrMensajeRespuesta;
import gob.sv.bcr.snip.model.LbtrNotificacion;
import gob.sv.bcr.snip.model.LbtrNotificacionResponse;
import gob.sv.bcr.snip.model.LbtrRequest;
import gob.sv.bcr.snip.model.LbtrResponse;
import gob.sv.bcr.snip.repository.IMensajesDao;

@RestController
@RequestMapping("snip")
public class SnipController {
	
	private static final Logger logger = LoggerFactory.getLogger(SnipController.class);
	
	@Autowired
	private IMensajesDao mensRepository;
	
	@Autowired
	private SnipConfiguration snipConfiguration;

	private static final String KEY = "lbtr.bcr";	
	
	@PostMapping("/newTopic/{topic}")
	public ResponseEntity<Object> crearTopicKafka(@PathVariable String topic) {

		Properties config = new Properties();
		config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		AdminClient admin = AdminClient.create(config);
		// creating new topic
		System.out.println("-- creando topic --");
		NewTopic newTopic = new NewTopic(topic, 1, (short) 1);		
		admin.createTopics(Collections.singleton(newTopic));
		LbtrMensajeRespuesta respuesta = new LbtrMensajeRespuesta();
		respuesta.setMensaje("Topic creado correctamente");
		respuesta.setDatos("Datos del participante");

		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping("/producer")
	public LbtrResponse post(@RequestBody LbtrRequest notificacionPeticion) throws InterruptedException, ExecutionException {

		LbtrResponse notificacionResponse = new LbtrResponse();

		if (notificacionPeticion.getDescripcionMensaje() == null || notificacionPeticion.getNombreUsuario() ==null || notificacionPeticion.getNombreTopic() ==null) {
			notificacionResponse.setDescripcion("Error de validacion, cumpruebe los datos ingresados");
			notificacionResponse.setEstado("01");
		} else {

			Cls_mensaje mensaje = new Cls_mensaje();
			mensaje.setDsc(notificacionPeticion.getDescripcionMensaje());
			mensaje.setCreated_at(new Date());
			mensaje.setState(1);
			mensaje.setModified_at(new Date());

			try {
				
				String Topic = notificacionPeticion.getNombreTopic().trim();
				Producer<String, LbtrRequest> producer = new KafkaProducer<String, LbtrRequest>(snipConfiguration.kafkaTemplate().getProducerFactory().getConfigurationProperties());
				ProducerRecord<String, LbtrRequest> record = new ProducerRecord<String, LbtrRequest>(Topic, 0, KEY,	notificacionPeticion);
				producer.send(record).get();
				producer.close();						
				mensRepository.insert(mensaje);
				notificacionResponse.setDescripcion("Mensaje insertado correctamente");
				notificacionResponse.setEstado("00");

			} catch (Exception e) {

				notificacionResponse.setDescripcion("Problemas al establecer la Conexion");
				notificacionResponse.setEstado("02");
			}

		}

		return notificacionResponse;
	}
	
	@GetMapping("/consumer/{topic}")
	public ResponseEntity<Object> lbtrConsumer(@PathVariable String topic) {

		if (topic == null) {

			LbtrNotificacionResponse lbtrNotificacionResponse = new LbtrNotificacionResponse();
			lbtrNotificacionResponse.setFechaGeneracion(new Date());
			lbtrNotificacionResponse.setNombreTopic(topic);
			lbtrNotificacionResponse.setResultado("ERROR DE VALIDACION: El topic especificado no es valido");

			return new ResponseEntity<Object>(lbtrNotificacionResponse, HttpStatus.OK);

		} else {

			LbtrNotificacionResponse lbtrNotificacionResponse = new LbtrNotificacionResponse();
			lbtrNotificacionResponse.setFechaGeneracion(new Date());
			lbtrNotificacionResponse.setNombreTopic(topic);

			lbtrNotificacionResponse.setResultado("OK: Lista de notificaciones para el topic especificado");
			lbtrNotificacionResponse.setNotificaciones(new ArrayList<LbtrNotificacion>());

			KafkaConsumer<String, LbtrRequest> consumer;
			consumer = new KafkaConsumer<String, LbtrRequest>(snipConfiguration.consumerFactory().getConfigurationProperties());
			consumer.subscribe(Collections.singleton(topic));

			logger.info("Consumer Acceso");

			Integer count = 0;
			try {
				while (true) {
					ConsumerRecords<String, LbtrRequest> records = consumer.poll(Duration.ofMillis(1000));
					System.out.println("consumer");

					for (ConsumerRecord<String, LbtrRequest> record : records) {
						System.out.println("entro al for");
						LbtrNotificacion lbtrNotificacion = new LbtrNotificacion();
						lbtrNotificacion.setDescripcion(record.value().getDescripcionMensaje());
						lbtrNotificacion.setFecha(record.value().getFechaMensaje());
						lbtrNotificacionResponse.getNotificaciones().add(lbtrNotificacion);
						System.out.println("Cantidad: " + count);
					}

					if (records == null || records.isEmpty()) {
						count++;
					}

					if (count > 2) {
						break;
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				consumer.close();
			}

			logger.info("Finaliza consumer");
			return new ResponseEntity<Object>(lbtrNotificacionResponse, HttpStatus.OK);

		}

	}

	@GetMapping("/consumer/all/{topic}")
	public ResponseEntity<Object> lbtrConsumerAll(@PathVariable String topic) {

		if (topic == null) {

			LbtrNotificacionResponse lbtrNotificacionResponse = new LbtrNotificacionResponse();
			lbtrNotificacionResponse.setFechaGeneracion(new Date());
			lbtrNotificacionResponse.setNombreTopic(topic);
			lbtrNotificacionResponse.setResultado("ERROR DE VALIDACION: El topic especificado no es valido");
			return new ResponseEntity<Object>(lbtrNotificacionResponse, HttpStatus.OK);

		} else {

			LbtrNotificacionResponse lbtrNotificacionResponse = new LbtrNotificacionResponse();
			lbtrNotificacionResponse.setFechaGeneracion(new Date());
			lbtrNotificacionResponse.setNombreTopic(topic);
			lbtrNotificacionResponse.setResultado("OK: Lista de notificaciones para el topic especificado");
			lbtrNotificacionResponse.setNotificaciones(new ArrayList<LbtrNotificacion>());

			KafkaConsumer<String, LbtrRequest> consumer;
			consumer = new KafkaConsumer<String, LbtrRequest>(snipConfiguration.consumerFactoryLastRecord().getConfigurationProperties());
			consumer.subscribe(Collections.singleton(topic));

			logger.info("Consumer Acceso");

			Integer count = 0;
			try {
				while (true) {
					ConsumerRecords<String, LbtrRequest> records = consumer.poll(Duration.ofMillis(1000));
					System.out.println("consumer");

					for (ConsumerRecord<String, LbtrRequest> record : records) {
						System.out.println("entro al for");
						LbtrNotificacion lbtrNotificacion = new LbtrNotificacion();
						lbtrNotificacion.setDescripcion(record.value().getDescripcionMensaje());
						lbtrNotificacion.setFecha(record.value().getFechaMensaje());
						lbtrNotificacionResponse.getNotificaciones().add(lbtrNotificacion);
						System.out.println("Cantidad: " + count);
					}

					if (records == null || records.isEmpty()) {
						count++;
					}

					if (count > 2) {
						break;
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				consumer.close();
			}

			logger.info("Finaliza consumer");			
			/*
			List<LbtrNotificacion> result = lbtrNotificacionResponse.getNotificaciones().stream()
				      .filter(notificacion -> notificacion.getDescripcion().length() > 10)				      
				      .collect(Collectors.toList());			
			System.out.println(result.size());
			*/
			
			return new ResponseEntity<Object>(lbtrNotificacionResponse, HttpStatus.OK);

		}

	}

}
