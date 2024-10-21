package id.ac.ui.cs.eaap.lab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "issue")
public class IssueModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_seq")
    @SequenceGenerator(name = "issue_seq", sequenceName = "issue_seq", allocationSize = 1, initialValue = 1000)
    @Column(name = "issue_id")
    private long issueId;
    @Column(name = "description")
    private String description;
    @Column(name = "reported_by")
    private String reportedBy;
    @Column(name = "reported_on")
    private Date reportedOn;
    @Column(name = "status")
    private String status;

    public long getUnresolvedDays() {
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(reportedOn.toLocalDate(), today);
    }

    // Relasi dengan RoomModel
    @ManyToOne
    @JoinColumn(name = "issue_room_id", referencedColumnName = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    RoomModel roomModel;
}
