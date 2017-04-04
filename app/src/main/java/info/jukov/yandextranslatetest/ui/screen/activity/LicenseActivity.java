package info.jukov.yandextranslatetest.ui.screen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.util.Guard;

/**
 * User: jukov
 * Date: 04.04.2017
 * Time: 0:21
 */

public class LicenseActivity extends AppCompatActivity {

	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.containerLicense) LinearLayout containerLicense;
	@BindView(R.id.textViewYandexCopyright) TextView textViewCopyright;

	public static void start(@NonNull final Context context) {
		Guard.checkNotNull(context, "null == context");

	    Intent starter = new Intent(context, LicenseActivity.class);
	    context.startActivity(starter);
	}
	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_license);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		final String[] licenseArray = getResources().getStringArray(R.array.licenses);

		for (final String license : licenseArray) {

			final TextView textViewLicense = (TextView) getLayoutInflater().inflate(R.layout.component_license_item, null);

			textViewLicense.setText(Html.fromHtml(license));
//			textViewLicense.setMovementMethod(new LinkMovementMethod());

			containerLicense.addView(textViewLicense);
		}

		textViewCopyright.setText(Html.fromHtml(getString(R.string.translateFragment_copyright)));
		textViewCopyright.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
