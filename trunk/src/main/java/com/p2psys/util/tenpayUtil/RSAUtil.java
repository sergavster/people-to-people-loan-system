package com.p2psys.util.tenpayUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;




public class RSAUtil {
	private static final String signatureAlgorithm = "SHA1WithRSA";
	
	//static {
	//	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	//}
	
	private static  byte[] loadBytesFromPemFile(String fileName) 
	throws IOException {
		StringBuffer stringBuffer = new StringBuffer();

		FileInputStream fs = new FileInputStream(fileName);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(fs));
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
	
	
	public static PublicKey getPublicKeyFromFile(String fileName) throws NoSuchAlgorithmException, 
	InvalidKeySpecException, IOException  {	
		byte[] bytes = loadBytesFromPemFile(fileName);
		
		KeyFactory kf = KeyFactory.getInstance("RSA");
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
		
		PublicKey key = kf.generatePublic(keySpec);
		
		return key;
		}
	
	public static  PrivateKey getPrivateKeyFromPKCS8(
			String fileName) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException  {
	 
		byte[] keyBytes = loadBytesFromPemFile(fileName);
		PKCS8EncodedKeySpec privatePKCS8 = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(privatePKCS8);
	}
	
	public static BigInteger getElement(DataInputStream ds) throws Exception {
		 int count = 0;
		 int bt = 0;
		 
		 bt = ds.readUnsignedByte();
		 if(bt != 0x02){
			 throw(new Exception("pem format err,element head : " +  bt + " != 0x02"));
		 }
		 
		 bt = ds.readUnsignedByte();
		 if(bt == 0x81) {
			 count = ds.readUnsignedByte();
		 } else if( bt == 0x82) {
			 count = ds.readUnsignedShort();
		 } else {
			 count = bt;
		 }
		 
		 byte[] value = new byte[count];
		 ds.read(value, 0, count);
		 
		 return new BigInteger(value); 
	 }
	
	
	public static RSAPrivateCrtKey getPrivateKeyFromPem(String fileName) throws Exception  {
	    
		 BigInteger MODULUS, E, D, P, Q, DP, DQ, IQ;
		 
		 byte[] keyBytes = loadBytesFromPemFile(fileName);
		 
		 DataInputStream ds = new DataInputStream(new ByteArrayInputStream(keyBytes));
		 
		 int bt = 0;
		 int twoBytes = 0;
		 
		 twoBytes = ds.readUnsignedShort();
		 if(twoBytes == 0x3081){
			 ds.readByte();
		 }else if(twoBytes == 0x3082) {
			 ds.readShort();
		 } else {
			 throw(new Exception("pem format err,head 1: " +  twoBytes + " != 0x3081 or 0x3082"));
		 }
		 
		 twoBytes = ds.readUnsignedShort();
		 bt = ds.readUnsignedByte();
		 if(twoBytes != 0x0201 || bt != 0x00) {
			 throw(new Exception("pem format err,head 2: " +  twoBytes + " != 0x0201 or " + bt + " != 0x00"));

		 }
		 
		 MODULUS = getElement(ds); 
		 E = getElement(ds);
		 D = getElement(ds); 
		 P = getElement(ds);
		 Q = getElement(ds);
		 DP = getElement(ds);
		 DQ = getElement(ds);
		 IQ = getElement(ds);

		 RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(MODULUS, E, D, P, Q, DP, DQ, IQ);
		 
		 RSAPrivateCrtKey key = (RSAPrivateCrtKey)KeyFactory.getInstance("RSA").generatePrivate(keySpec);
		 return key;
	 }
	
	//密钥格式不是PEM就是PKCS8
	public static byte[] rsaPrivateCrypt(byte[] srcBytes, Boolean isEncrypt, String rsaPrivateKeyFile, Boolean isPem) throws Exception{
			 
		// Cipher rsaCipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		// Cipher rsaCipher=Cipher.getInstance("RSA/ECB/NoPadding");
		Cipher rsaCipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		 
		 PrivateKey key;
		 if(isPem) {
			 key= getPrivateKeyFromPem(rsaPrivateKeyFile);
		 } else {
			 key= getPrivateKeyFromPKCS8(rsaPrivateKeyFile);
		 }
		 
		 int mode;
		 if(isEncrypt) {
			mode = Cipher.ENCRYPT_MODE; 
		 } else {
			 mode = Cipher.DECRYPT_MODE;
		 }
		 rsaCipher.init(mode, key);
		 

		 byte[] resBytes = rsaCipher.doFinal(srcBytes);
		 		 
		 return resBytes;
	 }
	
	public static String rsaPrivateCryptString(String content,Boolean isEncrypt, String rsaPrivateKeyFile, Boolean isPem)  throws Exception{
		PrivateKey key;
		if(isPem) {
			key= getPrivateKeyFromPem(rsaPrivateKeyFile);
		} else {
			key= getPrivateKeyFromPKCS8(rsaPrivateKeyFile);
		}
		//System.out.println("content in rsaPrivateCryptString :"+content); 
		
		java.security.Signature signer = java.security.Signature.getInstance(signatureAlgorithm);
		
		signer.initSign((PrivateKey) key);
		
		signer.update(content.getBytes("gbk"));
		
		return new String(Base64.encode(signer.sign()));
	}
	
	
	public static boolean rsaDecryptString(String content, String charset, String base64Sign, String rsaPublicKeyFile)  throws Exception{
			
		//System.out.println("rsaPublicKeyFile in rsaDecryptString :"+rsaPublicKeyFile); 
		
		//InputStream publicKeyInputStream = ResponseHandler.class.getResourceAsStream(rsaPublicKeyFile);
		////System.out.println("publicKeyInputStream in rsaDecryptString :"+publicKeyInputStream); 
		
		PublicKey publicKey = getPublicKeyFromFile(rsaPublicKeyFile);
		//System.out.println("publicKey in rsaDecryptString :"+publicKey);
		
		java.security.Signature signVerifier = java.security.Signature.getInstance(signatureAlgorithm);
		
		signVerifier.initVerify((PublicKey) publicKey);
		
		signVerifier.update(content.getBytes(charset));
		
		//System.out.println("content in rsaDecryptString :"+content); 
		
		byte[] sign = Base64.decode(base64Sign.getBytes(charset));
		
		boolean result = signVerifier.verify(sign);
		//System.out.println("result in rsaDecryptString :"+result); 
		
		return result;
	}
	
}
