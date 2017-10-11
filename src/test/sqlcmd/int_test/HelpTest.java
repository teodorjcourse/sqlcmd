package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.controller.commands.*;

import java.util.ArrayList;
import java.util.List;

public class HelpTest extends IntBase {
	private StringBuilder expectedFull;
	private StringBuilder expectedShort;
	private List<String> expectedMessages;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		expectedFull = new StringBuilder();
		expectedShort = new StringBuilder();

		expectedFull.append(Keys.LANGUAGE_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.OPEN_CONNECTION_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.CLOSE_CONNECTION_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.CONNECT_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.CREATE_DATABASE_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.DROP_DATABASE_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.CREATE_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.DROP_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.CLEAR_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.INSERT_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.UPDATE_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.DELETE_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.TABLES_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.FIND_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.EXIT_COMMAND_DESC).append(System.lineSeparator());
		expectedFull.append(Keys.HELP_COMMAND_DESC).append(System.lineSeparator());


		expectedShort.append("\t - ").append(new ChangeLanguage(null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new OpenConnectionCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new CloseConnection(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new ConnectCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new CreateDataBaseCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new DropDataBaseCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new CreateTableCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new DropTableCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new ClearTableCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new InsertRowCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new UpdateTableCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new DeleteRowCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new GetTablesCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new SelectTableCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new ExitCommand(null, null).id()).append(System.lineSeparator());
		expectedShort.append("\t - ").append(new HelpCommand(null, null).id()).append(System.lineSeparator());


		expectedMessages = new ArrayList<>();
		expectedMessages.add(expectedFull.toString());
		expectedMessages.add(expectedShort.toString());
		expectedMessages.add(Keys.CONNECT_COMMAND_DESC.toString());
		expectedMessages.add(expectedFull.toString());
		expectedMessages.add(expectedFull.toString());
	}


	// тенстируем правильный вывод команды хелп
	public void test() {
		controller.onUserInput("help");
		controller.onUserInput("help|list");
		controller.onUserInput("help|connect");
		controller.onUserInput("help|wrong_input");
		controller.onUserInput("help|wrong_input||||");

		Mockito.verify(viewMock, Mockito.times(5)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for(int index = 0; index < capturedArgs.size(); index++) {
			assertEquals(expectedMessages.get(index), capturedArgs.get(index));
		}
	}
}
