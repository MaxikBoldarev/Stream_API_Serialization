import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        GameProgress[] gp = new GameProgress[]{
                new GameProgress(100, 5, 3, 3.5),
                new GameProgress(90, 4, 6, 3.2),
                new GameProgress(80, 2, 8, 2.2)
        };

        File file = new File("/Users/admin/Documents/");

        int input = 0;
        StringBuilder sb = new StringBuilder();

        while (input != 11) {
            System.out.println("""
                    Работа с директорий!
                    Доступны следующие функции:
                    1. Указать рабочую директорию
                    2. Создать каталог (Обязательно выполните пункт 1)
                    3. Создать файл (Обязательно выполните пункт 1)
                    4. Показать рабочую директорию
                    5. Сохранить данные в файл (Обязательно выполните пункт 1)
                    6. Архивировать файлы из директории (Обязательно выполните пункт 1)
                    7. Распаковать файл из директории (Обязательно выполните пункт 1)
                    8. Десериализовать файл из директории (Обязательно выполните пункт 1)
                    9. Удалить файлы из директории (Обязательно выполните пункт 1)
                    10. Показать лог
                    11. Завершить работу и запись в лог"""
            );

            Scanner sc = new Scanner(System.in);
            String inputCase = sc.nextLine();
            input = Integer.parseInt(inputCase);

            switch (input) {
                case 1 -> {
                    System.out.println("Введите полный адрес директории (Пример:\"/Users/admin/Documents/Games\")");
                    Scanner scannerCaseOne = new Scanner(System.in);
                    String inputCaseOne = scannerCaseOne.nextLine();
                    File f = new File(inputCaseOne);

                    if (f.isDirectory()) {
                        file = new File(inputCaseOne);
                        System.out.println("It is directory");
                    } else {
                        System.out.println("It is not directory");
                    }
                    sb.append("Изменена рабочая директория на:")
                            .append(file)
                            .append("\n");
                }
                case 2 -> {
                    System.out.println("Ведите название каталога без '/' (Вы должны находиться рабочей директории)");
                    Scanner scannerCaseTwo = new Scanner(System.in);
                    String inputCaseTwo = scannerCaseTwo.nextLine();

                    String forSbTwo = createNewDir(inputCaseTwo, file);
                    System.out.println(forSbTwo);
                    sb.append(forSbTwo)
                            .append("\n");
                }
                case 3 -> {
                    System.out.println("Ведите имя файла с разрешением '.txt' например (Вы должны находиться рабочей директории)");
                    Scanner scannerCaseThree = new Scanner(System.in);
                    String inputCaseThree = scannerCaseThree.nextLine();

                    String forSbThree = createNewFile(inputCaseThree, file);
                    System.out.println(forSbThree);
                    sb.append(forSbThree)
                            .append("\n");
                }
                case 4 -> {
                    System.out.println("Рабочая директория: " + file);
                    sb.append("Просмотр рабочей директории: ")
                            .append(file)
                            .append("\n");
                }
                case 5 -> {
                    saveGame(gp, file);
                    String forSave = "Данные сохранены";
                    System.out.println(forSave);
                    sb.append(forSave)
                            .append("\n");
                }
                case 6 -> {
                    String forZip = zipFiles(file);
                    System.out.println(forZip);
                    sb.append(forZip)
                            .append("\n");
                }
                case 7 -> {
                    System.out.println("Введите адрес распаковки: ");
                    Scanner scannerAddress = new Scanner(System.in);
                    String address = scannerAddress.nextLine();
                    String forOpenZip = openZip(file, address);
                    System.out.println(forOpenZip);
                    sb.append(forOpenZip)
                            .append(" в ")
                            .append(address)
                            .append("\n");
                }
                case 8 -> {
                    System.out.println("Введите имя файла для десериализации: ");
                    Scanner scannerProgress = new Scanner(System.in);
                    String nameFile = scannerProgress.nextLine();
                    String forProgress = openProgress(file, nameFile);
                    System.out.println(forProgress);
                    sb.append(forProgress)
                            .append("\n");
                }
                case 9 -> {
                    String forDelete = deleteFile(file);
                    System.out.println(forDelete);
                    sb.append(forDelete)
                            .append("\n");
                }
                case 10 -> {
                    String forLog = readLog();
                    sb.append(forLog)
                            .append("\n");
                }
                case 11 -> {
                    System.out.println("Работа завершена");
                    writerLog(sb, file);
                }
                default -> System.out.println("Данной функции не существует");
            }
        }
    }

    public static String createNewFile(String text, File file) {
        File dir1 = new File(file, text);
        try {
            if (dir1.createNewFile())
                return "The file has been created: " + text;
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return "The file was not created: " + text;
    }

    public static String createNewDir(String text, File file) {
        File dir1 = new File(file + "/" + text);
        if (dir1.mkdir()) {
            return "the directory has been created: " + text;
        } else {
            return "the directory was not created: " + text;
        }
    }

    public static void writerLog(StringBuilder sb, File file) {
        try (FileOutputStream fos = new FileOutputStream(file + "/temp.txt", true)) {
            String text = String.valueOf(sb);
            byte[] bytes = text.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String readLog() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("/Users/admin/Documents/Games/temp/temp.txt")))) {
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Запрос лога";
    }

    public static void saveGame(GameProgress[] gp, File file) {
        int a = 0;
        for (GameProgress o : gp) {
            a++;
            try (FileOutputStream fos = new FileOutputStream(file + "/save" + a + ".dat");
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(o);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String zipFiles(File file) {
        if (file.isDirectory()) {
            try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(file + "/" + "zip_output.zip"))) {
                for (File item : Objects.requireNonNull(file.listFiles())) {
                    if (!item.getName().equals("zip_output.zip")) {
                        FileInputStream fis = new FileInputStream(item);
                        ZipEntry entry = new ZipEntry(item.getName());
                        zout.putNextEntry(entry);
                        byte[] buffer = new byte[fis.available()];
                        zout.write(buffer);
                        fis.close();
                        if (item.delete()) {
                            System.out.println(item.getName() + " перемещен в архив");
                        }
                    }
                }
                zout.closeEntry();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return "Архив успешно создан";
        } else {
            return "Не удалось создать архив";
        }
    }

    public static String deleteFile(File file) {
        if (file.isDirectory()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (File item : Objects.requireNonNull(file.listFiles())) {
                if (item.isFile()) {
                    if (item.delete()) {
                        stringBuilder.append("Файл ")
                                .append(item)
                                .append(" был удален\n");
                    }
                } else {
                    stringBuilder.append("Файл ")
                            .append(item)
                            .append(" не может быть удален");
                }
            }
            return String.valueOf(stringBuilder);
        }
        return "Не правильно указан адрес директории";
    }

    public static String openZip(File file, String address) {
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(file + "/" + "zip_output.zip"))) {
            ZipEntry entry;
            String name;
            while ((entry = zip.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(address + "/" + name);
                for (int e = zip.read(); e != -1; e = zip.read()) {
                    fout.write(e);
                }
                fout.flush();
                zip.closeEntry();
                fout.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "Распаковка файла прошла успешно";
    }

    public static String openProgress(File file, String nameFile) {
        GameProgress gameProgress;
        File f = new File(file, nameFile);
        if (f.isFile()) {
            try (FileInputStream fis = new FileInputStream(f);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                System.out.println("1111");
                gameProgress = (GameProgress) ois.readObject();
                System.out.println(gameProgress);
                ois.close();
                return "Десериализация выполнена из " + f;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Файл не найден");
        }
        return "Десериализация не может быть выполнена из " + f;
    }
}