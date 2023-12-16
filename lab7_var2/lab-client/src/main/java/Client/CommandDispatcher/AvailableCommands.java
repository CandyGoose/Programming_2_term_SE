package Client.CommandDispatcher;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Класс, хранящий доступные команды для диспетчера команд.
 */
public class AvailableCommands {
    /**
     * Множество команд без аргументов.
     */
    public static final Set<String> COMMANDS_WITHOUT_ARGS = new HashSet<>();
    /**
     * Множество команд с аргументом ID.
     */
    public static final Set<String> COMMANDS_WITH_ID_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_STRING_ARG = new HashSet<>();

    /**
     * Множество команд с аргументом персоны.
     */
    public static final Set<String> COMMANDS_WITH_PERSON_ARG = new HashSet<>();
    /**
     * Множество команд с аргументами ID и персоны.
     */
    public static final Set<String> COMMANDS_WITH_PERSON_ID_ARGS = new HashSet<>();
    /**
     * Множество команд, принимающих аргументом скрипт.
     */
    public static final Set<String> SCRIPT_ARGUMENT_COMMAND = new HashSet<>();

    static {
        Collections.addAll(COMMANDS_WITHOUT_ARGS,
                "help",
                "show",
                "info",
                "exit",
                "remove_first",
                "clear",
                "average_of_height",
                "head",
                "history"
        );
        Collections.addAll(COMMANDS_WITH_ID_ARG,
                "remove_by_id"
        );
        Collections.addAll(COMMANDS_WITH_STRING_ARG,
                "remove_any_by_hair_color",
                "filter_starts_with_name"
        );
        Collections.addAll(COMMANDS_WITH_PERSON_ARG,
                "add"
        );
        Collections.addAll(COMMANDS_WITH_PERSON_ID_ARGS,
                "update");
        SCRIPT_ARGUMENT_COMMAND.add("execute_script");
    }
}
