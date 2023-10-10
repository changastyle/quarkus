package es.indra;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class WaypointsAPI
{
    public static void main(String ... args) {
        System.out.println("STARTING QUARKUS :  http://localhost:8080/docs");
        Quarkus.run(args);

    }
}
