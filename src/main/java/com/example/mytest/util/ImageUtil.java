package com.example.mytest.util;

import android.content.Context;
import android.widget.ImageView;

import com.example.mytest.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by gaopeng on 2016/11/18.
 * ImageLoader图片加载工具类
 */
public class ImageUtil {


    /**
     * 使用方法：初始化工具类，直接调用“加载图片”的方法即可
     */
    public ImageUtil(Context context){
        // 创建DisplayImageOptions对象
        DisplayImageOptions defaulOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        // 创建ImageLoaderConfiguration对象
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaulOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // ImageLoader对象的配置
        ImageLoader.getInstance().init(configuration);
    }

    /**
     * 加载图片（注意，在对应的activity中onDestory方法调用loader.clearMemoryCache()清除缓存）
     *
     * @param imageLoader 使用  ImageLoader loader = ImageLoader.getInstance();初始化对象
     * @param url         图片地址
     * @param imageView   控件
     */
    public void loadImage(ImageLoader imageLoader , String url , ImageView imageView){
        // 创建DisplayImageOptions对象并进行相关选项配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.img_noread)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.img_read)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.recycler_footer_error)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                .build();// 创建DisplayImageOptions对象
        // 使用ImageLoader加载图片
        imageLoader.displayImage(url,imageView, options);
    }
}
