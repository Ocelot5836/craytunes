package com.ocelot.gaming.apps.game.crayquest.gfx;

/**
 * This class can loop through an array of images using a specified animation time.
 * 
 * @author Ocelot5836
 */
public class Animation {

	private Sprite[] frames;
	private int currentFrame;

	private long startTime;
	private long delay;

	private boolean playedOnce;

	/**
	 * Creates a new animation.
	 */
	public Animation() {
		playedOnce = false;
	}

	/**
	 * Sets the frames to be looped through to The specified frames.
	 * 
	 * @param frames
	 *            The frames to loop through
	 */
	public void setFrames(Sprite[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}

	/**
	 * Sets the delay to the specified delay.
	 * 
	 * @param delay
	 *            The delay that will be looped through
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * This sets the delay to -1 so it does not loop anymore.
	 */
	public void clearDelay() {
		this.delay = -1;
	}

	/**
	 * This sets the current frame to the specified frame.
	 * 
	 * @param currentFrame
	 */
	public void setFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	/**
	 * Updates the animation.
	 */
	public void update() {
		if (delay == -1)
			return;

		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}

		if (currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
	}

	/**
	 * @return The current frame
	 */
	public int getFrame() {
		return currentFrame;
	}

	/**
	 * @return The sprite at the current frame
	 */
	public Sprite getImage() {
		return frames[currentFrame];
	}

	/**
	 * @return Whether this animation has already played once
	 */
	public boolean hasPlayedOnce() {
		return playedOnce;
	}
}