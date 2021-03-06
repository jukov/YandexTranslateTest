package info.jukov.yandextranslatetest.ui.format;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse.Definition;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse.Example;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse.Meaning;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse.Syn;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse.Translation;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.StringUtils;
import java.util.Iterator;
import java.util.List;

/**
 * User: jukov
 * Date: 25.03.2017
 * Time: 10:39
 */

public final class DictionaryConstructor {

	private static final String EMPTY = "";
	private static final String SPACE = " ";
	private static final String DOT = ".";
	private static final String COMMA = ",";
	private static final String TIRET = "—";
	private static final String OPEN_PARENTHESIS = "(";
	private static final String CLOSE_PARENTHESIS = ")";
	private static final String OPEN_BRACE = "[";
	private static final String CLOSE_BRACE = "]";

	@ColorInt private static int secondaryItemsColor;

	public static void init(@NonNull final Context context) {
		Guard.checkNotNull(context, "null == context");

		secondaryItemsColor = context.getResources().getColor(R.color.text_dictConstructor_secondaryItems);
	}

	/**
	 * Прикрепляет к {@code parent} набор {@link android.view.View},
	 * которые визуализируют объект {@link LookupResponse}
	 */
	public static void makeLookupResponse(@NonNull final Context context, @NonNull final LinearLayout parent,
		@NonNull final LookupResponse response) {
		Guard.checkNotNull(context, "null == context");
		Guard.checkNotNull(parent, "null == parent");
		Guard.checkNotNull(response, "null == response");
		Guard.checkPreCondition(secondaryItemsColor != 0, "Call method init before use this class");

		final LayoutInflater inflater = LayoutInflater.from(context);

		//Части речи
		for (final Definition definition : response.getDefinitions()) {

			final TextView textViewPartOfSpeech = (TextView) inflater
				.inflate(R.layout.component_dict_item_part_or_speech, null);
			parent.addView(textViewPartOfSpeech);

			textViewPartOfSpeech.setText(definition.getPartOfSpeech());

			//Варианты перевода
			if (definition.getTranslationList() != null) {
				makeTranslations(parent, inflater, definition.getTranslationList());
			}
		}
	}

	private static void makeTranslations(@NonNull final LinearLayout parent, @NonNull final LayoutInflater inflater,
		@NonNull final List<Translation> translations) {

		int translationCount = 0;

		for (final Translation translation : translations) {

			translationCount++;

			String count = Integer.toString(translationCount);

			final TextView textViewDefinitions = (TextView) inflater
				.inflate(R.layout.component_dict_item_definition, null);
			parent.addView(textViewDefinitions);

			final SpannableStringBuilder stringBuilderDefinitions = new SpannableStringBuilder();
			stringBuilderDefinitions.append(count).append(DOT).append(SPACE);

			stringBuilderDefinitions.setSpan(new StyleSpan(BOLD), 0, count.length() + DOT.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

			if (translation.getText() != null) {
				stringBuilderDefinitions.append(translation.getText());

				if (translation.getGen() != null) {

					stringBuilderDefinitions.append(SPACE).append(translation.getGen());

					final int genStart = stringBuilderDefinitions.length() - translation.getGen().length();
					final int genEnd = stringBuilderDefinitions.length();

					stringBuilderDefinitions.setSpan(new StyleSpan(ITALIC), genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);

					stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor), genStart, genEnd,
						SPAN_EXCLUSIVE_EXCLUSIVE);
				}

				if (translation.getAsp() != null) {
					stringBuilderDefinitions.append(SPACE).append(translation.getAsp());

					final int aspStart = stringBuilderDefinitions.length() - translation.getAsp().length();
					final int aspEnd = stringBuilderDefinitions.length();

					stringBuilderDefinitions.setSpan(new StyleSpan(ITALIC), aspStart, aspEnd, SPAN_EXCLUSIVE_EXCLUSIVE);

					stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor), aspStart, aspEnd,
						SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}

			if (translation.getSynonyms() != null) {
				for (final Syn syn : translation.getSynonyms()) {

					if (syn.getText() != null) {
						stringBuilderDefinitions.append(COMMA).append(SPACE).append(syn.getText());

						if (syn.getGen() != null) {
							stringBuilderDefinitions.append(SPACE).append(syn.getGen());

							final int genStart = stringBuilderDefinitions.length() - syn.getGen().length();
							final int genEnd = stringBuilderDefinitions.length();

							stringBuilderDefinitions.setSpan(new StyleSpan(ITALIC), genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);

							stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor), genStart, genEnd,
								SPAN_EXCLUSIVE_EXCLUSIVE);
						}

						if (syn.getAsp() != null) {
							stringBuilderDefinitions.append(SPACE).append(syn.getAsp());

							final int aspStart = stringBuilderDefinitions.length() - syn.getAsp().length();
							final int aspEnd = stringBuilderDefinitions.length();

							stringBuilderDefinitions.setSpan(new StyleSpan(ITALIC), aspStart, aspEnd,
								SPAN_EXCLUSIVE_EXCLUSIVE);

							stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor), aspStart, aspEnd,
								SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
				}
			}

