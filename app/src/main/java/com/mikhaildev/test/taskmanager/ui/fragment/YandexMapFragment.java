package com.mikhaildev.test.taskmanager.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mikhaildev.test.taskmanager.R;
import com.mikhaildev.test.taskmanager.model.Task;
import com.mikhaildev.test.taskmanager.ui.activity.DataListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;


public class YandexMapFragment extends BaseFragment implements MapListener, OnBalloonListener {

    @Bind(R.id.map) MapView mMapView;
    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private Overlay tasksOverlay;

    private Map<Integer, OverlayItem> mMarkers = new HashMap<>();
    private DataListener mDataCallback;

    public YandexMapFragment() {

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDataCallback = (DataListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DataListener");
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.f_yandex_map;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        prepareMap();

        Task[] tasks = mDataCallback.getTasks();
        if (tasks!=null && tasks.length>0)
            updateMarkers(tasks);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDataCallback = null;
    }

    @Override
    public void updateMarkers(Task[] newTasks) {
        if (mMarkers!=null && mMarkers.size()>0) {
            removeOldMarkers(newTasks);
        }
        for (int i = 0; i < newTasks.length; i++) {
            updateMarker(newTasks[i]);
        }
        setZoomSpan();
    }

    private void updateMarker(Task task) {
        if (mMarkers.containsKey(task.getId())) {
            OverlayItem overlayItem = mMarkers.get(task.getId());
            overlayItem.getBalloonItem().setText(task.getTitle());
        } else {
            GeoPoint geoPoint = new GeoPoint(task.getLocation().getLat(), task.getLocation().getLon());
            OverlayItem overlayItem = new OverlayItem(geoPoint, ContextCompat.getDrawable(getActivity(), R.drawable.pin)); // getDrawable(resId); deprecated
            BalloonItem balloonItem = new BalloonItem(getActivity(), geoPoint);
            balloonItem.setText(task.getTitle());
            balloonItem.setOnBalloonListener(this);
            overlayItem.setBalloonItem(balloonItem);
            tasksOverlay.addOverlayItem(overlayItem);
            mOverlayManager.addOverlay(tasksOverlay);
            mMarkers.put(task.getId(), overlayItem);
        }
    }

    private void removeOldMarkers(Task[] tasks) {
        for (Map.Entry<Integer, OverlayItem> entry : mMarkers.entrySet()) {
            int overlayItemKey = entry.getKey();
            boolean removed = true;
            for (int i = 0; i < tasks.length; i++) {
                if (tasks[i].getId()==overlayItemKey) {
                    removed = false;
                    break;
                }
            }
            if (removed)
                tasksOverlay.removeOverlayItem(entry.getValue());
        }
    }

    private void setZoomSpan(){
        List<OverlayItem> list = tasksOverlay.getOverlayItems();
        double maxLat, minLat, maxLon, minLon;
        maxLat = maxLon = Double.MIN_VALUE;
        minLat = minLon = Double.MAX_VALUE;
        for (int i = 0; i < list.size(); i++){
            GeoPoint geoPoint = list.get(i).getGeoPoint();
            double lat = geoPoint.getLat();
            double lon = geoPoint.getLon();

            maxLat = Math.max(lat, maxLat);
            minLat = Math.min(lat, minLat);
            maxLon = Math.max(lon, maxLon);
            minLon = Math.min(lon, minLon);
        }
        mMapController.setZoomToSpan(maxLat - minLat, maxLon - minLon);
        mMapController.setPositionAnimationTo(new GeoPoint((maxLat + minLat)/2, (maxLon + minLon)/2));
    }

    private void prepareMap() {
        mMapController = mMapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        tasksOverlay = new Overlay(mMapController);
        mMapView.showZoomButtons(true);
        mMapView.showFindMeButton(true);
        mMapView.showJamsButton(false);
    }

    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view) {
        mDataCallback.showTaskDetails(getBalloonTaskId(balloonItem.getOverlayItem()));
    }

    @Override
    public void onBalloonShow(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonHide(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationStart(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem balloonItem) {

    }

    private int getBalloonTaskId(OverlayItem overlayItem) {
        for (Map.Entry<Integer, OverlayItem> entry : mMarkers.entrySet()) {
            OverlayItem overlayItemValue = entry.getValue();
            if (overlayItemValue==overlayItem) {
                return entry.getKey();
            }
        }
        return Integer.MIN_VALUE;
    }
}
