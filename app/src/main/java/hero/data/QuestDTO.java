package hero.data;

public class QuestDTO {

    String name;
    int badge;
    String detail;

    public QuestDTO(){

    }

    public QuestDTO(String name, int badge, String detail) {
        this.name = name;
        this.badge = badge;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
