package Client.util;

import Common.exception.*;
import Common.data.*;
import Common.util.TextWriter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;


public class ScannerManager {

    /**
     * Константа строки приглашения для ввода в интерактивном режиме.
     */
    public static final String INPUT_INFO = "> ";

    /**
     * Константа строки приглашения для ввода команд в интерактивном режиме.
     */
    public static final String INPUT_COMMAND = "$ ";

    /**
     * Сканер для ввода.
     */
    private Scanner userScanner;

    /**
     * Режим скрипта.
     */
    private boolean scriptMode;

    /**
     * Шаблон для проверки числа.
     */
    private final String numberPattern = "-?\\d+(\\.\\d+)?";

    /**
     * Создает менеджер для ввода с помощью заданного сканера.
     * @param scanner сканер для ввода.
     * @param scriptMode режим чтение со скрипта
     */
    public ScannerManager(Scanner scanner, boolean scriptMode) {
        this.userScanner = scanner;
        this.scriptMode = scriptMode;
    }


    public Person askPerson() throws IncorrectInputInScriptException {
        return new Person(
                null,
                askName(),
                askCoordinates(),
                ZonedDateTime.now(),
                askHeight(),
                askEyeColor(),
                askHairColor(),
                askCountry(),
                askLocation()
        );
    }

