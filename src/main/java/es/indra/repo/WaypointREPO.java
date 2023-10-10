package es.indra.repo;

import es.indra.modelo.Waypoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaypointREPO extends JpaRepository<Waypoint, Integer>
{
}
