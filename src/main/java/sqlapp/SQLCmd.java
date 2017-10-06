package sqlapp;

import sqlapp.controller.SQLCmdController;
import sqlapp.model.managers.PostgreSQLManager;
import sqlapp.view.ConsoleView;
import java.util.logging.Logger;

public class SQLCmd {
	private SQLCmdController controller;

	public SQLCmd() {
		controller = new SQLCmdController(new PostgreSQLManager());
		controller.addView(new ConsoleView());
	}

	public void run() {

		controller.monitorConsoleInput();
	}

	public static void main(String[] args) {
		disableLogging();

		new SQLCmd().run();
	}

	private static void disableLogging() {
		// отключаем логирование
		// код нашел найден на просторах интернета
		Logger l0 = Logger.getLogger("");
		l0.removeHandler(l0.getHandlers()[0]);
	}
}
