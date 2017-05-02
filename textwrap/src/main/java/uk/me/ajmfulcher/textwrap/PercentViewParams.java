package uk.me.ajmfulcher.textwrap;

import android.annotation.SuppressLint;
import android.support.percent.PercentLayoutHelper;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

class PercentViewParams implements WrappedViewParams {

	private final int width;
	private final int height;

	@HorizontalGravity
	private final int gravity;

	/**
	 * Create an instance of {@link WrappedViewParams} from a view implementing {@link android.support.percent.PercentLayoutHelper.PercentLayoutParams}
	 * @param view view to wrap
	 * @param totalWidth total width of viewport
	 */
	PercentViewParams(View view, int totalWidth) {
		if(view.getLayoutParams() instanceof PercentLayoutHelper.PercentLayoutParams) {
			PercentLayoutHelper.PercentLayoutInfo layoutInfo = ((PercentLayoutHelper.PercentLayoutParams) view.getLayoutParams()).getPercentLayoutInfo();
			width = Math.round(totalWidth * layoutInfo.widthPercent);
			height = Math.round(1 / layoutInfo.aspectRatio * width);
		} else {
			throw new RuntimeException("Supplied view does not implement PercentLayoutInfo");
		}
		gravity = computeGravity(view);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getHorizontalGravity() {
		return gravity;
	}

	@SuppressLint("RtlHardcoded")
	private @HorizontalGravity
	int computeGravity(View view) {
		if(view.getParent() instanceof FrameLayout) {
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
			int gravity = GravityCompat.getAbsoluteGravity(params.gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK, ViewCompat.getLayoutDirection(view));
			switch(gravity) {
				case Gravity.LEFT:
					return Gravity.LEFT;
				case Gravity.RIGHT:
					return Gravity.RIGHT;
				default:
					throw new RuntimeException("Unsupported horizontal gravity");
			}
		}
		throw new RuntimeException("View parent is of unknown type");
	}

}
