package info.jukov.yandextranslatetest.ui;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import info.jukov.yandextranslatetest.R;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jukov
 * Date: 18.04.2017
 * Time: 9:41
 *
 * Класс для отобраения выпадающих списков. В отличие от Spinner, позволяет управлять header элементом независимо от выбранного элемента.
 */
public final class DropdownView extends FrameLayout {

	private static final int SHOW_DELAY = 80;

	private TextView textViewHeader;
	private ListPopupWindow listPopupWindow;

	private final List<OnItemClickListener> itemClickListeners = new ArrayList<>();
	private final List<OnClickListener> headerClickListeners = new ArrayList<>();

	/**
	 * Позволяет понять в каком состоянии сейчас находится {@link ListPopupWindow}.
	 * {@link ListPopupWindow#isShowing()} не используется, так как всё время возвращает false.
	 */
	private boolean isShowing = false;

	public DropdownView(@NonNull final Context context) {
		super(context);
		init();
	}

	public DropdownView(@NonNull final Context context,
		@Nullable final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DropdownView(@NonNull final Context context,
		@Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		textViewHeader.setEnabled(enabled);
	}

	public void setAdapter(final ListAdapter adapter) {
		listPopupWindow.setAdapter(adapter);
	}

	public void addOnItemClickListener(final OnItemClickListener listener) {
		itemClickListeners.add(listener);
	}

	public void addOnHeaderClickListener(final OnClickListener listener) {
		headerClickListeners.add(listener);
	}

	public void setText(final String text) {
		textViewHeader.setText(text);
	}

	private void init() {
		final LayoutInflater layoutInflater = LayoutInflater.from(getContext());

		textViewHeader = (TextView) layoutInflater.inflate(R.layout.component_dropdownview_textview, null);
		addView(textViewHeader);

		listPopupWindow = new ListPopupWindow(getContext());
		listPopupWindow.setAnchorView(this);
		listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
		listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
		listPopupWindow.setDropDownGravity(Gravity.START);
		listPopupWindow.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				for (final OnItemClickListener listener : itemClickListeners) {
					listener.onItemClick(parent, view, position, id);
				}
				listPopupWindow.dismiss();
				isShowing = false;
			}
		});
		listPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				isShowing = false;
			}
		});

		textViewHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				for (final OnClickListener listener : headerClickListeners) {
					listener.onClick(v);
				}

				if (!isShowing) {
//					Здесь нужна небольшая задержка, так как клавиатура убирается какое-то время,
// 					в течение которого остается активной и занимает место на экране.
//					Из-за этого окно listPopupWindow разворачивается только до клавиатуры.
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							listPopupWindow.show();
						}
					}, SHOW_DELAY);
					isShowing = true;
				} else {
					listPopupWindow.dismiss();
					isShowing = false;
				}
			}
		});
	}
}
