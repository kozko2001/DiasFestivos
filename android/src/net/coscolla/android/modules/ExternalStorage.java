package net.coscolla.android.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

public class ExternalStorage {

	
	public static Errorable<Integer> write(String text, String file, Context ctx)
	{
		return write(text.getBytes(), file, ctx);
	}
	
	public static Errorable<Integer> write(byte[] data, String file, Context ctx)
	{
		if( ! mountedWR() )
			return new Errorable<Integer>( new Exception("No possible to mount sd"));
		
		File f = new File(getExternalStorageAppFolder(ctx), file);
		
		
		OutputStream os;
		try {
			os = new FileOutputStream(f);
			os.write(data);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			 return new Errorable<Integer>(e);
		} catch (IOException e) {
			return new Errorable<Integer>(e);
		}
		
		return new Errorable<Integer>(1);

	}
	
	public static Errorable<String> readString(String file, Context ctx)
	{
		Errorable<byte[]> b = readBytes(file, ctx);
		if( ! b.getError() )
			return new Errorable<String>(new String(b.getData()));
		else
			return new Errorable<String>(b.getException());
	}
	
	public static  Errorable<byte[]> readBytes(String file, Context ctx)
	{
		// 1) Test if is mounted external storage
		if( ! mountedRO() )
			return new Errorable<byte[]>( new Exception("No possible to mount sd"));
		
		File f = new File(getExternalStorageAppFolder(ctx), file);
		
		if( !f.canRead() || ! f.exists() || ! f.isFile() )
			return new Errorable<byte[]>( new Exception("No possible to read the file..."));
		
		long length = f.length();
		byte[] b    = new byte[(int)length];
		
		
		
		 try {
			InputStream is = new FileInputStream(f);
			int offset = 0;
		    int numRead = 0;
		    while (offset < b.length
		           && (numRead=is.read(b, offset, b.length-offset)) >= 0) {
		        offset += numRead;
		    }
		    is.close();
		} catch (FileNotFoundException e) {
			return new Errorable<byte[]>( e);
		} catch (IOException e) {
			return new Errorable<byte[]>( e);
		}
		 
		return new Errorable<byte[]>( b );
	}
	
	
	
	private static boolean mountedRO()
	{
		boolean mounted    = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		return mounted || mountedWR();
	}
	
	private static  boolean mountedWR()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		
	}
	
	private static String getExternalStorageAppFolder(Context ctx)
	{
		String packageName = ctx.getPackageName();
		File externalPath = Environment.getExternalStorageDirectory();
		File appFiles = new File(externalPath.getAbsolutePath() +
		                         "/Android/data/" + packageName + "/");
		
		boolean t = false;
		if( ! appFiles.exists())
			t = appFiles.mkdirs();
		
		return appFiles.getAbsolutePath();
		
	}
	
	
}
