package sqlapp.view;

public class ConsoleView implements View {

	private void print(String data) {
		System.out.print(data);
	}

	@Override
	public void render(String data) {
		print(data);
	}
}