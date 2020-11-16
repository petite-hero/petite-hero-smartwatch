package hero.data;

public class QuestDTO {

    long id;
    String name;
    int badge;
    String detail;
    String title;
    String status;

    public QuestDTO(){

    }

    public QuestDTO(long id, String name, int badge, String detail, String title, String status) {
        this.id = id;
        this.name = name;
        this.badge = badge;
        this.detail = detail;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
