package sqlcmd.int_test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.TranslationBase;
import sqlapp.controller.SQLCmdController;
import sqlapp.model.managers.Configuration;
import sqlapp.model.managers.PostgreSQLManager;
import sqlapp.view.View;

import java.util.List;

public class ExitTest {
	protected View viewMock;
	protected ArgumentCaptor<String> argument;
	protected SQLCmdController controller;

	@Before
	public void setUp() throws Exception {
		viewMock = Mockito.mock(View.class);
		AppConfig.translationBase = Mockito.mock(TranslationBase.class);

		Mockito.when(AppConfig.translationBase.getTranslation(Mockito.anyString())).thenAnswer(
				invocationOnMock -> {
					return invocationOnMock.getArgumentAt(0, String.class);
				}
		);

		argument = ArgumentCaptor.forClass(String.class);
		controller = new SQLCmdController(new PostgreSQLManager(new Configuration()));
		controller.addView(viewMock);

	}

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void test() {
		String expected_1 = Keys.CONNECTION_CLOSED.toString();
		String expected_2 = Keys.GOODBYE.toString();

		exit.expectSystemExit();
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				Mockito.verify(viewMock, Mockito.times(2)).render(argument.capture());

				List<String> list = argument.getAllValues();
				Assert.assertEquals(expected_1, list.get(0));
				Assert.assertEquals(expected_2, list.get(1));
			}
		});

		controller.onUserInput("exit");
	}
}
