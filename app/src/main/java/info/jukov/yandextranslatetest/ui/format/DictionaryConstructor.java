package info.jukov.yandextranslatetest.ui.format;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce.Definition;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce.Example;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce.Meaning;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce.Syn;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce.Translation;
import info.jukov.yandextranslatetest.util.StringUtils;
import java.util.Iterator;
import java.util.List;

/**
 * User: jukov
 * Date: 25.03.2017
 * Time: 10:39
 */

public final class DictionaryConstructor {

	private static final String SPACE = " ";
	private static final String DOT = ".";
	private static final String COMMA = ",";
	private static final String TIRET = "—";
	private static final String OPEN_PARENTHESIS = "(";
	private static final String CLOSE_PARENTHESIS = ")";
	private static final String OPEN_BRACE = "[";
	private static final String CLOSE_BRACE = "]";

	/**
	 * Прикрепляет к {@code parent} набор {@link android.view.View},
	 * которые визуализируют объект {@link LookupResponce}
	 */
	public static void visualiseLookupResponce(@NonNull final Context context,
		@NonNull final LinearLayout parent, @NonNull final LookupResponce responce) {

		parent.removeAllViews();
		final LayoutInflater inflater = LayoutInflater.from(context);

		//Части речи
		for (final Definition definition : responce.getDefinitions()) {

			final TextView textViewPartOfSpeech = (TextView) inflater
				.inflate(R.layout.component_dict_item_part_or_speech, null);
			parent.addView(textViewPartOfSpeech);

			textViewPartOfSpeech.setText(definition.getPartOfSpeech());

			//Варианты перевода
			if (definition.getTranslations() != null) {
				visualiseTranslations(parent, inflater, definition.getTranslations());
			}
		}
	}

	private static void visualiseTranslations(@NonNull final LinearLayout parent,
		@NonNull final LayoutInflater inflater, @NonNull final List<Translation> translations) {

		int translationCount = 0;

		for (final Translation translation : translations) {

			translationCount++;

			final TextView textViewDefinitions = (TextView) inflater
				.inflate(R.layout.component_dict_item_definition, null);
			parent.addView(textViewDefinitions);

			final StringBuilder stringBuilderDefinitions = new StringBuilder();
			stringBuilderDefinitions.append(translationCount).append(DOT).append(SPACE);

			if (translation.getText() != null) {
				stringBuilderDefinitions.append(translation.getText());

				if (translation.getGen() != null) {
					stringBuilderDefinitions.append(SPACE).append(translation.getGen());
				}
			}

			if (translation.getSynonyms() != null) {
				for (final Syn syn : translation.getSynonyms()) {

					if (syn.getText() != null) {
						stringBuilderDefinitions.append(COMMA).append(SPACE)
							.append(syn.getText());

						if (syn.getGen() != null) {
							stringBuilderDefinitions.append(SPACE).append(syn.getGen());
						}
					}
				}
			}

			textViewDefinitions.setText(stringBuilderDefinitions.toString());

			//Синонимы на исходном языке
			if (translation.getMeanings() != null) {
				visualiseMeanings(parent, inflater, translation.getMeanings());
			}

			//Примеры употребления
			if (translation.getExamples() != null) {
				visualiseExamples(parent, inflater, translation.getExamples());
			}
		}
	}

	private static void visualiseMeanings(@NonNull final LinearLayout parent,
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

	private static void visualiseExamples(@NonNull final LinearLayout parent,
		@NonNull final LayoutInflater inflater, @NonNull final List<Example> examples) {

		final StringBuilder stringBuilderExample = new StringBuilder();

		for (final Example example : examples) {

			final TextView textViewExample = (TextView) inflater
				.inflate(R.layout.component_dict_item_example, null);
			parent.addView(textViewExample);

			stringBuilderExample.setLength(0);

			stringBuilderExample
				.append(example.getText());

			if (example.getTr() != null) {
				stringBuilderExample.append(SPACE).append(TIRET).append(SPACE)
					.append(example.getTr().get(0).getText());
			}

			textViewExample.setText(stringBuilderExample.toString());
		}
	}

	/**
	* Возвращает строку и информацией о транскрипции слова.
	* */
	public static CharSequence formatDefinition(
		@NonNull final LookupResponce.Definition definition) {

		final SpannableStringBuilder formattedDefinition = new SpannableStringBuilder();

		formattedDefinition
			.append(definition.getText());

		if (!StringUtils.isNullOrEmpty(definition.getTs())) {
			formattedDefinition
				.append(SPACE)
				.append(OPEN_BRACE)
				.append(definition.getTs())
				.append(CLOSE_BRACE);
		}

		return formattedDefinition;
	}

	private DictionaryConstructor() {
	}
}
