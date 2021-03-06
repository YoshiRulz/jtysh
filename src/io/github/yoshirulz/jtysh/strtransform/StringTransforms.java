package io.github.yoshirulz.jtysh.strtransform;

/**
 * @author YoshiRulz
 * @version 2017-12-11/00
 */
public enum StringTransforms { ;
	public enum LatinCharset { ;
		@SuppressWarnings("HardCodedStringLiteral")
		public static String[] getUppercase() {
			return new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		}
		public static String[] getNumbers() {
			return new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		}
		@SuppressWarnings("HardCodedStringLiteral")
		public static String[] getLowercase() {
			return new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		}
	}

	enum TransformationType {
		Cipher, SimpleDigital,
		RegEx,
		Relex,
		Unknown
	}

	@FunctionalInterface
	public interface StringTransformation {
		TransformationType getTransformType();
	}

	public interface StringTransform1To1 extends StringTransformation {
		String performOn(String s);
		default String[] performOnEach(String[] a) {
			int l = a.length;
			String[] toReturn = new String[l];
			for (int i = 0; i < l; i++) toReturn[i] = performOn(a[i]);
			return toReturn;
		}
	}

	public interface StringTransform2To1 extends StringTransformation {
		String performOn(String s, String s1);
		default String[] performOnEach(String[] a, String[] a1) {
			int l = a.length;
			if (l != a1.length) throw new RuntimeException("OOPS");
			String[] toReturn = new String[l];
			for (int i = 0; i < l; i++) toReturn[i] = performOn(a[i], a1[i]);
			return toReturn;
		}
	}

	public interface StringTransform2To2 extends StringTransformation {
		String[][] performOn(String s, String s1);
	}
}
