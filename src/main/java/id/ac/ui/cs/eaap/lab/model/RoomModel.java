package id.ac.ui.cs.eaap.lab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room")
public class RoomModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @SequenceGenerator(name = "room_seq", sequenceName = "room_seq", allocationSize = 100, initialValue = 1000)
    @Column(name = "room_id")
    private long roomId;
    @Column(name = "room_number")
    private String roomNumber;
    @Column(name = "room_name")
    private String roomName;
    @Column(name = "building_name")
    private String buildingName;
    @Column(name = "faculty")
    private String faculty;
}
