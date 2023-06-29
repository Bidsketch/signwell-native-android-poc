package com.signwell.androiddemo;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileUtils
{
	static String getFileName(Uri uri)
	{
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = App._instance.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                fileName = cursor.getString(nameIndex);
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }


	static byte[] getBytesFromInputStream(InputStream inputStream) throws IOException
	{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, bytesRead);
        }
        return byteBuffer.toByteArray();
    }


	static String getFileBase64Content(Uri uri)
	{
        try {
            InputStream inputStream = App._instance.getContentResolver().openInputStream(uri);
            byte[] fileBytes = getBytesFromInputStream(inputStream);
            return Base64.encodeToString(fileBytes, Base64.NO_WRAP); // .NO_WRAP instead of .DEFAULT for the SignWell API
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
