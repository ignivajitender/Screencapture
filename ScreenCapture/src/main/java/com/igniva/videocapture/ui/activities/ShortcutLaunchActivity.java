package com.igniva.videocapture.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.analytics.HitBuilders;
import com.igniva.videocapture.utils.Analytics;
import com.igniva.videocapture.utils.CaptureHelper;
import com.igniva.videocapture.BaseApplication;

import javax.inject.Inject;

public final class ShortcutLaunchActivity extends Activity {
  private static final String KEY_ACTION = "launch-action";

  public static Intent createQuickTileIntent(Context context) {
    Intent intent = new Intent(context, ShortcutLaunchActivity.class);
    intent.putExtra(KEY_ACTION, Analytics.ACTION_QUICK_TILE_LAUNCHED);
    return intent;
  }

  @Inject Analytics analytics;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((BaseApplication) getApplication()).injector().inject(this);

    String launchAction = getIntent().getStringExtra(KEY_ACTION);
    if (launchAction == null) {
      launchAction = Analytics.ACTION_SHORTCUT_LAUNCHED;
    }

    analytics.send(new HitBuilders.EventBuilder() //
        .setCategory(Analytics.CATEGORY_SHORTCUT)
        .setAction(launchAction)
        .build());

    CaptureHelper.fireScreenCaptureIntent(this, analytics);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (!CaptureHelper.handleActivityResult(this, requestCode, resultCode, data, analytics)) {
      super.onActivityResult(requestCode, resultCode, data);
    }
    finish();
  }

  @Override protected void onStop() {
    if (!isFinishing()) {
      finish();
    }
    super.onStop();
  }
}
