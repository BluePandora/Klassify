package com.fedorvlasov.lazylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.betelguese.shoppingapploginscreen.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
	private final boolean	             canScale;
	MemoryCache	                         memoryCache	= new MemoryCache();
	FileCache	                         fileCache;
	private final Map<ImageView, String>	imageViews	= Collections
	                                                           .synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService	                     executorService;
	Handler	                             handler	   = new Handler();	                                           // handler
	private Context	                     context;
	
	public ImageLoader(Context context) {
		canScale = false;
		this.context = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}
	
	public ImageLoader(Context context, boolean canScale) {
		this.canScale = canScale;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}
	
	final int	stub_id	= R.drawable.product;
	
	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			Log.d("Image", "Image loaded:yes");
		} else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}
	
	public void DisplayImage(String url, ImageView imageView,
	        ProgressBar progressBar) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		
		imageView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		
		if (bitmap != null) {
			/*
			 * imageView.setImageBitmap(bitmap);
			 * imageView.setVisibility(View.VISIBLE);
			 * progressBar.setVisibility(View.GONE);
			 */
			queuePhoto(bitmap, imageView, progressBar);
			imageView.setImageResource(stub_id);
		} else {
			queuePhoto(url, imageView, progressBar);
			imageView.setImageResource(stub_id);
		}
	}
	
	public void DisplayImage(String url, ImageView imageView,
	        ImageView imageView2) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		
		imageView.setVisibility(View.GONE);
		imageView2.setVisibility(View.VISIBLE);
		
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			imageView.setVisibility(View.VISIBLE);
			imageView2.setVisibility(View.GONE);
		} else {
			queuePhoto(url, imageView, imageView2);
			imageView.setImageResource(stub_id);
		}
	}
	
	public Bitmap getMyImage(String url) {
		Bitmap bitmap = null;
		bitmap = memoryCache.get(url);
		if (bitmap == null) {
			bitmap = getBitmap(url);
		}
		return bitmap;
	}
	
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}
	
	private void queuePhoto(String url, ImageView imageView,
	        ProgressBar progressBar) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, progressBar);
		executorService.submit(new PhotosLoader(p));
	}
	
	private void queuePhoto(Bitmap bmp, ImageView imageView,
	        ProgressBar progressBar) {
		PhotoToLoad p = new PhotoToLoad(bmp, imageView, progressBar);
		executorService.submit(new PhotosLoader(p));
	}
	
	private void queuePhoto(String url, ImageView imageView,
	        ImageView imageView2) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, imageView2);
		executorService.submit(new PhotosLoader(p));
	}
	
	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);
		
		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;
		
		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
			        .openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}
	
	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();
			
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true && canScale) {
				if (width_tmp / 2 < REQUIRED_SIZE
				        || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return null;
	}
	
	// Task for the queue
	private class PhotoToLoad {
		public String		url;
		public Bitmap		bmp;
		public ImageView	imageView;
		public ImageView	imageView2;
		public ProgressBar	progressBar;
		
		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
		
		public PhotoToLoad(String u, ImageView i, ProgressBar i2) {
			url = u;
			imageView = i;
			progressBar = i2;
		}
		
		public PhotoToLoad(Bitmap u, ImageView i, ProgressBar i2) {
			bmp = u;
			imageView = i;
			progressBar = i2;
		}
		
		public PhotoToLoad(String u, ImageView i, ImageView i2) {
			url = u;
			imageView = i;
			imageView2 = i2;
		}
	}
	
	class PhotosLoader implements Runnable {
		PhotoToLoad	photoToLoad;
		
		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}
		
		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp;
				if (photoToLoad.bmp != null)
					bmp = photoToLoad.bmp;
				else {
					bmp = getBitmap(photoToLoad.url);
					memoryCache.put(photoToLoad.url, bmp);
				}
				
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}
	
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (photoToLoad.bmp == null
		        && (tag == null || !tag.equals(photoToLoad.url)))
			return true;
		return false;
	}
	
	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap		bitmap;
		PhotoToLoad	photoToLoad;
		
		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}
		
		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bitmap);
				if (photoToLoad.progressBar != null) {
					photoToLoad.imageView.setVisibility(View.VISIBLE);
					photoToLoad.progressBar.setVisibility(View.GONE);
				}
				if (photoToLoad.imageView2 != null) {
					photoToLoad.imageView.setVisibility(View.VISIBLE);
					photoToLoad.imageView2.setVisibility(View.GONE);
				}
			}
		}
	}
	
	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}
}
