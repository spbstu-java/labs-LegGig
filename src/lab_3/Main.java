package lab_3;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dictionaryPath = getDictionaryPath(args, scanner);

        try {
            Dictionary dictionary = Dictionary.loadFromFile(dictionaryPath);
            TextTranslator translator = new TextTranslator(dictionary);

            System.out.println("Словарь загружен. Записей: " +
                    dictionary.getEntries().size());
            runTranslationLoop(scanner, translator);

        } catch (InvalidFileFormatException | FileReadException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static String getDictionaryPath(String[] args, Scanner scanner) {
        if (args.length > 0) return args[0];

        if (FileUtils.isFileReadable("dictionary.txt")) {
            System.out.println("Используется словарь по умолчанию: dictionary.txt");
            return "dictionary.txt";
        }

        System.out.print("Введите путь к файлу словаря: ");
        return scanner.nextLine().trim();
    }

    private static void runTranslationLoop(Scanner scanner, TextTranslator translator) {
        System.out.println("Введите текст для перевода (или 'quit' для выхода):");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) break;

            System.out.println("Результат перевода:");
            System.out.println(translator.translate(input));
        }
    }
}