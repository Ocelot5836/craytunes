package com.ocelot.gaming.apps.game.crayquest.world.property;

import java.util.Random;

public class Prop {

	protected Random random;
	protected String name;

	public String getName() {
		return name;
	}

	public Prop setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Tries to turn the property into a {@link PropBool}
	 * 
	 * @return The property
	 */
	public PropBool toBool() {
		return (PropBool) this;
	}

	/**
	 * Tries to turn the property into a {@link PropInt}
	 * 
	 * @return The property
	 */
	public PropInt toInt() {
		return (PropInt) this;
	}
}