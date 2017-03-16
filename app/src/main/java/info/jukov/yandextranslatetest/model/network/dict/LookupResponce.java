package info.jukov.yandextranslatetest.model.network.dict;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

/**
 * User: jukov
 * Date: 16.03.2017
 * Time: 22:09
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"def"
})
@JsonIgnoreProperties({"head"})
public final class LookupResponce {

	@JsonProperty("def")
	private List<Def> def = null;

	@JsonProperty("def")
	public List<Def> getDef() {
		return def;
	}

	@JsonProperty("def")
	public void setDef(final List<Def> def) {
		this.def = def;
	}

	@Override
	public String toString() {
		return "LookupResponce{" +
			"def=" + def +
			'}';
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
		"text",
		"pos",
		"ts",
		"tr"
	})
	public static final class Def {

		@JsonProperty("text")
		private String text;
		@JsonProperty("pos")
		private String pos;
		@JsonProperty("ts")
		private String ts;
		@JsonProperty("tr")
		private List<Tr> tr = null;

		@JsonProperty("text")
		public String getText() {
			return text;
		}

		@JsonProperty("text")
		public void setText(final String text) {
			this.text = text;
		}

		@JsonProperty("pos")
		public String getPos() {
			return pos;
		}

		@JsonProperty("pos")
		public void setPos(final String pos) {
			this.pos = pos;
		}

		@JsonProperty("ts")
		public String getTs() {
			return ts;
		}

		@JsonProperty("ts")
		public void setTs(final String ts) {
			this.ts = ts;
		}

		@JsonProperty("tr")
		public List<Tr> getTr() {
			return tr;
		}

		@JsonProperty("tr")
		public void setTr(final List<Tr> tr) {
			this.tr = tr;
		}

		@Override
		public String toString() {
			return "Def{" +
				"text='" + text + '\'' +
				", pos='" + pos + '\'' +
				", ts='" + ts + '\'' +
				", tr=" + tr +
				'}';
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
		"text",
		"tr"
	})
	public static final class Ex {

		@JsonProperty("text")
		private String text;
		@JsonProperty("tr")
		private List<Tr_> tr = null;

		@JsonProperty("text")
		public String getText() {
			return text;
		}

		@JsonProperty("text")
		public void setText(final String text) {
			this.text = text;
		}

		@JsonProperty("tr")
		public List<Tr_> getTr() {
			return tr;
		}

		@JsonProperty("tr")
		public void setTr(final List<Tr_> tr) {
			this.tr = tr;
		}


		@Override
		public String toString() {
			return "Ex{" +
				"text='" + text + '\'' +
				", tr=" + tr +
				'}';
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
		"text"
	})
	public static final class Mean {

		@JsonProperty("text")
		private String text;

		@JsonProperty("text")
		public String getText() {
			return text;
		}

		@JsonProperty("text")
		public void setText(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return "Mean{" +
				"text='" + text + '\'' +
				'}';
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
		"text",
		"pos",
		"gen"
	})
	public static final class Syn {

		@JsonProperty("text")
		private String text;
		@JsonProperty("pos")
		private String pos;
		@JsonProperty("gen")
		private String gen;
		@JsonProperty("asp")
		private String asp;

		@JsonProperty("text")
		public String getText() {
			return text;
		}

		@JsonProperty("text")
		public void setText(final String text) {
			this.text = text;
		}

		@JsonProperty("pos")
		public String getPos() {
			return pos;
		}

		@JsonProperty("pos")
		public void setPos(final String pos) {
			this.pos = pos;
		}

		@JsonProperty("gen")
		public String getGen() {
			return gen;
		}

		@JsonProperty("gen")
		public void setGen(final String gen) {
			this.gen = gen;
		}

		@JsonProperty("asp")
		public String getAsp() {
			return asp;
		}

		@JsonProperty("asp")
		public void setAsp(final String asp) {
			this.asp = asp;
		}

		@Override
		public String toString() {
			return "Syn{" +
				"text='" + text + '\'' +
				", pos='" + pos + '\'' +
				", gen='" + gen + '\'' +
				'}';
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
		"text",
		"pos",
		"gen",
		"syn",
		"mean",
		"ex",
		"asp"
	})
	public static final class Tr {

		@JsonProperty("text")
		private String text;
		@JsonProperty("pos")
		private String pos;
		@JsonProperty("gen")
		private String gen;
		@JsonProperty("syn")
		private List<Syn> syn = null;
		@JsonProperty("mean")
		private List<Mean> mean = null;
		@JsonProperty("ex")
		private List<Ex> ex = null;
		@JsonProperty("asp")
		private String asp;

		@JsonProperty("text")
		public String getText() {
			return text;
		}

		@JsonProperty("text")
		public void setText(final String text) {
			this.text = text;
		}

		@JsonProperty("pos")
		public String getPos() {
			return pos;
		}

		@JsonProperty("pos")
		public void setPos(final String pos) {
			this.pos = pos;
		}

		@JsonProperty("gen")
		public String getGen() {
			return gen;
		}

		@JsonProperty("gen")
		public void setGen(final String gen) {
			this.gen = gen;
		}

		@JsonProperty("syn")
		public List<Syn> getSyn() {
			return syn;
		}

		@JsonProperty("syn")
		public void setSyn(final List<Syn> syn) {
			this.syn = syn;
		}

		@JsonProperty("mean")
		public List<Mean> getMean() {
			return mean;
		}

		@JsonProperty("mean")
		public void setMean(final List<Mean> mean) {
			this.mean = mean;
		}

		@JsonProperty("ex")
		public List<Ex> getEx() {
			return ex;
		}

		@JsonProperty("ex")
		public void setEx(final List<Ex> ex) {
			this.ex = ex;
		}

		@JsonProperty("asp")
		public String getAsp() {
			return asp;
		}

		@JsonProperty("asp")
		public void setAsp(final String asp) {
			this.asp = asp;
		}

		@Override
		public String toString() {
			return "Tr{" +
				"text='" + text + '\'' +
				", pos='" + pos + '\'' +
				", gen='" + gen + '\'' +
				", syn=" + syn +
				", mean=" + mean +
				", ex=" + ex +
				", asp='" + asp + '\'' +
				'}';
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
		"text"
	})
	public static final class Tr_ {

		@JsonProperty("text")
		private String text;

		@JsonProperty("text")
		public String getText() {
			return text;
		}

		@JsonProperty("text")
		public void setText(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return "Tr_{" +
				"text='" + text + '\'' +
				'}';
		}
	}
}
