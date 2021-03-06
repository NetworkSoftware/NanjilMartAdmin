package com.rahul.media.imagemodule.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.appcompat.app.ActionBar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cache.ByteImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rahul.media.R;
import com.rahul.media.model.MediaObject;
import com.rahul.media.utils.MediaSingleTon;
import com.rahul.media.utils.MediaUtility;
import com.rahul.media.utils.SquareImageView;

import java.util.ArrayList;
import java.util.HashSet;


public class ImageGalleryGridAdapter extends BaseAdapter {

    private final ArrayList<MediaObject> mediaObjectArrayList;
    private final int pickCount;
    private final Context mContext;
    private final DisplayImageOptions options;
    private final ImageLoader imageLoader;
    private ByteImageLoader byteImageLoader;
    String saveDir;
    ActionBar actionBar;
    private String bucketTitle;

    public ImageGalleryGridAdapter(Context context, ArrayList<MediaObject> pickedImageBeans,
                                   String saveDir, int pickCount, ActionBar supportActionBar, String bucketTitle, ByteImageLoader byteImageLoader) {
        this.mContext = context;
        this.mediaObjectArrayList = pickedImageBeans;
        this.saveDir = saveDir;
        this.pickCount = pickCount;
        this.actionBar = supportActionBar;
        this.bucketTitle = bucketTitle;
        this.byteImageLoader = byteImageLoader;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));


        BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
        resizeOptions.inSampleSize = 3; // decrease size 3 times

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.placeholder_470x352)
                .showImageForEmptyUri(R.drawable.placeholder_470x352)
                .showImageOnFail(R.drawable.placeholder_470x352)
                .decodingOptions(resizeOptions)
                .cacheOnDisk(true).build();

    }

    HashSet<Integer> selectedPositions = new HashSet<>();

    @Override
    public int getCount() {
        return mediaObjectArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(mContext);
            holder = new ViewHolder();
            holder.position = position;
            convertView = vi.inflate(R.layout.list_image_item_gallery, null);
            holder.imgThumb = (SquareImageView) convertView.findViewById(R.id.imgThumbnail);
            holder.videoDuration = (TextView) convertView.findViewById(R.id.txtDuration);
            holder.selectIv = (CheckBox) convertView
                    .findViewById(R.id.imgQueueMultiSelected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mediaObjectArrayList.get(position).isSelected) {
            selectedPositions.add(position);
        } else
            selectedPositions.remove(position);

        byteImageLoader.DisplayImage(mediaObjectArrayList.get(position).getPath(), holder.imgThumb);
//        loadThumbnail(holder, position);

        holder.selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performCheck(holder, position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performCheck(holder, position);
            }
        });

        holder.selectIv.setChecked(mediaObjectArrayList.get(position).isSelected);

        return convertView;
    }

    private void loadThumbnail(ViewHolder holder, int position) {
        byte[] imageByte = MediaSingleTon.getInstance().getImageByte(mediaObjectArrayList.get(position).getPath());
        if (imageByte != null) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(imageByte)
                    .into(holder.imgThumb);
        } else {
            byte[] thumbnail = MediaUtility.getThumbnail(mediaObjectArrayList.get(position).getPath());
            if (thumbnail != null) {
                MediaSingleTon.getInstance().putImageByte(mediaObjectArrayList.get(position).getPath(), thumbnail);
                Glide.with(mContext)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .load(thumbnail)
                        .into(holder.imgThumb);
            } else {
                Glide.with(mContext)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(200, 200) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                                .centerCrop()) // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                        .asBitmap()
                        .load(Uri.parse("file://" + mediaObjectArrayList.get(position).getPath()))
                        .into(holder.imgThumb);

            }
        }
//        holder.imgThumb.setImageURI(Uri.parse("file://" + mediaObjectArrayList.get(position).getPath()));

    }

    private void performCheck(ViewHolder holder, int position) {
        if (selectedPositions.size() == pickCount && !mediaObjectArrayList.get(position).isSelected) {
            if (pickCount == 1)
                Toast.makeText(mContext, "You can select max " + pickCount + " image", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mContext, "You can select max " + pickCount + " images", Toast.LENGTH_SHORT).show();
        } else {
            if (!mediaObjectArrayList.get(position).isSelected) {
                selectedPositions.add(position);
            } else
                selectedPositions.remove(position);

            mediaObjectArrayList.get(position).isSelected = !mediaObjectArrayList.get(position).isSelected;
            setActionbarTitle(getCount());
        }
        holder.selectIv.setChecked(mediaObjectArrayList.get(position).isSelected);
    }

    private static class ViewHolder {
        CheckBox selectIv;
        /*SimpleDraweeView*/ SquareImageView imgThumb;
        TextView videoDuration;
        public int position;
    }

    private void setActionbarTitle(int total) {
        if (pickCount == 1)
            actionBar.setTitle(bucketTitle);
        else
            actionBar.setTitle(bucketTitle + " (" + selectedPositions.size() + "/" + String.valueOf(total) + ")");
    }
}