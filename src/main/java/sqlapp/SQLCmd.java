package sqlapp;

import sqlapp.controller.SQLCmdController;
import sqlapp.model.ResponseData;
import sqlapp.model.events.Event;
import sqlapp.model.events.Events;
import sqlapp.model.managers.Configuration;
import sqlapp.model.managers.PostgreSQLManager;
import sqlapp.view.ConsoleView;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public class SQLCmd {
	private SQLCmdController controller;

	public SQLCmd() {
        Configuration databaseConfiguration = loadConfiguration(AppConfig.CONFIGURATION_FILE);
        tracePropsInfo(databaseConfiguration);
		controller = new SQLCmdController(new PostgreSQLManager(databaseConfiguration));
		controller.addView(new ConsoleView());
	}

    private Configuration loadConfiguration(String filePath) {
        InputStream is = this.getClass().getResourceAsStream(filePath);

        Configuration conf = null;
        Properties dbProperties = new Properties();

        try {
            dbProperties.load(is);
            conf = new Configuration(dbProperties);
        } catch (Throwable any) {
            Log.err("Due to load error database.properties would be set as default");
            conf = new Configuration();
        }

        return conf;
    }

    public void run() {
	    controller.notifyViews(
	    		new Event(Events.CHANGED.toString(),
					    new ResponseData(
					    		AppConfig.translationBase.getTranslation(Keys.GREETING.toString())
					    )
			    )
	    );

	    Scanner in = new Scanner(System.in);

	    while (true) {
		    controller.notifyViews(
		    		new Event(Events.CHANGED.toString(),
						    new ResponseData(
						    		AppConfig.translationBase.getTranslation(Keys.INPUT.toString())
						    )
		            )
		    );

		    controller.onUserInput(in.nextLine());
	    }
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

	private static void tracePropsInfo(Configuration configuration) {
	    Log.info(configuration.toString());
    }
}
