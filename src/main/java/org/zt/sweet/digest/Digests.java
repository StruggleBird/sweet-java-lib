/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zt.sweet.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.zt.sweet.lang.Lang;
import org.zt.sweet.lang.Strings;

/**
 * Miscellaneous methods for calculating digests.
 * <p>
 * Mainly for internal use within the framework; consider <a
 * href="http://commons.apache.org/codec/">Apache Commons Codec</a> for a more
 * comprehensive suite of digest utilities.
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see org.apache.commons.codec.digest.DigestUtils
 */
public abstract class Digests {

	private static final String MD5_ALGORITHM_NAME = "MD5";
	private static final String SHA1_ALGORITHM_NAME = "SHA1";

	private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取指定字符串的 SHA1 值
	 * 
	 * @param cs
	 *            字符串
	 * @return 指定字符串的 SHA1 值
	 * @see #digest(String, CharSequence)
	 */
	public static String sha1(CharSequence cs) {
		return digest(SHA1_ALGORITHM_NAME, cs);
	}

	/**
	 * Calculate the MD5 digest of the given bytes.
	 * 
	 * @param bytes
	 *            the bytes to calculate the digest over
	 * @return the digest
	 */
	public static byte[] md5(byte[] bytes) {
		return digest(MD5_ALGORITHM_NAME, bytes);
	}

	public static byte[] md5(String text) {
		return md5(text.getBytes());
	}

	/**
	 * Return a hexadecimal string representation of the MD5 digest of the given
	 * bytes.
	 * 
	 * @param bytes
	 *            the bytes to calculate the digest over
	 * @return a hexadecimal digest string
	 */
	public static String md5AsHex(byte[] bytes) {
		return digestAsHexString(MD5_ALGORITHM_NAME, bytes);
	}
	
	public static String md5AsHex(CharSequence text) {
		return md5AsHex(text.toString().getBytes());
	}

	/**
	 * Append a hexadecimal string representation of the MD5 digest of the given
	 * bytes to the given {@link StringBuilder}.
	 * 
	 * @param bytes
	 *            the bytes to calculate the digest over
	 * @param builder
	 *            the string builder to append the digest to
	 * @return the given string builder
	 */
	public static StringBuilder appendMd5DigestAsHex(byte[] bytes,
			StringBuilder builder) {
		return appendDigestAsHex(MD5_ALGORITHM_NAME, bytes, builder);
	}

	/**
	 * 从字符串计算出数字签名
	 * 
	 * @param algorithm
	 *            算法，比如 "SHA1" 或者 "MD5" 等
	 * @param cs
	 *            字符串
	 * @return 数字签名
	 */
	public static String digest(String algorithm, CharSequence cs) {
		return digest(algorithm, Strings.getBytesUTF8(null == cs ? "" : cs),
				null, 1);
	}

	/**
	 * 从字节数组计算出数字签名
	 * 
	 * @param algorithm
	 *            算法，比如 "SHA1" 或者 "MD5" 等
	 * @param bytes
	 *            字节数组
	 * @param salt
	 *            随机字节数组
	 * @param iterations
	 *            迭代次数
	 * @return 数字签名
	 */
	public static String digest(String algorithm, byte[] bytes, byte[] salt,
			int iterations) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
	
			if (salt != null) {
				md.update(salt);
			}
	
			byte[] hashBytes = md.digest(bytes);
	
			for (int i = 1; i < iterations; i++) {
				md.reset();
				hashBytes = md.digest(hashBytes);
			}
	
			return fixedHexString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw Lang.wrapThrow(e);
		}
	}

	/**
	 * Creates a new {@link MessageDigest} with the given algorithm. Necessary
	 * because {@code MessageDigest} is not thread-safe.
	 */
	private static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException(
					"Could not find MessageDigest with algorithm \""
							+ algorithm + "\"", ex);
		}
	}

	private static byte[] digest(String algorithm, byte[] bytes) {
		return getDigest(algorithm).digest(bytes);
	}

	private static String digestAsHexString(String algorithm, byte[] bytes) {
		char[] hexDigest = digestAsHexChars(algorithm, bytes);
		return new String(hexDigest);
	}

	private static StringBuilder appendDigestAsHex(String algorithm,
			byte[] bytes, StringBuilder builder) {
		char[] hexDigest = digestAsHexChars(algorithm, bytes);
		return builder.append(hexDigest);
	}

	private static char[] digestAsHexChars(String algorithm, byte[] bytes) {
		byte[] digest = digest(algorithm, bytes);
		return encodeHex(digest);
	}

	private static char[] encodeHex(byte[] bytes) {
		char chars[] = new char[32];
		for (int i = 0; i < chars.length; i = i + 2) {
			byte b = bytes[i / 2];
			chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
			chars[i + 1] = HEX_CHARS[b & 0xf];
		}
		return chars;
	}
	
	public static String fixedHexString(byte[] hashBytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hashBytes.length; i++) {
			sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}

}
