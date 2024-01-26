package es.tributasenasturias.documentopagoutils;

import java.util.ArrayList;

public class SHAUtils {
	/*
	 * Configurable variables. You may need to tweak these to be compatible with the server-side, but the defaults work
	 * in most cases.
	 */
	static int hexcase = 1; /* hex output format. 0 - lowercase; 1 - uppercase */
	static String b64pad = "="; /* base-64 pad character. "=" for strict RFC compliance */
	static int chrsz = 8; /* bits per input character. 8 - ASCII; 16 - Unicode */

	/*
	 * Estas son las funciones que tienes que usar según tus necesidades Reciben cadenas como argumentos y devuelven
	 * cadenas codificadas HEX o BASE-64
	 */
	public static String str2hex(String s) {
		return binb2hex(str2binb(s));
	}

	public static String hex_sha1(String s) {
		return binb2hex(core_sha1(str2binb(s), s.length() * chrsz));
	}

	private static String b64_sha1(String s) {
		return binb2b64(core_sha1(str2binb(s), s.length() * chrsz));
	}

	private static String str_sha1(String s) {
		return binb2str(core_sha1(str2binb(s), s.length() * chrsz));
	}

	public static String hex_hmac_sha1(String key, String data) {
		return binb2hex(core_hmac_sha1(key, data));
	}

	private static String b64_hmac_sha1(String key, String data) {
		return binb2b64(core_hmac_sha1(key, data));
	}

	private static String str_hmac_sha1(String key, String data) {
		return binb2str(core_hmac_sha1(key, data));
	}

	/*
	 * Testea si VM está trabajando
	 */
	public static boolean sha1_vm_test() {
		return hex_sha1("abc").equals("a9993e364706816aba3e25717850c26c9cd0d89d".toUpperCase());
	}

