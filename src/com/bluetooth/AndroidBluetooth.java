package com.bluetooth;





import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AndroidBluetooth extends Activity {
	
	//private static final int REQUEST_ENABLE_BT = 1;
	Integer REQ_BT_ENABLE=1;
	CheckBox cboxEnable, CboxDiscoverable;
	public static ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	public static BluetoothServerSocket serverSocket;
	public static BluetoothSocket socket;
	public static UUID uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static DataHelper dh;
    ListView listDevicesFound;
	Button btnScanDevice;
	TextView stateBluetooth;
	Drawable bon, boff;

   	Button MakeDiscoverable_btn,paired;
    CheckBox enable;
    CheckBox discover;
    BluetoothAdapter bluetoothAdapter;
	ArrayAdapter<String> btArrayAdapter;
	ToggleButton toggle;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        bon = this.getResources().getDrawable(R.drawable.bton);
        boff = this.getResources().getDrawable(R.drawable.btoff);
        toggle= (ToggleButton) findViewById(R.id.toggle);
        btnScanDevice = (Button)findViewById(R.id.scandevice);
        dh = new DataHelper(getApplicationContext());
        
        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      
        
        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        //enable=(CheckBox)findViewById(R.id.cboxEnable);
        
        
        discover=(CheckBox)findViewById(R.id.cBoxDisoverable);
        btArrayAdapter = new ArrayAdapter<String>(AndroidBluetooth.this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);
        
        btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener); //Discovery of devices   
        
        registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND)); 
        
        
 // -------------Select Remote device from List & make connection with Remote device-------------- 
        
        listDevicesFound.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Toast.makeText(getApplicationContext(), "selected " + pos, 500).show();
				
				try {
					BluetoothDevice dev = devices.get(pos);
					BluetoothSocket soc = dev.createRfcommSocketToServiceRecord(uuid);
					Toast.makeText(getApplicationContext(),soc.getRemoteDevice().getAddress().toString(), 500).show();
					soc.connect();
					
					Toast.makeText(getApplicationContext(), "Connected To Bluetooth Device.", 500).show();
					Intent in = new Intent(getApplicationContext(), Sample.class);
					startActivity(in);
				} catch (IOException e) {
					//error();
					e.printStackTrace();
				}
			}
        });
        
        
       
        
        //------------------Make local device enable or disable ------------------
        
        /*enable.setOnCheckedChangeListener(new OnCheckedChangeListener() {

        	 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        	 // TODO Auto-generated method stub
        	 if(buttonView.isChecked())
        	 {
        	 if(bluetoothAdapter==null)
        	 {
        	 Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
        	 }
        	 else
        	 {
        	 if(!bluetoothAdapter.isEnabled())
        	 {
        	 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
        	 startActivityForResult(enableBtIntent, REQ_BT_ENABLE);
        	 Toast.makeText(getApplicationContext(), "Enabling Bluetooth!!", Toast.LENGTH_LONG).show();
        	 }
        	 }
        	 }
        	 else
        	 {
        	 Toast.makeText(getApplicationContext(), "Disabling Bluetooth!!", Toast.LENGTH_LONG).show();
        	 bluetoothAdapter.disable();
        	 }
        	 }
        	 });
*/
toggle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (toggle.isChecked())
				{
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
		        	 startActivityForResult(enableBtIntent, REQ_BT_ENABLE);
		        	 //Toast.makeText(getApplicationContext(), "Enabling Bluetooth!!...", Toast.LENGTH_LONG).show();
		        	 toggle.setBackgroundDrawable(bon);
				}
				else{
					if(bluetoothAdapter!=null && bluetoothAdapter.isEnabled())
						bluetoothAdapter.disable();
		        	 toggle.setBackgroundDrawable(boff);

				}
				
				
			}
		});

   
       
        //-------------Make local device discoverable --------------
        
        discover.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.isChecked())
				{
				if(!bluetoothAdapter.isEnabled())
				{
					Toast.makeText(getApplicationContext(), "First Enable  your Bluetooth", Toast.LENGTH_LONG).show();	
				    buttonView.setChecked(false);
				}
				else{
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		        startActivityForResult(discoverableIntent,2);
		        
			        }
				}
				}
			
			
			
			
			
		});
          
        
        
    }
    
  //---------on activity result make connection with selected device --------
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	if(requestCode == REQ_BT_ENABLE){
    	if (resultCode == RESULT_OK){
    	Toast.makeText(getApplicationContext(), "BlueTooth is now Enabled", Toast.LENGTH_LONG).show();
    	}
    	 if(resultCode == RESULT_CANCELED){
    	Toast.makeText(getApplicationContext(), "Error occured while enabling.Leaving the application..", Toast.LENGTH_LONG).show();
    	 ///finish();
    	 }
    	 }
    	
    	if(requestCode == 2){
    		if(resultCode>0){
    			Toast.makeText(getApplicationContext(), "Now your device is discoverable..", Toast.LENGTH_LONG).show();
    		String name = "server";
    		
				final BluetoothServerSocket btServer;
				try {
					btServer = bluetoothAdapter.listenUsingRfcommWithServiceRecord(name, uuid);
			
    		
    		Thread acceptThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					BluetoothSocket sock=null;
					while(true){
					try {
						 sock = btServer.accept();
						//toDo();
					} catch (IOException e) {
						error();
						e.printStackTrace();
						break;
					}
					if(sock!=null){
						//toDo(sock);
						Intent in = new Intent(getApplicationContext(), Sample.class);
						startActivity(in);
						
						//break;
					}
					
			}
					
					
				}
				});
				
    		acceptThread.start();
    	} catch (IOException e1) {
			error();
			e1.printStackTrace();
		}	
    }
    }
    	
   }//onActivityREsult
    public void toDo(BluetoothSocket sock){
    	Toast.makeText(getApplicationContext(),"LOL", 500).show();
	}
    private void error() {
    	Toast.makeText(getApplicationContext(),"Error", 500).show();

	}
     
 //-----------------scan button---------
    
    private Button.OnClickListener btnScanDeviceOnClickListener
    = new Button.OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			btArrayAdapter.clear();
			bluetoothAdapter.startDiscovery();
		}};

	
    //------------Receiver listen for remote device & create list of remote device------
		
	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	            devices.add(device);
	            btArrayAdapter.notifyDataSetChanged();
	        }
			else{
				Toast.makeText(getApplicationContext(), "OOPSS!!Device not Found Try again..", Toast.LENGTH_LONG).show();
			}
		}};
		
		   @Override
			protected void onDestroy() {
				// TODO Auto-generated method stub
				super.onDestroy();
				unregisterReceiver(ActionFoundReceiver);
			}

		   // --------------------------------- 
/*		   @Override
		protected void onStop() {
			super.onStop();
			bluetoothAdapter.disable();
		}
		*/  
}// MAIN CLASS