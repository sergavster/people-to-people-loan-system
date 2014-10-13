package com.p2psys.util.tenpayUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public class RSAPublicKeyReader {
	public static PublicKey getPublicKeyFromFile(InputStream is,String charset) throws IOException,
			NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] bytes = loadPublicKeyStringFromFile(is,charset);

		KeyFactory kf = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
		PublicKey key = kf.generatePublic(keySpec);

		return key;
	}

	private static byte[] loadPublicKeyStringFromFile(InputStream is,String charset)
			throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(is));
		String line;
		do {
			line = bufferedReader.readLine();
		} while (line != null && line.charAt(0) != '-');

		do {
			line = bufferedReader.readLine();
		} while (line != null && line.charAt(0) == '-');

		while (line != null && line.charAt(0) != '-') {
			stringBuffer.append(line);
			line = bufferedReader.readLine();
		}
		String base64String = stringBuffer.toString();
		return Base64.decode(base64String.getBytes());
	}
}
