import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Order order = null;
        Scanner sc = new Scanner(System.in);
        RestaurantStorage storage = RestaurantStorage.getInstance();
        storage.addIngredient("М'ясо", 2);
        storage.addIngredient("Томати", 1);
        storage.addIngredient("Рис", 1);
        Dish steak = new Dish("Головні страви", "Стейк", 350.0, "М'ясо");
        Dish salad = new Dish("Салати", "Салат Цезар", 150.0, "Томати");
        storage.addDishToMenu(steak);
        storage.addDishToMenu(salad);
        storage.registerTable(1);
        storage.registerTable(2);
        String filename = "restaurant_state.ser";
        System.out.println("~~~FRESH~~~");
        while (true) {
            System.out.println("\n1. Переглянути меню ресторану");
            System.out.println("2. Переглянути стан складу ");
            System.out.println("3. Зарезервувати столик №1");
            System.out.println("4. Сформувати замовлення для столика №1");
            System.out.println("5. Додати Стейк до замовлення");
            System.out.println("6. Додати Салат до замовлення");
            System.out.println("7. Передати замовлення на кухню ");
            System.out.println("8. Фінальний розрахунок клієнта (Дисконтна картка 10%)");
            System.out.println("9. Поповнити склад ");
            System.out.println("10. Показати журнал логів системи");
            System.out.println("11. Зберегти склад у файл (Memento)");
            System.out.println("12. Відновити склад з файлу (Memento)");
            System.out.println("0. Вихід");
            System.out.print("Ваш вибір: ");
            try {
            byte chose = sc.nextByte();
            sc.nextLine();
                if (chose == 1) {
                    storage.printMenu();
                }
                else if (chose == 2) {
                    storage.printskladStatus();
                }
                else if (chose == 3) {
                    storage.reserveTable(1);
                    System.out.println("Успішно зарезервовано!");
                }
                else if (chose == 4) {
                    order = storage.createOrder(1);
                    System.out.println("Замовлення №" + order.getId() + " відкрито.");
                }
                else if (chose == 5) {
                    if (order == null) {
                        System.out.println("Помилка: Спочатку відкрийте замовлення (Пункт 4)!");
                        continue;
                    }
                    order.addDish(steak);
                    System.out.println("Стейк додано до чеку.");
                }
                else if (chose == 6) {
                    if (order == null) {
                        System.out.println("Помилка: Спочатку відкрийте замовлення!");
                        continue;
                    }
                    order.addDish(salad);
                    System.out.println("Салат додано до чеку.");
                }
                else if (chose == 7) {
                    if (order == null) {
                        System.out.println("Немає активного замовлення.");
                        continue;
                    }
                    storage.verifyAndSendToKitchen(order);
                    System.out.println("Замовлення на кухні, продукты зарезервовано/списано.");
                }
                else if (chose == 8) {
                    if (order == null) {
                        System.out.println("Немає активного замовлення для розрахунку.");
                        continue;
                    }
                    int discount = 10;
                    double totalPrice = order.calculateTotal(discount);
                    order.pay();
                    storage.getLogger().addLog("Фінальний чек замовлення №" + order.getId() + " оплачено. Сума: " + totalPrice + " грн.");
                    System.out.println("Чек");
                    System.out.println("Замовлення №" + order.getId() + " оплачено.");
                    System.out.println("Сума до сплати з урахуванням дисконту " + discount + "%: " + totalPrice + " грн.");
                    System.out.println("Столик №1 тепер знову вільний!");
                    order = null;
                }
                else if (chose == 9) {
                    storage.addIngredient("Томати", 5);
                    System.out.println("Склад оновлено.");
                }
                else if (chose == 10) {
                    storage.getLogger().logs();
                }
                else if (chose == 11) {
                    storage.saveToFile(filename);
                }
                else if (chose == 12) {
                    storage.loadFromFile(filename);
                }
                else if (chose == 0) {
                    System.out.println("Програму завершено.");
                    break;
                }
            } catch (RestaurantException e) {
                System.out.println("\n Помилка: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Не вірно вказано значення");
                break;
            }
        }
    }
}