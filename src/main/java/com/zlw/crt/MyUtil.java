package com.zlw.crt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

public class MyUtil {
	public static void openCRT(String scriptName,String path,String address) {
		Runtime rn = Runtime.getRuntime();
		Process p = null;
		String command = new String();
//		  String abPath = this.getClass().getResource("/scripts/uecallp.vbs").getFile();

		command = path + " /SCRIPT C:/CRT/" + scriptName +" /T /SSH2 /L root /PASSWORD 111111 " + address;
		try {
			p = rn.exec(command);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}


	public static void createTxt() throws IOException, InterruptedException{
		String path="C:/CRT/CrtPath.txt";
		String path1="C:/CRT/logPath.txt";
		String path2="C:/CRT/addressPath.txt";
		File src =new File("C:/CRT/scripts");
		if(!src.exists()){
			src.mkdirs();
		}
		File src1 =new File(path);
		if(!src1.exists()){
			src1.createNewFile();
		}
		File src2 =new File(path1);
		if(!src2.exists()){
			src2.createNewFile();
		}
		File src3 =new File(path2);
		if(!src3.exists()){
			src3.createNewFile();
		}
	}

	public static void input(String path,String str) {
		File dest =new File(path);
		OutputStream os =null;
		try {
			os =new FileOutputStream(dest,false);
			byte[] data =str.getBytes();
			os.write(data,0,data.length);

			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("文件未找到");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件写出失败");
		}finally{
			try {
				if (null != os) {
					os.close();
				}
			} catch (Exception e2) {
				System.out.println("关闭输出流失败");
			}
		}
	}

	public static String output(String path) {
		File src =new File(path);
		Reader reader =null;
		try {
			reader =new FileReader(src);
			char[] flush =new char[1024];
			int len =0;
			while(-1!=(len=reader.read(flush))){
				String str =new String(flush,0,len);
				return str;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("源文件不存在");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件读取失败");
		}finally{
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}
}
