package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "TIMETABLES")
@NamedQueries({
        @NamedQuery(
                name = "TimeTable.getAll",
                query = "SELECT t FROM TimeTable t ORDER BY t.sport.name"
        ),
        @NamedQuery(
                name = "TimeTable.getByIds",
                query = "SELECT t FROM TimeTable t WHERE t.id IN :ids ORDER BY t.sport.name"
        )
})
public class TimeTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private DayOfWeek day;

    @NotNull
    private LocalTime start;

    @NotNull
    private LocalTime end;

    @ManyToOne
    @JoinColumn(name = "SPORT_CODE", nullable = false)
    private Sport sport;

    public TimeTable() {
    }

    public TimeTable(DayOfWeek day, LocalTime start, LocalTime end, Sport sport) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.sport = sport;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Transient
    public String getDay() {
        //return DayOfWeek.of(day.getValue()).toString();
        //return new DateFormatSymbols().getWeekdays()[day.getValue()];
        return day.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public String getDuration () {
        long hours = Duration.between(start, end).toHours();
        long minutes = Duration.between(start, end).toMinutes() - (hours * 60);
        return hours + ":" + minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTable)) return false;
        TimeTable timeTable = (TimeTable) o;
        return id == timeTable.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
