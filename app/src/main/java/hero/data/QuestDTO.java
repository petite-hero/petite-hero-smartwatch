package hero.data;

public class QuestDTO {

    String name;
    String badgeName;
    String detail;
    String color;

    public QuestDTO(){

    }

    public QuestDTO(String name, String badgeName, String detail, String color) {
        this.name = name;
        this.badgeName = badgeName;
        this.detail = detail;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
