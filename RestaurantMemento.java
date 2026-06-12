import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantMemento implements Serializable {
    private List<String> names = new ArrayList<>();
    private List<Integer> counts = new ArrayList<>();

    public RestaurantMemento(List<Ingredient> sklad) {
        for (var ing : sklad) {
            this.names.add(ing.getName());
            this.counts.add(ing.getCount());
        }
    }
    public List<Ingredient> getSavedSklad() {
        List<Ingredient> restoredSklad = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            restoredSklad.add(new Ingredient(names.get(i), counts.get(i)));
        }
        return restoredSklad;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Integer> getCounts() {
        return counts;
    }

    public void setCounts(List<Integer> counts) {
        this.counts = counts;
    }
}