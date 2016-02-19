package com.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BTDissconnectBCast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		Toast.makeText(context, dev.getName()+" is DISconnected", 500).show();
		Intent mapIntent = new Intent(context, MapActivityCls.class);
		mapIntent.putExtra("btAddress", dev.getAddress());
		mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mapIntent);
	}

}
