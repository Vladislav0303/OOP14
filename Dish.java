import java.io.Serializable;

public class Dish implements MenuItem, Serializable {
    private String category;
    private String name;
    private double price;
    private String requiredIngredient;

    public Dish(String category, String name, double price, String requiredIngredient) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.requiredIngredient = requiredIngredient;
    }

    @Override public String getCategory() {
        return category;
    }
    @Override public String getName() {
        return name;
    }
    @Override public double getPrice() {
        return price;
    }
    public String getRequiredIngredient() {
        return requiredIngredient;
    }
}