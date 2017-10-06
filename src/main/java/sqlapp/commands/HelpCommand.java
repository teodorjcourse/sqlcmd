package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.utils.StringUtils;

public class HelpCommand extends Command {
    private Object object;

    public HelpCommand(Object target, ResponseModel model) {
        super("help", model);

        this.object = target;
    }

    @Override
    protected void executeInternal(String[] args) {
        responseModel().push(new ResponseData(object.toString()));
    }

    @Override
    protected String format() {
        return id();
    }

    @Override
    public String description() {
        return StringUtils.substitute(new ApplicationConfig.
                Translation(Keys.HELP_COMMAND_DESC.toString()).translation(),
                id(), format());
    }
}
