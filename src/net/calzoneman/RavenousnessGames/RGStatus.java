package net.calzoneman.RavenousnessGames;

public enum RGStatus {
	SPECTATOR(0),
	COMPETITOR(1),
	SPONSOR(2),
	ADMIN(3);

	private final int value;

	private RGStatus(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static RGStatus getByValue(final int value) {
		for (RGStatus status : values()) {
			if (status.getValue() == value) {
				return status;
			}
		}
		return null;
	}
}
