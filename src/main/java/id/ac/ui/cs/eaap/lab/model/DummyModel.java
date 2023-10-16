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
@Table(name = "dummy")
public class DummyModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dummy_seq")
    @SequenceGenerator(name = "dummy_seq", sequenceName = "dummy_seq", allocationSize = 1, initialValue = 1000)
    @Column(name = "id")
    private long id;
    @Column(name = "description")
    private String description;
}
