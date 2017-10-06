package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;

class UpdateTableCommand extends Command {

    UpdateTableCommand() {
        super("push");
    }

    @Override
    protected void executeInternal(String[] args) {

    }

	@Override
	protected String format() {
		return null;
	}

	@Override
	public String description() {
		return new ApplicationConfig.FormattedTranslation(Keys.UPDATE_COMMAND_DESC.toString()).translation();
	}
}
