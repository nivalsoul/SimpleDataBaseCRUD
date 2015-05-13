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
 * Title: DES �ӽ����㷨��
 * Description: DES �ӽ����㷨
 * Copyright: Copyright (c) 2013
 * Company: 30san
 * @author xuwl fengsl
 */
public class DES {
	private static String strDefaultKey = "sstcd_youth1";// Ĭ����Կ

	private Cipher encryptCipher = null;

	private Cipher decryptCipher = null;
	
    private static String Algorithm = "DES";//���� �����㷨,���� DES,DESede,Blowfish

	/**
	 * ��byte����ת��Ϊ��ʾ16����ֵ���ַ����� �磺byte[]{8,18}ת��Ϊ��0813�� ��public static byte[]
	 * hexStr2ByteArr(String strIn) ��Ϊ�����ת������
	 * 
	 * @param arrB
	 *            ��Ҫת����byte����
	 * @return ת������ַ���
	 * @throws Exception
	 *             �������������κ��쳣�������쳣ȫ���׳�
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// ÿ��byte�������ַ����ܱ�ʾ�������ַ����ĳ��������鳤�ȵ�����
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// �Ѹ���ת��Ϊ����
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// С��0F������Ҫ��ǰ�油0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * ����ʾ16����ֵ���ַ���ת��Ϊbyte���飬 ��public static String byteArr2HexStr(byte[] arrB)
	 * ��Ϊ�����ת������
	 * 
	 * @param strIn
	 *            ��Ҫת�����ַ���
	 * @return ת�����byte����
	 * @throws Exception
	 *             �������������κ��쳣�������쳣ȫ���׳�
	 * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// �����ַ���ʾһ���ֽڣ������ֽ����鳤�����ַ������ȳ���2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * Ĭ�Ϲ��췽����ʹ��Ĭ����Կ
	 * 
	 * @throws Exception
	 */
	public DES() throws Exception {
		this(strDefaultKey);
	}

	/**
	 * ָ����Կ���췽��
	 * 
	 * @param strKey
	 *            ָ������Կ
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
	 * �����ֽ�����
	 * 
	 * @param arrB
	 *            ����ܵ��ֽ�����
	 * @return ���ܺ���ֽ�����
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * �����ַ���
	 * 
	 * @param strIn
	 *            ����ܵ��ַ���
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public String encrypt(String strIn) throws Exception {
		return byteArr2HexStr(encrypt(strIn.getBytes()));
	}

	/**
	 * �����ֽ�����
	 * 
	 * @param arrB
	 *            ����ܵ��ֽ�����
	 * @return ���ܺ���ֽ�����
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	/**
	 * �����ַ���
	 * 
	 * @param strIn
	 *            ����ܵ��ַ���
	 * @return ���ܺ���ַ���
	 * @throws Exception
	 */
	public String decrypt(String strIn) throws Exception {
		return new String(decrypt(hexStr2ByteArr(strIn)));
	}

	/**
	 * ��ָ���ַ���������Կ����Կ������ֽ����鳤��Ϊ8λ ����8λʱ���油0������8λֻȡǰ8λ
	 * 
	 * @param arrBTmp
	 *            ���ɸ��ַ������ֽ�����
	 * @return ���ɵ���Կ
	 * @throws java.lang.Exception
	 */
	private Key getKey(byte[] arrBTmp) throws Exception {
		// ����һ���յ�8λ�ֽ����飨Ĭ��ֵΪ0��
		byte[] arrB = new byte[8];

		// ��ԭʼ�ֽ�����ת��Ϊ8λ
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// ������Կ
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}
	
	
	/****************�ӽ����ļ�***********************/
	
	/**
     * �����ܳ�
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
            System.out.println("������Կ�ɹ�");
        } catch (Exception e) {
            System.out.println("������Կʧ��");
            e.printStackTrace();
        }

    }

    /**
     * �����ļ�
     *
     * @param sourcefile
     * @param outfile
     * @param key
     */
    public String DesFile(File sourcefile, File outfile, File key) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        String reStr = "���ܳɹ�";
        try {
            //�����ļ�
            System.out.println("�����ļ�");
            byte[] bt = getBytesFromFile(sourcefile);
            //�ܳ�
            System.out.println("ȡ���ܳ�");
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(key));
            SecretKey deskey = (SecretKey) in.readObject();
            in.close();
            //����
            System.out.println("�����ļ�");
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] cipherByte = c1.doFinal(bt);
            bt = null;
            //д���ļ�
            System.out.println("д���ļ�");
            writeBytesToFile(outfile, cipherByte);
        } catch (java.security.NoSuchAlgorithmException e1) {
            reStr = "����ʧ�ܣ�û�и��㷨";
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            reStr = "����ʧ��";
            e2.printStackTrace();
        } catch (java.lang.OutOfMemoryError e3) {
            reStr = "����ʧ�ܣ��ļ������ڴ����";
            e3.printStackTrace();
        } catch (java.io.IOException e4) {
            reStr = "�ļ���ȡ��д��ʧ��";
            e4.printStackTrace();
        } catch (java.lang.Exception e5) {
            e5.printStackTrace();
        }
            return reStr;
    }


    /**
     * �����ļ�
     *
     * @param sourcefile
     * @param outfile
     * @param key
     */
    public void OnDesFile(File sourcefile, File outfile, File key) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        try {
            //�����ļ�
            byte[] bt = getBytesFromFile(sourcefile);
            //�ܳ�
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(key));
            SecretKey deskey = (SecretKey) in.readObject();
            in.close();
            System.out.println(deskey.getEncoded().length);
            System.out.println(new String(deskey.getEncoded()));
            //����
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] cipherByte = c1.doFinal(bt);
            byte[] b = deskey.getEncoded();
            for (int k = 0; k < b.length; k++) {
                b[k] = 25;
                System.out.println(b[k]);
            }
            //д���ļ�
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
     * ��ȡ�ֽ�
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
            throw new IOException("���ܶ�ȡ�ļ��� " + file.getAbsolutePath() + file.getName());
        }
        is.close();
        return bytes;
    }

    /**
     * д���ļ�
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
			DES des = new DES();// Ĭ����Կ
			System.out.println("����ǰ���ַ���" + test);
			System.out.println("���ܺ���ַ���" + des.encrypt(test));
			System.out.println("���ܺ���ַ���" + des.decrypt(des.encrypt(test)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}