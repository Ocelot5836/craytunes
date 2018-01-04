package com.ocelot.gaming.apps.game.crayquest.world.property;

public class PropRotation extends PropInt {
	
	public void setRotation(EnumRoationDirections rotation) {
		this.setValue(rotation.getDirection());
		this.setMinValue(0);
		this.setMaxValue(3);
	}

	public PropRotation randomizeRotation() {
		setRotation(EnumRoationDirections.values()[random.nextInt(3)]);
		return this;
	}

	public enum EnumRoationDirections {
		NORMAL(0x00), X(0x01), XY(0x02);

		private byte flip;

		private EnumRoationDirections(int flip) {
			this.flip = (byte) flip;
		}

		public byte getDirection() {
			return flip;
		}
	}
}