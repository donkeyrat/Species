package com.ninni.species.access;

public interface CrankingEntity {

	int getShotsFired();
	void setShotsFired(int value);

	default void addShotsFired() {
		int value = this.getShotsFired();
		if (value < 40) this.setShotsFired(this.getShotsFired() + 1);
	}

}
