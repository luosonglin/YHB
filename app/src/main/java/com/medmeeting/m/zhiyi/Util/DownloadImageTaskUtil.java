package com.medmeeting.m.zhiyi.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 04/11/2017 11:09 AM
 * @describe ImageView加载URL图片
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class DownloadImageTaskUtil extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTaskUtil(ImageView bmImage) {
        this.bmImage = bmImage;
        Log.e("DownloadImageTaskUtil", " "+bmImage);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        Log.e("doInBackground", "Bitmap "+urldisplay);
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        Log.e("onPostExecute", " "+result);
    }
}
