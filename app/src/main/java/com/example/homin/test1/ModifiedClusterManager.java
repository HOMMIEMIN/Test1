package com.example.homin.test1;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.Algorithm;

public class ModifiedClusterManager extends ClusterManager<ClusterItem> {
    public ModifiedClusterManager(Context context, GoogleMap map) {
        super(context, map);
//        Algorithm<ClusterItem> algorithm = this.getAlgorithm();
//        algorithm.removeItem();
//        algorithm.addItem();
    }

    @Override
    public void removeItem(ClusterItem item) {
        super.removeItem(item);
    }
}
