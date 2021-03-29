package com.akhil.jauntparents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Bharath on 15/02/17.
 */

public class StartFirebaseAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(FirebaseBackgroundService.class.getName()));
    }
}