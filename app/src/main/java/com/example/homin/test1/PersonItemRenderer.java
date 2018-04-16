package com.example.homin.test1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class PersonItemRenderer extends DefaultClusterRenderer<ClusteringMarker> {
    Context context;
    GoogleMap googleMap;
    public PersonItemRenderer(Context context, GoogleMap map, ClusterManager<ClusteringMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.googleMap = map;
    }


    @Override
    protected void onBeforeClusterItemRendered(ClusteringMarker item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        IconGenerator iconFactory = new IconGenerator(context);

        markerOptions.title(item.getTitle());
//        markerOptions.snippet();
//
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(),R.drawable.sample_person,options);
//
//       Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap,128,128,false);
//        options.inSampleSize = calculateInSampleSize(options,100,100);
//
//        options.inJustDecodeBounds = false;
//        bitmap = BitmapFactory.decodeResource(this.context.getResources(),R.drawable.sample_person,options);
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//
//
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("hello")));

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(decodeSampledBitmapFromResource(this.context.getResources(),R.drawable.sample_person, 35, 35)));


    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }


    @Override
    protected void onBeforeClusterRendered(Cluster<ClusteringMarker> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusteringMarker> cluster) {
        return super.shouldRenderAsCluster(cluster);
    }
}
