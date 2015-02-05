package org.zt.sweet.algorithm;

class Base64 {
	public static String toBase64(byte[] b) {
		return b == null ? null : toBase64(b, 0, b.length);
	}

	public static String toBase64(byte[] b, int off, int len) {
		int groups = len / 3;
		int rem = len - 3 * groups;
		StringBuilder sb = new StringBuilder();
		char[] cs = intToBase64;

		for (int i = 0; i < groups; i++) {
			int byte0 = b[off++] & 0xff;
			int byte1 = b[off++] & 0xff;
			int byte2 = b[off++] & 0xff;
			sb.append(cs[byte0 >> 2]);
			sb.append(cs[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
			sb.append(cs[(byte1 << 2) & 0x3f | (byte2 >> 6)]);
			sb.append(cs[byte2 & 0x3f]);
		}

		if (rem != 0) {
			int byte0 = b[off++] & 0xff;
			sb.append(cs[byte0 >> 2]);
			if (rem == 1) {
				sb.append(cs[(byte0 << 4) & 0x3f]);
				sb.append("==");
			} else {
				int byte1 = b[off++] & 0xff;
				sb.append(cs[(byte0 << 4) & 0x3f | (byte1 >> 4)]);
				sb.append(cs[(byte1 << 2) & 0x3f]);
				sb.append('=');
			}
		}
		return sb.toString();
	}

	private static final char[] intToBase64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();
	private static final byte base64ToInt[] = new byte[256];
	static {
		for (int i = 0; i < base64ToInt.length; i++)
			base64ToInt[i] = -1;
		for (int i = 0; i < intToBase64.length; i++)
			base64ToInt[intToBase64[i]] = (byte) i;
	}

	public static byte[] parseBase64(String s) {
		return s == null ? null : parseBase64(s, 0, s.length());
	}

	public static byte[] parseBase64(String s, int off, int len) {
		byte[] ai = base64ToInt;
		int groups = len / 4;
		if (4 * groups != len)
			throw new IllegalArgumentException("String length must be a multiple of four.");
		int missings = 0;
		int fullGroups = groups;
		if (len != 0) {
			if (s.charAt(off + len - 1) == '=') {
				missings++;
				fullGroups--;
			}
			if (s.charAt(off + len - 2) == '=')
				missings++;
		}
		byte[] result = new byte[3 * groups - missings];

		int inCursor = off, outCursor = 0;
		for (int i = 0; i < fullGroups; i++) {
			int ch0 = base64toInt(s.charAt(inCursor++), ai);
			int ch1 = base64toInt(s.charAt(inCursor++), ai);
			int ch2 = base64toInt(s.charAt(inCursor++), ai);
			int ch3 = base64toInt(s.charAt(inCursor++), ai);
			result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));
			result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
			result[outCursor++] = (byte) ((ch2 << 6) | ch3);
		}

		if (missings != 0) {
			int ch0 = base64toInt(s.charAt(inCursor++), ai);
			int ch1 = base64toInt(s.charAt(inCursor++), ai);
			result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));

			if (missings == 1) {
				int ch2 = base64toInt(s.charAt(inCursor++), ai);
				result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
			}
		}
		return result;
	}

	private static int base64toInt(char c, byte[] alphaToInt) {
		int result = alphaToInt[c];
		if (result < 0)
			throw new IllegalArgumentException("Illegal character " + c);
		return result;
	}

}
