package dtos;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeTableDTO implements Serializable {
    private int id;

    private String day;

    private LocalTime start;

    private LocalTime end;

    private int sportCode;

    private String sportName;

    public TimeTableDTO() {
    }

    public TimeTableDTO(int id) {
        this.id = id;
    }

    public TimeTableDTO(int id, String day, LocalTime start, LocalTime end, int sportCode, String sportName) {
        this.id = id;
        this.day = day;
        this.start = start;
        this.end = end;
        this.sportCode = sportCode;
        this.sportName = sportName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
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

    public int getSportCode() {
        return sportCode;
    }

    public void setSportCode(int sportCode) {
        this.sportCode = sportCode;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}
