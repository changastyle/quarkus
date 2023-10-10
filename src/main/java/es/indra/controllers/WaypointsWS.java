package es.indra.controllers;

import es.indra.modelo.Waypoint;
import es.indra.repo.WaypointREPO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;
import java.util.*;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
@Path("/waypoints")
public class WaypointsWS
{

    @Inject
    WaypointREPO waypointREPO;
    private final static String TOPIC = "WAYPOINTS";

    @GET
    @Path("/randomWaypoints/")
    public List<Waypoint> randomWaypoints()
    {
        List<Waypoint> arrWaypoints = new ArrayList<>();

        for(int i = 0 ; i < 20 ; i++ )
        {
            long now = System.currentTimeMillis();
            int coordX = (int) (Math.random() * 300);
            int coordY = (int) (Math.random() * 300);
            Waypoint waypointGenerado = new Waypoint("i12341234",now,coordX , coordY );
            arrWaypoints.add(waypointGenerado);
//            WaypointQueue.sendWaypoint(i , waypoint);
        }

        return arrWaypoints;
    }
    @POST()
    @Path("/encolar")
    public boolean encolar(Waypoint waypoint)
    {
        boolean ok = false;
//        List<Waypoint> arrWaypoints = new ArrayList<>();

        waypoint.setTimestamp(System.currentTimeMillis());
        waypointREPO.save(waypoint);
//        String bootstrapServer = "localhost:9092";
//        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , bootstrapServer);
//        props.put(ProducerConfig.ACKS_CONFIG, "1");
////        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , StringSerializer.class.getName());
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG , StringSerializer.class.getName());
//        System.out.println("CONNECTED TO KAFKA [" + bootstrapServer + "] -> [" + TOPIC + "]: PROPS(" + props.size() +")");



//        try(KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);)
//        {
//            String strWaypoint = waypoint.toString();
//            ProducerRecord<String, String> record=new ProducerRecord<String, String>(TOPIC,String.valueOf(indice) ,strWaypoint );
////          ASYNC:
//            producer.send(record);
////          SYNC:
////            RecordMetadata recordMetadata = producer.send(record).get();
//            producer.flush();
//            producer.close();
//
//            System.out.println("ENVIADO: " + strWaypoint);
//            ok = true;
//        }
//        catch (KafkaException e)
//        {
//            e.printStackTrace();
//        }

        return ok;
    }
    @GET() @Path("/desencolar")
    public List<Waypoint> desencolar()
    {
        List<Waypoint> arrWaypoints = new ArrayList<>();

        System.out.println("DESENCOLANDO MENSAJES");

        String bootstrapServer = "localhost:9092";
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put("group.id", "grupo1");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
//        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");



        try(KafkaConsumer<String,String> consumer = new KafkaConsumer<>(props))
        {
            consumer.subscribe(Arrays.asList(TOPIC));

            System.out.println("CONSUMER CONNECTED TO KAFKA [" + bootstrapServer + "] -> [" + TOPIC + "]: PROPS(" + props.size() +")");
            int i =100;
            while(i < 100)
            {
                ConsumerRecords<String,String> arrMsg = consumer.poll(Duration.ofMillis(100));

                arrMsg.forEach(consumerLoop ->
                {
                    System.out.println(consumerLoop.offset() +") " + consumerLoop.key() + " = " + consumerLoop.value());
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        return arrWaypoints;
    }
}
