package farm.chaos.ppfax.model;

public enum ParagraphStyle {

	WIDE_IMAGE("wide image"), IMAGE_LEFT("image inline left"), IMAGE_RIGHT("image inline right");

	private String name;
	ParagraphStyle(String n) {
		name = n;
	}
	public String getName() {
		return name;
	}
}
