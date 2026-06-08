import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private Table table;
    private List<Dish> orderedDishes = new ArrayList<>();
    private String status;

    public Order(int id, Table table) {
        this.id = id;
        this.table = table;
        this.status = "NEW";
        this.table.setStatus("OCCUPIED");
    }
    public void addDish(Dish dish) {
        orderedDishes.add(dish);
    }
    public void sendToKitchen() {
        this.status = "KITCHEN";
    }
    public double calculateTotal(int discountPercentage) {
        double sum = 0;
        for (Dish dish : orderedDishes) {
            sum = sum + dish.getPrice();
        }
        if (discountPercentage > 0) {
            sum = sum - (sum * discountPercentage / 100.0);
        }
        return sum;
    }
    public void pay() {
        this.status = "PAID";
        this.table.setStatus("FREE");
    }
    public int getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public List<Dish> getOrderedDishes() {
        return orderedDishes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void setOrderedDishes(List<Dish> orderedDishes) {
        this.orderedDishes = orderedDishes;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}