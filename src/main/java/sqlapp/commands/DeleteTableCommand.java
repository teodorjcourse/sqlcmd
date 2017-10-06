package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;

public class DeleteTableCommand extends Command {
    DataBaseManager dataBaseManager;

    public DeleteTableCommand(DataBaseManager dataBaseManager, ResponseModel response) {
        super("delete", response);

        this.dataBaseManager = dataBaseManager;
    }

    @Override
    protected void executeInternal(String[] args)
            throws DataBaseRequestException
    {
    }

    @Override
    protected String format() {
        return null;
    }

    @Override
    public String description() {
        return new ApplicationConfig.FormattedTranslation(Keys.DELETE_COMMAND_DESC.toString()).translation();
    }
}