	/*
	 * Calcula el SHA-1 de un array de palabras big-endian, y un bit de longitud
	 */
	private static int[] core_sha1(int[] x, int len) {
		/* append padding */
		int key = len >> 5;
		x = pad(x, key, 0); // size the array if need be
		x[len >> 5] |= 0x80 << (24 - len % 32);
		// x[((len + 64 >> 9) << 4) + 15] = len;
		int key2 = ((len + 64 >> 9) << 4) + 15;
		x = pad(x, key2, len); // size the array if need be

		int[] w = new int[80];
		int a = 1732584193;
		int b = -271733879;
		int c = -1732584194;
		int d = 271733878;
		int e = -1009589776;

		for (int i = 0; i < x.length; i += 16) {
			int olda = a;
			int oldb = b;
			int oldc = c;
			int oldd = d;
			int olde = e;

			for (int j = 0; j < 80; j++) {
				if (j < 16)
					w[j] = x[i + j];
				else
					w[j] = rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);
				int t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)), safe_add(safe_add(e, w[j]), sha1_kt(j)));
				e = d;
				d = c;
				c = rol(b, 30);
				b = a;
				a = t;
			}

			a = safe_add(a, olda);
			b = safe_add(b, oldb);
			c = safe_add(c, oldc);
			d = safe_add(d, oldd);
			e = safe_add(e, olde);
		}
		return new int[] { a, b, c, d, e };

	}

	/*
	 * Perform the appropriate triplet combination function for the current iteration
	 */
	private static int sha1_ft(int t, int b, int c, int d) {
		if (t < 20)
			return (b & c) | ((~b) & d);
		if (t < 40)
			return b ^ c ^ d;
		if (t < 60)
			return (b & c) | (b & d) | (c & d);
		return b ^ c ^ d;
	}

	/*
	 * Determine the appropriate additive constant for the current iteration
	 */
	private static int sha1_kt(int t) {
		return (t < 20) ? 1518500249 : (t < 40) ? 1859775393 : (t < 60) ? -1894007588 : -899497514;
	}

	/**
	 * create larger int array - especially for the sha-core method
	 * 
	 * @param a
	 *            - int[] array
	 * @param size
	 *            - create new array to this size
	 * @param len
	 *            - stick this value at the end of the array, which copies the javascript
	 * @return
	 */
	private static int[] pad(int[] a, int size, int len) {
		// change to based on zero ordinal
		size++;
		if (a.length > size) {
			return a;
		}
		int[] c = new int[size];
		for (int i = 0; i < size; i++) {
			if (i < a.length) {
				c[i] = a[i];
			}
		}
		if (len > 0) {
			c[c.length - 1] = len;
		}
		return c;
	}

	/*
	 * Calculate the HMAC-SHA1 of a key and some data
	 */
	private static int[] core_hmac_sha1(String key, String data) {

		int[] bkey = str2binb(key);
		if (bkey.length > 16)
			bkey = core_sha1(bkey, key.length() * chrsz);
		if (bkey.length < 16) {
			bkey = pad(bkey, 15, 0);
		}

		int[] ipad = new int[16], opad = new int[16];
		for (int i = 0; i < 16; i++) {
			ipad[i] = bkey[i] ^ 0x36363636;
			opad[i] = bkey[i] ^ 0x5C5C5C5C;
		}
		int[] hash = core_sha1(join(ipad, str2binb(data)), 512 + data.length() * chrsz);
		return core_sha1(join(opad, hash), 512 + 160);
	}

	/*
	 * Add integers, wrapping at 2^32. This uses 16-bit operations internally to work around bugs in some JS
	 * interpreters.
	 */
	private static int safe_add(int x, int y) {
		int lsw = (x & 0xFFFF) + (y & 0xFFFF);
		int msw = (x >> 16) + (y >> 16) + (lsw >> 16);
		return (msw << 16) | (lsw & 0xFFFF);
	}

	/*
	 * Bitwise rotate a 32-bit number to the left.
	 */
	private static int rol(int num, int cnt) {
		return (num << cnt) | (num >>> (32 - cnt));
	}

	/*
	 * Convert an 8-bit or 16-bit string to an array of big-endian words In 8-bit function, characters >255 have their
	 * hi-byte silently ignored.
	 */
	private static int[] str2binb(String str) {
		int bin = 0;
		int index = 0;
		int mask = (1 << chrsz) - 1;
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (int i = 0; i < str.length() * chrsz; i += chrsz) {
			int pos = (i / chrsz); // & mask;
			byte unicode = (byte) str.substring(pos, pos + 1).charAt(0);
			int cal = (unicode & mask) << (32 - chrsz - i % 32);
			if (i >> 5 > index) {
				index++;
				ar.add(bin);
				bin = 0;
			}
			bin |= cal;
		}
		ar.add(bin);
		int[] rtn = new int[ar.size()];
		for (int i = 0; i < ar.size(); i++) {
			rtn[i] = ar.get(i);
		}
		return rtn;
	}

	/*
	 * Convert an array of big-endian words to a string
	 */
	private static String binb2str(int[] bin) {
		String str = "";
		int mask = (1 << chrsz) - 1;
		for (int i = 0; i < bin.length * 32; i += chrsz)
			str += (char) ((bin[i >> 5] >>> (32 - chrsz - i % 32)) & mask);
		return str;
	}

	/*
	 * Convert an array of big-endian words to a hex string.
	 */
	public static String binb2hex(int[] binarray) {
		String hex_tab = hexcase != 0 ? "0123456789ABCDEF" : "0123456789abcdef";
		String str = "";
		for (int i = 0; i < binarray.length * 4; i++) {
			// logger.debug((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4))+
			// "_"+(binarray[i >> 2] >> ((3 - i % 4) * 8)));
			str += hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4)) & 0xF) + ""
					+ hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8)) & 0xF);
		}
		return str;
	}
	/*
	 * Convert an array of hex string to big-endian words.
	 */
	public static byte[] hex2binb(String strarray) {
		strarray=strarray.toUpperCase();
		String hex_tab = "0123456789ABCDEF";//hexcase != 0 ? "0123456789ABCDEF" : "0123456789abcdef";
		if(strarray.length()%2!=0)
			strarray=strarray.concat("0");
		byte[] binarray=new byte[strarray.length()/2];
		for (int i = 0; i < strarray.length(); i+=2) {
			int b1=hex_tab.indexOf(strarray.charAt(i));
			int b2=hex_tab.indexOf(strarray.charAt(i+1));
			int val=b1*16+b2;val=(val<128)?val:val-256;
			binarray[i>>1]=(byte)val;
		}//logger.debug();
		return binarray;
	}

	/*
	 * Convert an array of big-endian words to a hex string.
	 */
	public static String binb2hex(byte[] binarray) {
		String hex_tab = hexcase != 0 ? "0123456789ABCDEF" : "0123456789abcdef";
		String str = "";
		for (int i = 0; i < binarray.length; i++) {
			byte b = binarray[i];
			if (b >= 0)
				str += hex_tab.charAt((b >> 4)) + "" + hex_tab.charAt(b - ((b >> 4) << 4));
			else
				str += hex_tab.charAt((b >> 4) + (256 >> 4)) + "" + hex_tab.charAt(b - ((b >> 4) << 4) );
		}
		return str;
	}

	/*
	 * Convert an array of big-endian words to a base-64 string
	 */
	private static String binb2b64(int[] binarray) {
		// examinar si hace falta corregir el array?
		String tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String str = "";
		for (int i = 0; i < binarray.length * 4; i += 3) {
			int triplet = (((binarray[i >> 2] >> 8 * (3 - i % 4)) & 0xFF) << 16)
					| (((binarray[i + 1 >> 2] >> 8 * (3 - (i + 1) % 4)) & 0xFF) << 8)
					| ((binarray[i + 2 >> 2] >> 8 * (3 - (i + 2) % 4)) & 0xFF);
			for (int j = 0; j < 4; j++) {
				if (i * 8 + j * 6 > binarray.length * 32)
					str += b64pad;
				else
					str += tab.charAt((triplet >> 6 * (3 - j)) & 0x3F);
			}
		}
		return str;
	}

	private static String hex_b64(String hashHex) {
		String hash2 = "";
		for (int i = 0; i < 20; i++) {
			hash2 += "%" + hashHex.substring(i * 2, i * 2 + 2);
		}
		return (binb2b64(str2binb(unescape(hash2))));
	}

	private static String unescape(String in) {
		return in;
	}

	private static int[] join(int[] a, int[] b) {
		int[] c = new int[a.length + b.length];
		int index = 0;
		for (int i = 0; i < a.length; i++) {
			c[index] = a[i];
			index++;
		}
		for (int i = 0; i < b.length; i++) {
			c[index] = b[i];
			index++;
		}
		return c;
	}

}
