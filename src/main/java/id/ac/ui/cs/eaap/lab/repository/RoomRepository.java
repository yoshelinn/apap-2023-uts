package main.java.id.ac.ui.cs.eaap.lab.repository;

import id.ac.ui.cs.eaap.lab.model.RoomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RoomRepository extends JpaRepository<RoomModel, Long> {
}
