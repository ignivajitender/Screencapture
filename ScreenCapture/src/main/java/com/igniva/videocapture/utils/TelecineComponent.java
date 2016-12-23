package com.igniva.videocapture.utils;

import com.igniva.videocapture.ui.activities.MainActivity;
import com.igniva.videocapture.ui.activities.ShortcutConfigureActivity;
import com.igniva.videocapture.ui.activities.ShortcutLaunchActivity;
import com.igniva.videocapture.utils.services.TelecineService;
import com.igniva.videocapture.utils.services.TelecineTileService;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = TelecineModule.class)
public interface TelecineComponent {
    void inject(MainActivity activity);
    void inject(ShortcutConfigureActivity activity);
    void inject(ShortcutLaunchActivity activity);
    void inject(TelecineService service);
    void inject(TelecineTileService service);

}