			textViewDefinitions.setText(stringBuilderDefinitions);

			//Синонимы на исходном языке
			if (translation.getMeanings() != null) {
				makeMeanings(parent, inflater, translation.getMeanings());
			}

			//Примеры употребления
			if (translation.getExamples() != null) {
				makeExamples(parent, inflater, translation.getExamples());
			}
		}
	}

	private static void makeMeanings(@NonNull final LinearLayout parent,
		@NonNull final LayoutInflater inflater, @NonNull final List<Meaning> meanings) {

		final TextView textViewMeaning = (TextView) inflater
			.inflate(R.layout.component_dict_item_meaning, null);
		parent.addView(textViewMeaning);

		final StringBuilder stringBuilderMeanings = new StringBuilder(OPEN_PARENTHESIS);

		for (final Iterator<Meaning> iterator = meanings.iterator();
			iterator.hasNext(); ) {

			final Meaning meaning = iterator.next();

			stringBuilderMeanings.append(meaning.getText());

			if (iterator.hasNext()) {
				stringBuilderMeanings.append(COMMA).append(SPACE);
			}
		}

		stringBuilderMeanings.append(CLOSE_PARENTHESIS);

		textViewMeaning.setText(stringBuilderMeanings.toString());
	}

	private static void makeExamples(@NonNull final LinearLayout parent,
		@NonNull final LayoutInflater inflater, @NonNull final List<Example> examples) {

		Guard.checkPreCondition(secondaryItemsColor != 0, "Call method init before use this class");

		final SpannableStringBuilder stringBuilderExample = new SpannableStringBuilder();

		for (final Example example : examples) {

			final TextView textViewExample = (TextView) inflater
				.inflate(R.layout.component_dict_item_example, null);
			parent.addView(textViewExample);

			stringBuilderExample.clear();

			stringBuilderExample
				.append(example.getText());

			if (example.getTr() != null) {
				stringBuilderExample.append(SPACE).append(TIRET).append(SPACE)
					.append(example.getTr().get(0).getText());
			}

			stringBuilderExample.setSpan(new StyleSpan(ITALIC), 0, stringBuilderExample.length(),
				SPAN_EXCLUSIVE_EXCLUSIVE);

			textViewExample.setText(stringBuilderExample);
		}
	}

	/**
	 * Возвращает строку и информацией о транскрипции слова.
	 */
	public static CharSequence formatDefinition(@NonNull final LookupResponse response) {
		Guard.checkNotNull(response, "null == response");
		Guard.checkPreCondition(secondaryItemsColor != 0, "Call method init before use this class");

		if (response.getDefinitions() != null) {

			final SpannableStringBuilder formattedDefinition = new SpannableStringBuilder();

			formattedDefinition
				.append(response.getDefinitions().get(0).getText());

			if (!StringUtils.isNullOrEmpty(response.getDefinitions().get(0).getTs())) {

				final int transcStart = formattedDefinition.length();

				formattedDefinition
					.append(SPACE)
					.append(OPEN_BRACE)
					.append(response.getDefinitions().get(0).getTs())
					.append(CLOSE_BRACE);

				formattedDefinition.setSpan(new ForegroundColorSpan(secondaryItemsColor), transcStart, formattedDefinition.length(),
					SPAN_EXCLUSIVE_INCLUSIVE);
			}

			return formattedDefinition;
		}
		return EMPTY;
	}

	private DictionaryConstructor() {
	}
}
