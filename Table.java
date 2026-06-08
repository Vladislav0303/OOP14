import java.io.Serializable;

public class Table implements Serializable {
    private int number;
    private String status;

    public Table(int number) {
        this.number = number;
        this.status = "FREE";
    }
    public int getNumber() {
        return number;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
