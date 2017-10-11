package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.SplitedString;
import sqlapp.utils.StringUtils;

import java.util.Arrays;

public class DeleteRowCommand extends Command {
    DataBaseManager dataBaseManager;

    public DeleteRowCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
        super("delete", responseModel);

        this.dataBaseManager = dataBaseManager;
    }

    @Override
    protected void executeInternal(String[] args)
            throws DataBaseRequestException
    {
        int result = dataBaseManager.deleteRows(args[0], Arrays.copyOfRange(args, 1, args.length));
        String key = result > 0 ? Keys.DELETE_ROW_SUCCESS.toString() : Keys.DELETE_ROW_SUCCESS_ZERO.toString();

        responseModel().push(
                new ResponseData(
                        StringUtils.substitute(
                                AppConfig.translationBase.getTranslation(key),
                                new String[]{args[0], Integer.toString(result)}
                        )
                )
        );
    }

    @Override
    protected String format() {
        return "delete | tableName | column | value";
    }

    @Override
    protected String descKey() {
        return Keys.DELETE_COMMAND_DESC.toString();
    }

//    @Override
//    public String description() {
//        SplitedString splited = SplitedString.split(format(), "\\|");
//
//        String[] args = new String[splited.all().length + 1];
//        args[0] = id();
//        args[1] = format();
//        System.arraycopy(splited.all(), 1, args, 2, splited.all().length - 1);
//
//        return StringUtils.substitute(
//                AppConfig.translationBase.getTranslation(Keys.DELETE_COMMAND_DESC.toString()),
//                args
//        );
//    }
}
