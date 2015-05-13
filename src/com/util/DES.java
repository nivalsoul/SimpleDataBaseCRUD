package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Title: DES 加解密算法类
 * Description: DES 加解密算法
 * Copyright: Copyright (c) 2013
 * Company: 30san
 * @author xuwl fengsl
 */
public class DES {
	private static String strDefaultKey = "sstcd_youth1";// 默认密钥

	private Cipher encryptCipher = null;

	private Cipher decryptCipher = null;
	
    private static String Algorithm = "DES";//定义 加密算法,可用 DES,DESede,Blowfish

	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * 默认构造方法，使用默认密钥
	 * 
	 * @throws Exception
	 */
	public DES() throws Exception {
		this(strDefaultKey);
	}

	/**
	 * 指定密钥构造方法
	 * 
	 * @param strKey
	 *            指定的密钥
	 * @throws Exception
	 */
	public DES(String strKey) throws Exception {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key = getKey(strKey.getBytes());

		encryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);

		decryptCipher = Cipher.getInstance("DES");
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	/**
	 * 加密字节数组
	 * 
	 * @param arrB
	 *            需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public String encrypt(String strIn) throws Exception {
		return byteArr2HexStr(encrypt(strIn.getBytes()));
	}

	/**
	 * 解密字节数组
	 * 
	 * @param arrB
	 *            需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public String decrypt(String strIn) throws Exception {
		return new String(decrypt(hexStr2ByteArr(strIn)));
	}

	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
	 * 
	 * @param arrBTmp
	 *            构成该字符串的字节数组
	 * @return 生成的密钥
	 * @throws java.lang.Exception
	 */
	private Key getKey(byte[] arrBTmp) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];

		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}
	
	
	/****************加解密文件***********************/
	
	/**
     * 创建密匙
     *
     * @param saveFile
     */
    public void CreateKey(File saveFile) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
            SecretKey deskey = keygen.generateKey();
            System.out.println("format:" + deskey.getFormat());
            System.out.println("format:" + deskey.getAlgorithm());
            System.out.println("format:" + String.valueOf(deskey.getEncoded()));
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(saveFile));
            out.writeObject(deskey);
            out.close();
            System.out.println("生成密钥成功");
        } catch (Exception e) {
            System.out.println("生成密钥失败");
            e.printStackTrace();
        }

    }

    /**
     * 加密文件
     *
     * @param sourcefile
     * @param outfile
     * @param key
     */
    public String DesFile(File sourcefile, File outfile, File key) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        String reStr = "加密成功";
        try {
            //读入文件
            System.out.println("读入文件");
            byte[] bt = getBytesFromFile(sourcefile);
            //密匙
            System.out.println("取得密匙");
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(key));
            SecretKey deskey = (SecretKey) in.readObject();
            in.close();
            //加密
            System.out.println("加密文件");
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] cipherByte = c1.doFinal(bt);
            bt = null;
            //写入文件
            System.out.println("写入文件");
            writeBytesToFile(outfile, cipherByte);
        } catch (java.security.NoSuchAlgorithmException e1) {
            reStr = "加密失败，没有该算法";
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            reStr = "加密失败";
            e2.printStackTrace();
        } catch (java.lang.OutOfMemoryError e3) {
            reStr = "加密失败，文件过大，内存溢出";
            e3.printStackTrace();
        } catch (java.io.IOException e4) {
            reStr = "文件读取或写入失败";
            e4.printStackTrace();
        } catch (java.lang.Exception e5) {
            e5.printStackTrace();
        }
            return reStr;
    }


    /**
     * 解密文件
     *
     * @param sourcefile
     * @param outfile
     * @param key
     */
    public void OnDesFile(File sourcefile, File outfile, File key) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        try {
            //读入文件
            byte[] bt = getBytesFromFile(sourcefile);
            //密匙
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(key));
            SecretKey deskey = (SecretKey) in.readObject();
            in.close();
            System.out.println(deskey.getEncoded().length);
            System.out.println(new String(deskey.getEncoded()));
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] cipherByte = c1.doFinal(bt);
            byte[] b = deskey.getEncoded();
            for (int k = 0; k < b.length; k++) {
                b[k] = 25;
                System.out.println(b[k]);
            }
            //写入文件
            writeBytesToFile(outfile, cipherByte);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
    }

    /**
     * 读取字节
     *
     * @param file
     * @return
     * @throws IOException
     */
    private byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        //Read in bytes
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        //Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("不能读取文件： " + file.getAbsolutePath() + file.getName());
        }
        is.close();
        return bytes;
    }

    /**
     * 写入文件
     *
     * @param file
     * @param bytes
     * @return
     * @throws IOException
     */
    private boolean writeBytesToFile(File file, byte[] bytes) throws IOException {
        try {
            if (!file.exists()) file.createNewFile();
            FileOutputStream fs = new FileOutputStream(file);
            fs.write(bytes);
            fs.close();
            return true;
        } catch (IOException e) {
            throw e;
        }
    }

	public static void main(String[] args) {
		try {
			String test = "fdasf";
			DES des = new DES();// 默认密钥
			System.out.println("加密前的字符：" + test);
			System.out.println("加密后的字符：" + des.encrypt(test));
			System.out.println("解密后的字符：" + des.decrypt(des.encrypt(test)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}