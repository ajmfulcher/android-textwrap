package uk.me.ajmfulcher.textwrap;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.TextView;

final class StaticLayoutFactory {

	/**
	 * Creates a static layout based on the values of the outer TextView with the width of the remaining space
	 * once the view we're wrapping around is taken into account.
	 * @param textView underlying {@link TextView}
	 * @param baseText the view's text
	 * @param width remaining space once the view we're wrapping around is taken into account.
	 * @return static layout
	 */
	static StaticLayout create(TextView textView, CharSequence baseText, int width) {
		final int version = android.os.Build.VERSION.SDK_INT;
		if (version >= 23) {
			return createMarshmallow(textView, baseText, width);
		} else {
			return createCompat(textView, baseText, width);
		}
	};

	@TargetApi(Build.VERSION_CODES.M)
	private static StaticLayout createMarshmallow(TextView textView, CharSequence baseText, int width) {
		return StaticLayout.Builder.obtain(baseText, 0, baseText.length(), textView.getPaint(), width)
			.setAlignment(Layout.Alignment.ALIGN_NORMAL)
			.setLineSpacing(textView.getLineSpacingExtra(), textView.getLineSpacingMultiplier())
			.setEllipsize(textView.getEllipsize())
			.setIncludePad(false)
			.setBreakStrategy(textView.getBreakStrategy())
			.setHyphenationFrequency(textView.getHyphenationFrequency())
			.build();
	}

	private static StaticLayout createCompat(TextView textView, CharSequence baseText, int width) {
		return new StaticLayout(baseText, 0, baseText.length(), new TextPaint(textView.getPaint()), width, Layout.Alignment.ALIGN_NORMAL,
			textView.getLineSpacingMultiplier(), textView.getLineSpacingExtra(),
			false, textView.getEllipsize(), 0);
	}

}
