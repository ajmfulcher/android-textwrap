package uk.me.ajmfulcher.textwrap;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.percent.PercentFrameLayout;
import android.support.percent.PercentRelativeLayout;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TextWrapLayout extends PercentFrameLayout {

	private View wrappedView;
	private TextView textView;
	private CharSequence text;
	private int width;

	public TextWrapLayout(Context context) {
		this(context, null);
	}

	public TextWrapLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextWrapLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
			@Override
			public void onChildViewAdded(View parent, View child) {
				scanForViews();
			}

			@Override
			public void onChildViewRemoved(View parent, View child) {}
		});
	}

	/**
	 * Set text to wrap
	 * @param text text to wrap
	 */
	public void setText(CharSequence text) {
		this.text = text;
		if(textView != null && wrappedView != null) {
			requestLayout();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(textView != null && wrappedView != null && text != null) {
			int width = View.getDefaultSize(0, widthMeasureSpec) - (getPaddingLeft() + getPaddingRight());
			if(this.width != width) {
				WrappedViewParams wrappedViewParams = new PercentViewParams(wrappedView, width);
				TextWithInset textWithInset = new ReplacementSpanInset(textView, text, width, wrappedViewParams);
				textView.setText(textWithInset.getTextWithInsets());
				this.width = width;
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(PercentRelativeLayout.LayoutParams.MATCH_PARENT, PercentRelativeLayout.LayoutParams.MATCH_PARENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	private void scanForViews() {
		for(int i = 0 ; i < getChildCount() ; i++) {
			View v = getChildAt(i);
			LayoutParams params = (LayoutParams) v.getLayoutParams();
			if(params.viewType == LayoutParams.TEXT) {
				textView = (TextView) v;
				maybeOverrideParameters(textView);
			} else if(params.viewType == LayoutParams.WRAPPED) {
				wrappedView = v;
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	private void maybeOverrideParameters(TextView textView) {
		final int version = android.os.Build.VERSION.SDK_INT;
		if (version >= 23) {
			/**
			 * Here we override the default break strategy and hyphenation to disable
			 * advanced line break techniques
			 * FIXME: these values are passed through to the underlying static layout but {@link ReplacementSpanInset}
			 * breaks when using more advanced strategies.  These should be supported.
			 */
			textView.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
			textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NONE);
		}
	}

	public static class LayoutParams extends PercentFrameLayout.LayoutParams {

		private static final int UNSET = -1;

		/**
		 * Text in this view should be populated by {@link TextWrapLayout}
		 */
		@SuppressWarnings("WeakerAccess")
		public static final int TEXT = 0;

		/**
		 * Text should be wrapped around this view
		 */
		@SuppressWarnings("WeakerAccess")
		public static final int WRAPPED = 1;

		@Retention(RetentionPolicy.SOURCE)
		@IntDef({TEXT, WRAPPED})
		@interface TextWrapViewType {}

		private int viewType = UNSET;

		@SuppressWarnings("WeakerAccess")
		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.TextWrapLayout);
			try {
				viewType = array.getInteger(R.styleable.TextWrapLayout_textWrap_viewType, UNSET);
			} finally {
				array.recycle();
			}
		}

		@SuppressWarnings("WeakerAccess")
		public LayoutParams(int width, int height) {
			super(width, height);
		}

		@SuppressWarnings("unused")
		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		@SuppressWarnings("unused")
		public LayoutParams(ViewGroup.MarginLayoutParams source) {
			super(source);
		}


		/**
		 * Tells {@link TextWrapLayout} to use this view
		 * Possible values are {@value TEXT} and {@value WRAPPED}
		 * Note that there should only be one view of each type in a layout
		 * @param viewType view type
		 */
		@SuppressWarnings("unused")
		public void setTextWrapViewType(@TextWrapViewType int viewType) {
			this.viewType = viewType;
		}

	}

}
