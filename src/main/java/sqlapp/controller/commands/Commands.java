package sqlapp.controller.commands;

import sqlapp.controller.exceptions.CommandNotFoundException;
import sqlapp.utils.SplitedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands implements Description {
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
        SplitedString splitedRequest = SplitedString.split(requestString, "\\|");
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
            resultString.append(String.format(cmd.description()));
            resultString.append(System.lineSeparator());
        }

        return resultString.toString();
    }

    @Override
    public String fullDescription() {
        return toString();
    }

    @Override
    public String shortDescription() {
        StringBuilder resultString = new StringBuilder();

        for (Command cmd : commands) {
            resultString.append("\t - ");
            resultString.append(cmd.id());
            resultString.append(System.lineSeparator());
        }

        return resultString.toString();
    }

    @Override
    public String descriptionFor(String key) {
        for (Command cmd : commands) {
            if (cmd.id().equals(key)) {
                return cmd.description();
            }
        }

        return fullDescription();
    }
}
