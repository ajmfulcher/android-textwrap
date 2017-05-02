package uk.me.ajmfulcher.textwrap;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;
import android.view.Gravity;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

interface WrappedViewParams {

	@Retention(SOURCE)
	@SuppressLint("RtlHardcoded")
	@IntDef({Gravity.LEFT, Gravity.RIGHT})
	@interface HorizontalGravity {
	}

	/**
	 * Get width of the wrapped view
	 * @return wrapped view width
	 */
	int getWidth();

	/**
	 * Get height of the wrapped view
	 * @return wrapped view height
	 */
	int getHeight();

	/**
	 * Get the horizontal gravity of the wrapped view
	 * @return horizontal gravity
	 */
	@HorizontalGravity
	int getHorizontalGravity();

}
