package sqlapp.controller.commands;

import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;

public class HelpCommand extends Command {

    private Description object;

    public HelpCommand(Description target, ResponseModel model) {
        super("help", model);

        this.object = target;
    }

    @Override
    protected void executeInternal(String[] args) {
        String message = null;

        if (args != null && args.length > 0) {
            String option = args[0];
            if (option != null) {
                if (option.equals("list")) {
                    message = object.shortDescription();
                } else {
                    message = object.descriptionFor(args[0]);
                }
            }
        }

        if (message == null) {
            message = object.fullDescription();
        }

        responseModel().push(new ResponseData(message));
    }

    @Override
    protected String format() {
        return "help";
    }

    @Override
    protected String descKey() {
        return Keys.HELP_COMMAND_DESC.toString();
    }
}
