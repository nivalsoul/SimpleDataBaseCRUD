package com.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

/**
 * 利用JSch包实现SFTP下载、上传文件
 * @author wlxu
 *
 */
public class SFTPFileUpload {
	private String ip;
	private int port;
	private String user;
	private String pwd;
	
	public  SFTPFileUpload(String ip, int port, String user, String pwd) {
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
	}
	
	/**
	 * 利用JSch包实现SFTP下载、上传文件
	 * @param srcFile 源文件
	 * @param dstFile 目标文件
	 */
	public void sshSftp( String srcFile, String dstFile) throws Exception{
		Session session = null;
		Channel channel = null;

		JSch jsch = new JSch();
		
		if(port <=0){
			//连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		}else{
			//采用指定的端口连接服务器
			session = jsch.getSession(user, ip ,port);
		}

		//如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}
		
		//设置登陆主机的密码
		session.setPassword(pwd);//设置密码   
		//设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		//设置登陆超时时间   
		session.connect(30000);
			
		try {
			//创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;		
			
			/*//进入服务器指定的文件夹
			sftp.cd(directory);
			//列出服务器指定的文件列表
			Vector v = sftp.ls("*.txt");
			for(int i=0;i<v.size();i++){
				System.out.println(v.get(i));
			}*/
			
			/**
			 * 以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
			 */
			/*
			OutputStream outstream = sftp.put(dstFile,ChannelSftp.OVERWRITE);
			InputStream instream = new FileInputStream(srcFile);
			byte b[] = new byte[1024*256];
			int n;
		    while ((n = instream.read(b)) != -1) {
		    	outstream.write(b, 0, n);
		    }
		    outstream.flush();
		    outstream.close();
		    instream.close();*/
			
			/**
			 * 代码段2
			 * 直接将本地文件名为srcFile的文件上传到目标服务器，目标文件名为dstFile
	         * （注：使用这个方法时，dstFile可以是目录，当dstFile是目录时，上传后的目标文件名将与srcFile文件名相同）
			 */
			sftp.put(srcFile, dstFile, ChannelSftp.OVERWRITE); 
			
	        /**
	         * 代码段3
	         * 将本地文件名为srcFile的文件输入流上传到目标服务器，目标文件名为dstFile
	         */
			//sftp.put(new FileInputStream(srcFile), dstFile, ChannelSftp.OVERWRITE); 
			
			sftp.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}
	
	/**
	 * 从服务器下载一个文件到本地
	 * @param srcFile
	 * @param dstFile
	 * @throws Exception
	 */
	public void downloadFile(String srcFile, String dstFile) throws Exception {
		Session session = null;
		Channel channel = null;
		JSch jsch = new JSch();
		if(port <=0){//连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		}else{//采用指定的端口连接服务器
			session = jsch.getSession(user, ip ,port);
		}
		if (session == null) {
			throw new Exception("session is null");
		}
		
		//设置登陆主机的密码
		session.setPassword(pwd);//设置密码   
		//设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		//设置登陆超时时间   
		session.connect(30000);
		try {
			//创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;		
			
		/*	InputStream instream = sftp.get(srcFile,ChannelSftp.OVERWRITE);
			OutputStream outstream = new FileOutputStream(dstFile);
			byte b[] = new byte[1024*256];
			int n;
		    while ((n = instream.read(b)) != -1) {
		    	outstream.write(b, 0, n);
		    }
		    instream.close();
		    outstream.flush();
		    outstream.close();*/
		    
	        
	        SftpATTRS attr = sftp.stat(srcFile);
	        OutputStream out = new FileOutputStream(dstFile);
	        sftp.get(srcFile, dstFile); // 代码段1
		    

			sftp.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}
}
