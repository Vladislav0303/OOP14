import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantMemento implements Serializable {
    private final List<String> names = new ArrayList<>();
    private final List<Integer> counts = new ArrayList<>();

    public RestaurantMemento(List<Ingredient> sklad) {
        for (var ing : sklad) {
            this.names.add(ing.getName());
            this.counts.add(ing.getcount());
        }
    }
    public List<Ingredient> getSavedSklad() {
        List<Ingredient> restoredSklad = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            restoredSklad.add(new Ingredient(names.get(i), counts.get(i)));
        }
        return restoredSklad;
    }
}