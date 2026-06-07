import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PR {

    static final int MAX = 50;

    static LocalDate[] dates = new LocalDate[MAX];
    static String[] notes = new String[MAX];
    static int count = 0;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== МІЙ ЩОДЕННИК =====");
        System.out.println("1. Новий щоденник");
        System.out.println("2. Завантажити з файлу");
        System.out.print("Ваш вибір: ");

        String choice = sc.nextLine();

        if (choice.equals("2")) {
            loadFromFile(sc);
        }

        while (true) {

            System.out.println("\n===== МЕНЮ =====");
            System.out.println("1. Додати запис");
            System.out.println("2. Видалити запис");
            System.out.println("3. Переглянути записи");
            System.out.println("4. Вихід");
            System.out.print("Ваш вибір: ");

            String c = sc.nextLine();

            if (c.equals("1")) {
                add(sc);
            } else if (c.equals("2")) {
                delete(sc);
            } else if (c.equals("3")) {
                show();
            } else if (c.equals("4")) {
                exit(sc);
                break;
            } else {
                System.out.println("Невірний вибір");
            }
        }
    }

    static void add(Scanner sc) {

        if (count >= MAX) {
            System.out.println("Щоденник заповнений");
            return;
        }

        try {

            System.out.print("Введіть дату (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(sc.nextLine(), formatter);

            System.out.println("Введіть текст (END для завершення):");

            String text = "";
            String line;

            while (true) {
                line = sc.nextLine();
                if (line.equals("END")) break;
                text = text + line + "\n";
            }

            dates[count] = date;
            notes[count] = text;
            count++;

            System.out.println("Запис додано");

        } catch (DateTimeParseException e) {
            System.out.println("Помилка формату дати");
        }
    }

    static void delete(Scanner sc) {

        try {

            System.out.print("Дата для видалення: ");
            LocalDate d = LocalDate.parse(sc.nextLine(), formatter);

            int idx = -1;

            for (int i = 0; i < count; i++) {
                if (dates[i].equals(d)) {
                    idx = i;
                    break;
                }
            }

            if (idx == -1) {
                System.out.println("Не знайдено");
                return;
            }

            for (int i = idx; i < count - 1; i++) {
                dates[i] = dates[i + 1];
                notes[i] = notes[i + 1];
            }

            count--;

            System.out.println("Видалено");

        } catch (DateTimeParseException e) {
            System.out.println("Невірна дата");
        }
    }

    static void show() {

        if (count == 0) {
            System.out.println("Порожньо");
            return;
        }

        for (int i = 0; i < count; i++) {
            System.out.println("----------------");
            System.out.println("Дата: " + dates[i].format(formatter));
            System.out.println(notes[i]);
        }
    }

    static void exit(Scanner sc) {

        System.out.print("Зберегти файл? (y/n): ");
        String ans = sc.nextLine();

        if (ans.equals("y")) {

            try {

                System.out.print("Введіть шлях файлу: ");
                String path = sc.nextLine();

                BufferedWriter bw = new BufferedWriter(new FileWriter(path));

                for (int i = 0; i < count; i++) {
                    bw.write(dates[i].format(formatter));
                    bw.newLine();
                    bw.write(notes[i]);
                    bw.newLine();
                    bw.write("---");
                    bw.newLine();
                }

                bw.close();

                System.out.println("Збережено");

            } catch (IOException e) {
                System.out.println("Помилка збереження");
            }
        }
    }

    static void loadFromFile(Scanner sc) {

        try {

            System.out.print("Шлях до файлу: ");
            String path = sc.nextLine();

            BufferedReader br = new BufferedReader(new FileReader(path));

            String line;
            String dateStr = "";
            String text = "";

            while ((line = br.readLine()) != null) {

                if (dateStr.equals("")) {
                    dateStr = line;

                } else if (line.equals("---")) {

                    dates[count] = LocalDate.parse(dateStr, formatter);
                    notes[count] = text;
                    count++;

                    dateStr = "";
                    text = "";

                } else {
                    text = text + line + "\n";
                }
            }

            br.close();

            System.out.println("Завантажено");

        } catch (IOException e) {
            System.out.println("Помилка файлу");

        } catch (DateTimeParseException e) {
            System.out.println("Помилка формату даних у файлі");
        }
    }
}