package sqlapp.commands;

import sqlapp.controller.exceptions.CommandNotFoundException;
import sqlapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands {
    private List<Command> commands;

    public Commands() {
        commands = new ArrayList<>();
    }

    public void register(Command command) {
        commands.add(command);
    }

    public void tryToExecute(String requestString)
            throws Exception
    {
        StringUtils.SplitedString splitedRequest = StringUtils.SplitedString.split(requestString, "\\|");
        Command cmd;

        for (int i = 0; i < commands.size(); i++) {
            cmd = commands.get(i);

            if (checkMatching(cmd, splitedRequest.get(0))) {
                cmd.execute(Arrays.copyOfRange(splitedRequest.all(), 1, splitedRequest.all().length));

                return;
            }
        }

        throw new CommandNotFoundException(splitedRequest.get(0));
    }

    private boolean checkMatching(Command cmd, String cmdParams) {
        return cmd.equals(cmdParams);
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();

        for (Command cmd : commands) {
            resultString.append(cmd.description());
            resultString.append(System.lineSeparator());
        }

        return resultString.toString();
    }
}
