package me.skymc.taboocode4.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyUtils {
	
	private static FileInputStream in;
	private static FileOutputStream out;

	public static long Copy(File file1, File file2) throws IOException {

		if (!file1.exists()) {
			file1.createNewFile();
		}
		if (!file2.exists()) {
			file2.createNewFile();
		}
		
		long time = System.currentTimeMillis();
		
		in = new FileInputStream(file1);
		out = new FileOutputStream(file2);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		ByteBuffer b = null;
		
		Integer length = 2097152;
		
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();
				return System.currentTimeMillis()-time;
			}
			if ((inC.size() - inC.position()) < length) {
				length = (int) (inC.size()-inC.position());
			}
			else {
				length = 2097152;
			}
			
			b = ByteBuffer.allocateDirect(length);
			inC.read(b);
			b.flip();
			outC.write(b);
			outC.force(false);
		}
	}

}