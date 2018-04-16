package com.example.homin.test1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

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

        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.title("item");
        markerOptions.snippet("snippet");


        Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(),R.drawable.sample_person);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap,100,100,false);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));



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
