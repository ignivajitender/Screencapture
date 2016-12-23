package com.igniva.videocapture;

import android.app.Application;
import com.bugsnag.android.BeforeNotify;
import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.Error;
import com.igniva.videocapture.utils.DaggerTelecineComponent;
import com.igniva.videocapture.utils.TelecineModule;
import com.igniva.videocapture.utils.BugsnagTree;
import com.igniva.videocapture.utils.TelecineComponent;

import timber.log.Timber;

public final class BaseApplication extends Application {


  private TelecineComponent telecineComponent;

  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      Bugsnag.init(this, BuildConfig.BUGSNAG_KEY);
      Bugsnag.setReleaseStage(BuildConfig.BUILD_TYPE);
      Bugsnag.setProjectPackages("com.igniva.videocapture");

      final BugsnagTree tree = new BugsnagTree();
      Bugsnag.getClient().beforeNotify(new BeforeNotify() {
        @Override public boolean run(Error error) {
          tree.update(error);
          return true;
        }
      });

      Timber.plant(tree);
    }

    telecineComponent = DaggerTelecineComponent.builder()
        .telecineModule(new TelecineModule(this))
        .build();
  }

  public final TelecineComponent injector() {
    return telecineComponent;
  }
}
