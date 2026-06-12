import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantStorage implements Serializable{
    private List<Ingredient> sklad = new ArrayList<>();
    private List<Dish> menu = new ArrayList<>();
    private List<Table> tables = new ArrayList<>();
    private int orderIdCounter = 1;
    private List<OrderObserver> observers = new ArrayList<>();
    private Logger logger = new Logger();
    private static RestaurantStorage instance;

    private RestaurantStorage() {
        addObserver(logger);
    }
    public static RestaurantStorage getInstance() {
        if(instance == null) {
            instance = new RestaurantStorage();
        }
        return instance;
    }
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }
    private void notifyObservers(String message) {
        for (var observer : observers) {
            observer.onOrderEvent(message);
        }
    }
    public RestaurantMemento saveState() {
        notifyObservers("Створено зліпок стану складу (Memento)");
        return new RestaurantMemento(sklad);
    }
    public void restoreState(RestaurantMemento memento) {
        this.sklad = memento.getSavedSklad();
        notifyObservers("Стан складу успішно відновлено з Memento");
    }
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            RestaurantMemento memento = saveState();
            oos.writeObject(memento);
            System.out.println("Стан успішно серіалізовано у файл: " + filename);
        } catch (IOException e) {
            System.out.println("Помилка серіалізації: " + e.getMessage());
        }
    }
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            RestaurantMemento memento = (RestaurantMemento) ois.readObject();
            restoreState(memento);
            System.out.println("\n=== ВІДНОВЛЕНІ ІНГРЕДІЄНТИ ===");
            for (int i = 0; i < memento.getNames().size(); i++) {
                System.out.println(" #" + (i + 1) + " " + memento.getNames().get(i) + ": " + memento.getCounts().get(i));
            }
            System.out.println("Стан десеріалізовано з файлу.");

        } catch (Exception e) {
            System.out.println("Не вдалося завантажити збереження: " + e.getMessage());
        }
    }
    public void addIngredient(String name, int count) {
        for (var ing : sklad) {
            if (ing.getName().equals(name)) {
                ing.setCount(ing.getCount() + count);
                notifyObservers("Поповнено склад: " + name + " на " + count + " шт.");
                return;
            }
        }
        sklad.add(new Ingredient(name, count));
        notifyObservers("Новий інгредієнт на складі: " + name + " (" + count + " шт.)");
    }
    public void printskladStatus() {
        System.out.println("Стан складу");
        for (var ing : sklad) {
            if (ing.getCount() > 0) {
                System.out.println("- " + ing.getName() + ": В наявності (" + ing.getCount() + " шт.)");
            } else {
                System.out.println("- " + ing.getName() + ": Відсутній!");
            }
        }
    }
    public void addDishToMenu(Dish dish) {
        menu.add(dish);
    }
    public void printMenu() {
        System.out.println("Меню");
        for (var dish : menu) {
            System.out.println("[" + dish.getCategory() + "] " + dish.getName() + " - " + dish.getPrice() + " грн.");
        }
    }
    public void registerTable(int number) {
        tables.add(new Table(number));
    }
    public void reserveTable(int number) {
        for (var table : tables) {
            if (table.getNumber() == number) {
                if (!table.getStatus().equals("FREE")) {
                    throw new RestaurantException("Столик №" + number + " вже зайнятий або зарезервований!");
                }
                table.setStatus("RESERVED");
                notifyObservers("Столик №" + number + " зарезервовано.");
                return;
            }
        }
        throw new RestaurantException("Столика №" + number + " не існує.");
    }
    public Order createOrder(int tableNumber) {
        for (var table : tables) {
            if (table.getNumber() == tableNumber) {
                Order order = new Order(orderIdCounter++, table);
                notifyObservers("Офіціант сформував замовлення №" + order.getId() + " для стола №" + tableNumber);
                return order;
            }
        }
        throw new RestaurantException("Не вдалося створити замовлення. Стіл не знайдено.");
    }
    public void verifyAndSendToKitchen(Order order) {
        for (var dish : order.getOrderedDishes()) {
            String required = dish.getRequiredIngredient();
            boolean found = false;
            for (var ing : sklad) {
                if (ing.getName().equals(required)) {
                    found = true;
                    if (ing.getCount() < 1) {
                        notifyObservers("КРИТИЧНО: Закінчився інгредієнт: " + required);
                        throw new RestaurantException("Немає інгредієнта [" + required + "] для страви " + dish.getName());
                    }
                }
            }
            if (!found) {
                throw new RestaurantException("Інгредієнт " + required + " відсутній на складі!");
            }
        }
        for (var dish : order.getOrderedDishes()) {
            for (var ing : sklad) {
                if (ing.getName().equals(dish.getRequiredIngredient())) {
                    ing.setCount(ing.getCount() - 1);
                }
            }
        }
        order.sendToKitchen();
        notifyObservers("Замовлення №" + order.getId() + " успішно перевірено та передано на кухню.");
    }

    public List<Ingredient> getSklad() {
        return sklad;
    }

    public void setSklad(List<Ingredient> sklad) {
        this.sklad = sklad;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public int getOrderIdCounter() {
        return orderIdCounter;
    }

    public void setOrderIdCounter(int orderIdCounter) {
        this.orderIdCounter = orderIdCounter;
    }

    public List<OrderObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<OrderObserver> observers) {
        this.observers = observers;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public static void setInstance(RestaurantStorage instance) {
        RestaurantStorage.instance = instance;
    }
}