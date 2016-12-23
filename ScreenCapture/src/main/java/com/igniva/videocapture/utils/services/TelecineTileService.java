package com.igniva.videocapture.utils.services;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.google.android.gms.analytics.HitBuilders;
import com.igniva.videocapture.ui.activities.ShortcutLaunchActivity;
import com.igniva.videocapture.ui.activities.ShortcutLaunchActivity;
import com.igniva.videocapture.utils.Analytics;
import com.igniva.videocapture.BaseApplication;

import javax.inject.Inject;
import timber.log.Timber;

@TargetApi(Build.VERSION_CODES.N) // Only created on N+
public final class TelecineTileService extends TileService {
  @Inject
  Analytics analytics;

  @Override public void onCreate() {
    super.onCreate();
    ((BaseApplication) getApplication()).injector().inject(this);
  }

  @Override public void onClick() {
    startActivity(ShortcutLaunchActivity.createQuickTileIntent(this));
  }

  @Override public void onStartListening() {
    Timber.i("Quick tile started listening");
    Tile tile = getQsTile();
    tile.setState(Tile.STATE_ACTIVE);
    tile.updateTile();
  }

  @Override public void onStopListening() {
    Timber.i("Quick tile stopped listening");
  }

  @Override public void onTileAdded() {
    Timber.i("Quick tile added");
    analytics.send(new HitBuilders.EventBuilder() //
        .setCategory(Analytics.CATEGORY_QUICK_TILE)
        .setAction(Analytics.ACTION_QUICK_TILE_ADDED)
        .build());
  }

  @Override public void onTileRemoved() {
    Timber.i("Quick tile removed");
    analytics.send(new HitBuilders.EventBuilder() //
        .setCategory(Analytics.CATEGORY_QUICK_TILE)
        .setAction(Analytics.ACTION_QUICK_TILE_ADDED)
        .build());
  }
}
