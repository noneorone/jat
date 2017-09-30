package org.noneorone.security.encrypt;
public enum AESType {

	ECB("ECB", "0"), CBC("CBC", "1"), CFB("CFB", "2"), OFB("OFB", "3");
	private String k;
	private String v;

	private AESType(String k, String v) {
		this.k = k;
		this.v = v;
	}

	public String key() {
		return this.k;
	}

	public String value() {
		return this.v;
	}

	public static AESType get(String key) {
		AESType[] vs = AESType.values();
		for (int i = 0; i < vs.length; i++) {
			AESType d = vs[i];
			if (d.key().equals(key))
				return d;
		}
		return AESType.CBC;
	}

}
