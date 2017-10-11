package sqlapp.controller.commands;

public interface Description {
	String fullDescription();
	String shortDescription();
	String descriptionFor(String key);
}
