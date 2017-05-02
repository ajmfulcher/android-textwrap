package uk.me.ajmfulcher.textwrap;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.style.ReplacementSpan;
import android.view.Gravity;
import android.widget.TextView;

@SuppressLint("RtlHardcoded")
class ReplacementSpanInset implements TextWithInset {

	private final CharSequence text;

	/**
	 * Creates inset text using an implementation of {@link ReplacementSpan}
	 * @param textView text view that will receive inset text
	 * @param baseText text to wrap
	 * @param totalWidth total width of the viewport
	 * @param wrappedViewParams details of the wrapped view
	 */
	ReplacementSpanInset(TextView textView, CharSequence baseText, int totalWidth, WrappedViewParams wrappedViewParams) {
		StaticLayout tempLayout = StaticLayoutFactory.create(textView, baseText, totalWidth - wrappedViewParams.getWidth());
		float lineHeight = tempLayout.getHeight() / tempLayout.getLineCount();
		int linesHigh = (int) Math.ceil(wrappedViewParams.getHeight() / lineHeight);
		SpannableStringBuilder builder;
		if(wrappedViewParams.getHorizontalGravity() == Gravity.RIGHT) {
			builder = leftAligned(tempLayout, wrappedViewParams.getWidth(), linesHigh);
		} else {
			builder = rightAligned(tempLayout, wrappedViewParams.getWidth(), linesHigh);
		}
		text = builder;
	}

	@Override
	public CharSequence getTextWithInsets() {
		return text;
	}

	private SpannableStringBuilder leftAligned(StaticLayout tempLayout, int viewWidth, int linesHigh) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		for(int i = 0 ; i < linesHigh ; i++) {
			builder.append(tempLayout.getText(), tempLayout.getLineStart(i), tempLayout.getLineEnd(i));
			if(tempLayout.getLineEnd(i) == tempLayout.getText().length()) {
				return builder;
			}
			// The last character on each line is a space when left-aligned. So attach the span to
			// the last real character instead.
			// TODO: implement behaviour for RtL layouts (will need to find the first non-space character in either direction)
			builder.setSpan(new PaddingSpan(viewWidth, Gravity.LEFT), builder.length() - 2, builder.length() -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		builder.append(tempLayout.getText(), tempLayout.getLineStart(linesHigh), tempLayout.getText().length());
		return builder;
	}

	private SpannableStringBuilder rightAligned(StaticLayout tempLayout, int viewWidth, int linesHigh) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		for(int i = 0 ; i < linesHigh ; i++) {
			builder.append(tempLayout.getText(), tempLayout.getLineStart(i), tempLayout.getLineEnd(i));
			builder.setSpan(new PaddingSpan(viewWidth, Gravity.RIGHT), tempLayout.getLineStart(i), tempLayout.getLineStart(i) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			if(tempLayout.getLineEnd(i) == tempLayout.getText().length()) {
				return builder;
			}
		}
		builder.append(tempLayout.getText(), tempLayout.getLineStart(linesHigh), tempLayout.getText().length());
		return builder;
	}

	private class PaddingSpan extends ReplacementSpan {

		private final int width;
		private final int gravity;

		PaddingSpan(int width, int gravity) {
			this.width = width;
			this.gravity = gravity;
		}

		@Override
		public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
			return Math.round(width + paint.measureText(text, start, end));
		}

		@Override
		public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
			if(gravity == Gravity.RIGHT) {
				canvas.drawText(text, start, end, width, y, paint);
			} else {
				canvas.drawText(text, start, end, x, y, paint);
			}
		}
	}

}