    public String askName() throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                TextWriter.printInfoMessage("Введите имя персоны:");
                System.out.print(INPUT_INFO);
                name = userScanner.nextLine().trim();
                if (scriptMode) TextWriter.printInfoMessage(name);
                if (name.equals("")) throw new NotNullException();
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NotNullException exception) {
                TextWriter.printErr("Значение поля не может быть пустым.");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if (!userScanner.hasNext()) {
                    TextWriter.printErr("Работа программы прекращена.");
                    System.exit(1);
                }
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            }
        }
        return name;
    }

    public float askX() throws IncorrectInputInScriptException {
        String strX;
        float x;
        while (true) {
            try {
                TextWriter.printInfoMessage("Введите координату X:");
                System.out.print(INPUT_INFO);
                strX = userScanner.nextLine().trim();
                strX = strX.replace(",", ".");
                if (scriptMode) TextWriter.printInfoMessage(strX);
                if (!strX.matches(numberPattern)) throw new NumberFormatException();
                x = Float.parseFloat(strX);
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if (!userScanner.hasNext()) {
                    TextWriter.printErr("Работа программы прекращена.");
                    System.exit(1);
                }
            } catch (NumberFormatException exception) {
                TextWriter.printErr("Значение поля должно быть float.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            }
        }
        return x;
    }

    public float askY() throws IncorrectInputInScriptException {
        String strY;
        float y;
        while (true) {
            try {
                TextWriter.printInfoMessage("Введите координату Y:");
                System.out.print(INPUT_INFO);
                strY = userScanner.nextLine().trim();
                strY = strY.replace(",", ".");
                if (scriptMode) TextWriter.printInfoMessage(strY);
                if (!strY.matches(numberPattern)) throw new NumberFormatException();
                y = Float.parseFloat(strY);
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                TextWriter.printErr("Значение поля должно быть float.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            }
        }
        return y;
    }

    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        float x = askX();
        float y = askY();
        return new Coordinates(x, y);
    }

    public Float askHeight() throws IncorrectInputInScriptException {
        String strHeight;
        Float height;
        while (true) {
            try {
                TextWriter.printInfoMessage("Введите рост:");
                System.out.print(INPUT_INFO);
                strHeight = userScanner.nextLine().trim();
                strHeight = strHeight.replace(",", ".");
                if (scriptMode) TextWriter.printInfoMessage(strHeight);
                if (!strHeight.matches(numberPattern)) throw new NumberFormatException();
                height = Float.parseFloat(strHeight);
                if (height <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                TextWriter.printErr("Значение поля должно быть Float.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            } catch (NotInDeclaredLimitsException e) {
                TextWriter.printErr("Значение поля должно быть больше 0.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return height;
    }


    public ColorEye askEyeColor() throws IncorrectInputInScriptException {
        String strColorEye;
        ColorEye colorEye;
        while (true) {
            try {
                TextWriter.printInfoMessage("Список цветов: " + ColorEye.nameList());
                TextWriter.printInfoMessage("Введите цвет глаз персоны из списка:");
                System.out.print(INPUT_INFO);
                strColorEye = userScanner.nextLine().trim();
                if (scriptMode) TextWriter.printInfoMessage(strColorEye);
                if (strColorEye.equals("")) throw new NotNullException();
                colorEye = ColorEye.valueOf(strColorEye.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                TextWriter.printErr("Такого цвета нет в списке.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            } catch (NotNullException exception) {
                TextWriter.printErr("Значение поля не может быть пустым.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return colorEye;
    }

    public ColorHair askHairColor() throws IncorrectInputInScriptException {
        String strColorHair;
        ColorHair colorHair;
        while (true) {
            try {
                TextWriter.printInfoMessage("Список цветов: " + ColorHair.nameList());
                TextWriter.printInfoMessage("Введите цвет волос персоны из списка:");
                System.out.print(INPUT_INFO);
                strColorHair = userScanner.nextLine().trim();
                if (scriptMode) TextWriter.printInfoMessage(strColorHair);
                if (strColorHair.equals("")) throw new NotNullException();
                colorHair = ColorHair.valueOf(strColorHair.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                TextWriter.printErr("Такого цвета нет в списке.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            } catch (NotNullException exception) {
                TextWriter.printErr("Значение поля не может быть пустым.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return colorHair;
    }


    public Country askCountry() throws IncorrectInputInScriptException {
        String strCountry;
        Country country;
        while (true) {
            try {
                TextWriter.printInfoMessage("Список цветов: " + Country.nameList());
                TextWriter.printInfoMessage("Введите цвет волос персоны из списка:");
                System.out.print(INPUT_INFO);
                strCountry = userScanner.nextLine().trim();
                if (scriptMode) TextWriter.printInfoMessage(strCountry);
                if (strCountry.equals("")) throw new NotNullException();
                country = Country.valueOf(strCountry.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                TextWriter.printErr("Такого цвета нет в списке.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            } catch (NotNullException exception) {
                TextWriter.printErr("Значение поля не может быть пустым.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            }
        }
        return country;
    }


    public Double askXLoc() throws IncorrectInputInScriptException {
        String strX;
        Double x;
        while (true) {
            try {
                TextWriter.printInfoMessage("Введите координату X:");
                System.out.print(INPUT_INFO);
                strX = userScanner.nextLine().trim();
                strX = strX.replace(",", ".");
                if (scriptMode) TextWriter.printInfoMessage(strX);
                if (!strX.matches(numberPattern)) throw new NumberFormatException();
                x = Double.parseDouble(strX);
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                if (scriptMode) throw new IncorrectInputInScriptException();
                if (!userScanner.hasNext()) {
                    TextWriter.printErr("Работа программы прекращена.");
                    System.exit(1);
                }
            } catch (NumberFormatException exception) {
                TextWriter.printErr("Значение поля должно быть Double.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            }
        }
        return x;
    }

    public int askYLoc() throws IncorrectInputInScriptException {
        String strY;
        int y;
        while (true) {
            try {
                TextWriter.printInfoMessage("Введите координату Y:");
                System.out.print(INPUT_INFO);
                strY = userScanner.nextLine().trim();
                strY = strY.replace(",", ".");
                if (scriptMode) TextWriter.printInfoMessage(strY);
                if (!strY.matches(numberPattern)) throw new NumberFormatException();
                y = Integer.parseInt(strY);
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                TextWriter.printErr("Значение поля должно быть int.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            }
        }
        return y;
    }

    public double askZLoc() throws IncorrectInputInScriptException {
        String strZ;
        double z;
        while (true) {
            try {
                TextWriter.printInfoMessage("Введите координату Z:");
                System.out.print(INPUT_INFO);
                strZ = userScanner.nextLine().trim();
                strZ = strZ.replace(",", ".");
                if (scriptMode) TextWriter.printInfoMessage(strZ);
                if (!strZ.matches(numberPattern)) throw new NumberFormatException();
                z = Double.parseDouble(strZ);
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Значение поля не может быть использовано.");
                System.exit(1);
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                TextWriter.printErr("Значение поля должно быть double.");
                if (scriptMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Непредвиденная ошибка.");
                System.exit(1);
            }
        }
        return z;
    }


    public Location askLocation() throws IncorrectInputInScriptException {
        Double x = askXLoc();
        int y = askYLoc();
        double z = askZLoc();
        return new Location(x, y, z);
    }
}


