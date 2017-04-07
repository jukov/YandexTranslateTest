package info.jukov.yandextranslatetest.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * User: jukov
 * Date: 06.04.2017
 * Time: 22:56
 *
 * Behavior для AppBarLayout, который позволяет отключить
 * стандартную обработку касания при нажатии на расположенные на этом AppBarLayout View.
 */
public final class IgnoreSwipeBehavior extends AppBarLayout.Behavior {

	public IgnoreSwipeBehavior() {
	}

	public IgnoreSwipeBehavior(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(final CoordinatorLayout parent, final AppBarLayout child, final MotionEvent ev) {

		final Rect rectBounds = new Rect();
		child.getHitRect(rectBounds);

		if (rectBounds.contains((int) ev.getX(), (int) ev.getY())) {
			return false;
		}

		return super.onInterceptTouchEvent(parent, child, ev);
	}
}
