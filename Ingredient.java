import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private int count;

    public Ingredient(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }
    public int getcount() {
        return count;
    }
    public void setcount(int count) {
        this.count = count;
    }
}