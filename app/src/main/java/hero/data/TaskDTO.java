package hero.data;

import java.util.Calendar;

public class TaskDTO {

    long id;
    String name;
    String type;
    String detail;
    Calendar fromTime;
    Calendar toTime;
    String status;

    public TaskDTO(){

    }

    public TaskDTO(long id, String name, String type, String detail, Calendar fromTime, Calendar toTime, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.detail = detail;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Calendar getFromTime() {
        return fromTime;
    }

    public void setFromTime(Calendar fromTime) {
        this.fromTime = fromTime;
    }

    public Calendar getToTime() {
        return toTime;
    }

    public void setToTime(Calendar toTime) {
        this.toTime = toTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
