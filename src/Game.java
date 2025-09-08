import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;

class MoveRecord {
    private String from;
    private String to;
    private String method;
    private int order;

    public MoveRecord(String from, String to, String method, int order) {
        this.from = from;
        this.to = to;
        this.method = method;
        this.order = order;
    }

    @Override
    public String toString() {
        return order + ". Из '" + from + "' в '" + to + "' (" + method + ")";
    }
}

interface MovementStrategy {
    void move(String from, String to);
    String getDescription();
}

class WalkingStrategy implements MovementStrategy {
    public void move(String from, String to) {
        System.out.println("Иду пешком из " + from + " в " + to);
    }

    public String getDescription() {
        return "Пешком";
    }
}

class HorseRidingStrategy implements MovementStrategy {
    public void move(String from, String to) {
        System.out.println("Скачу на лошади из " + from + " в " + to);
    }

    public String getDescription() {
        return "На лошади";
    }
}

class CarStrategy implements MovementStrategy {
    public void move(String from, String to) {
        System.out.println("Еду на машине из " + from + " в " + to);
    }

    public String getDescription() {
        return "На машине";
    }
}

class FlyingStrategy implements MovementStrategy {
    public void move(String from, String to) {
        System.out.println("Лечу по воздуху из " + from + " в " + to);
    }

    public String getDescription() {
        return "Полёт";
    }
}

class TeleportStrategy implements MovementStrategy {
    public void move(String from, String to) {
        System.out.println("Телепортируюсь из " + from + " в " + to);
    }

    public String getDescription() {
        return "Телепортация";
    }
}

class Hero {
    private MovementStrategy movementStrategy;
    private String currentPosition;
    private List<MoveRecord> moveHistory;
    private int moveCounter;

    public Hero() {
        this.currentPosition = "Начальная точка";
        this.moveHistory = new ArrayList<>();
        this.moveCounter = 0;
    }

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
        System.out.println("Выбран способ перемещения: " + movementStrategy.getDescription());
    }

    public void move(String to) {
        move(currentPosition, to);
    }

    public void move(String from, String to) {
        if (movementStrategy == null) {
            System.out.println("Сначала выберите способ перемещения!");
            return;
        }

        movementStrategy.move(from, to);

        moveCounter++;
        moveHistory.add(new MoveRecord(from, to, movementStrategy.getDescription(), moveCounter));

        currentPosition = to;
        System.out.println("Теперь герой находится в: " + currentPosition);
    }

    public MovementStrategy getCurrentStrategy() {
        return movementStrategy;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setPosition(String position) {
        moveCounter++;
        moveHistory.add(new MoveRecord(currentPosition, position, "Принудительная установка", moveCounter));

        this.currentPosition = position;
        System.out.println("Герой перемещен в: " + position);
    }

    public void printMoveHistory() {
        if (moveHistory.isEmpty()) {
            System.out.println("Герой еще никуда не перемещался!");
            return;
        }

        System.out.println("\n=== ИСТОРИЯ ПЕРЕМЕЩЕНИЙ ГЕРОЯ ===");
        for (MoveRecord record : moveHistory) {
            System.out.println(record);
        }
        System.out.println("Итого перемещений: " + moveCounter);
        System.out.println("Текущее положение: " + currentPosition);
    }
}

public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hero hero = new Hero();

        MovementStrategy[] strategies = {
                new WalkingStrategy(),
                new HorseRidingStrategy(),
                new FlyingStrategy(),
                new TeleportStrategy(),
                new CarStrategy()
        };

        while (true) {
            System.out.println("\n=== МЕНЮ ВЫБОРА СПОСОБА ПЕРЕМЕЩЕНИЯ ===");
            System.out.println("Текущая позиция героя: " + hero.getCurrentPosition());
            System.out.println("1. Выбрать способ перемещения");
            System.out.println("2. Переместиться в указанную точку");
            System.out.println("3. Переместиться из текущей точки в другую");
            System.out.println("4. Установить текущую позицию");
            System.out.println("5. Текущий способ перемещения");
            System.out.println("6. Показать историю перемещений");
            System.out.println("7. Выйти");
            System.out.print("Выберите действие: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число от 1 до 7!");
                scanner.next();
                continue;
            }
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nДоступные способы перемещения:");
                    for (int i = 0; i < strategies.length; i++) {
                        System.out.println((i + 1) + ". " + strategies[i].getDescription());
                    }
                    System.out.print("Выберите способ (1-" + strategies.length + "): ");

                    int strategyChoice = 0;
                    try {
                        strategyChoice = scanner.nextInt() - 1;
                    } catch (InputMismatchException e) {
                        System.out.println("Ошибка: введите число от 1 до " + strategies.length + "!");
                        scanner.next();
                        break;
                    }
                    scanner.nextLine();

                    if (strategyChoice >= 0 && strategyChoice < strategies.length) {
                        hero.setMovementStrategy(strategies[strategyChoice]);
                    } else {
                        System.out.println("Неверный выбор!");
                    }
                    break;

                case 2:
                    if (hero.getCurrentStrategy() != null) {
                        System.out.print("Введите конечную точку: ");
                        String to = scanner.nextLine();
                        hero.move(to);
                    } else {
                        System.out.println("Сначала выберите способ перемещения!");
                    }
                    break;

                case 3:
                    if (hero.getCurrentStrategy() != null) {
                        System.out.print("Введите начальную точку: ");
                        String from = scanner.nextLine();
                        System.out.print("Введите конечную точку: ");
                        String to = scanner.nextLine();
                        hero.move(from, to);
                    } else {
                        System.out.println("Сначала выберите способ перемещения!");
                    }
                    break;

                case 4:
                    System.out.print("Введите новую позицию героя: ");
                    String position = scanner.nextLine();
                    hero.setPosition(position);
                    break;

                case 5:
                    MovementStrategy current = hero.getCurrentStrategy();
                    if (current != null) {
                        System.out.println("Текущий способ перемещения: " + current.getDescription());
                    } else {
                        System.out.println("Способ перемещения не выбран");
                    }
                    break;

                case 6:
                    hero.printMoveHistory();
                    break;

                case 7:
                    System.out.println("\n=== ИТОГИ ПУТЕШЕСТВИЯ ГЕРОЯ ===");
                    hero.printMoveHistory();
                    System.out.println("Выход из программы...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор! Введите число от 1 до 7.");
            }
        }
    }
}