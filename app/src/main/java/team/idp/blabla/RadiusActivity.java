package team.idp.blabla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parrot.arsdk.arcommands.ARCOMMANDS_MINIDRONE_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;
import team.idp.blabla.MiniDrone;


public class RadiusActivity extends AppCompatActivity {

    private Button takeOff;
    private Button moveShakalaka;
    private float radius;
    private MiniDrone mMiniDrone;
    private TextView mBatteryLabel;
    private float time;
    private EditText timeEditText;
    private float acceleration = (float) 0.5858;
    private TextView status;

    private static double ultraImpNumThingy = 0.17431148549;


    private ProgressDialog mConnectionProgressDialog;
    private ProgressDialog mDownloadProgressDialog;

    private Runnable moveFront = new Runnable() {
        @Override
        public void run() {
            mMiniDrone.setPitch((byte) 50);
            mMiniDrone.setFlag((byte) 1);
            status.setText("Moving front");
        }
    };

    private Runnable moveBack = new Runnable() {
        @Override
        public void run() {
            mMiniDrone.setPitch((byte) -50);
            mMiniDrone.setFlag((byte) 1);
            status.setText("Moving back");
        }
    };

    private Runnable stopMoving = new Runnable() {
        @Override
        public void run() {
            mMiniDrone.setPitch((byte) 0);
            mMiniDrone.setFlag((byte) 0);
            mMiniDrone.setYaw((byte) 0);
            status.setText("Stopping motion");
        }
    };

    private Runnable takePicture = new Runnable() {
        @Override
        public void run() {
            mMiniDrone.takePicture();
            status.setText("Taking picture");
        }
    };

    private Runnable yawAntiClock = new Runnable() {
        @Override
        public void run() {
            mMiniDrone.setYaw((byte) -50);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);


        initIHM();


        Intent intent = getIntent();
        Bundle intentArgs = intent.getExtras();
        ARDiscoveryDeviceService service = intent.getParcelableExtra(MainActivity.EXTRA_DEVICE_SERVICE);
        radius = intentArgs.getFloat(MainActivity.RADIUS);
//        double timeSquaredDouble = (double) (2*radius)/acceleration;
        time = radius;
        status.setText(Float.toString(radius) + ", " + Float.toString(time));



        mMiniDrone = new MiniDrone(this, service);
        mMiniDrone.addListener(mMiniDroneListener);


    }


    @Override
    protected void onStart() {
        super.onStart();

        // show a loading view while the minidrone is connecting
        if ((mMiniDrone != null) && !(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(mMiniDrone.getConnectionState())))
        {
            mConnectionProgressDialog = new ProgressDialog(this, R.style.AppTheme);
            mConnectionProgressDialog.setIndeterminate(true);
            mConnectionProgressDialog.setMessage("Connecting ...");
            mConnectionProgressDialog.setCancelable(false);
            mConnectionProgressDialog.show();

            // if the connection to the MiniDrone fails, finish the activity
            if (!mMiniDrone.connect()) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMiniDrone != null)
        {
            mConnectionProgressDialog = new ProgressDialog(this, R.style.AppTheme);
            mConnectionProgressDialog.setIndeterminate(true);
            mConnectionProgressDialog.setMessage("Disconnecting ...");
            mConnectionProgressDialog.setCancelable(false);
            mConnectionProgressDialog.show();

            if (!mMiniDrone.disconnect()) {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onDestroy()
    {
        mMiniDrone.dispose();
        super.onDestroy();
    }

    private void initIHM(){

        status = (TextView) findViewById(R.id.statusText);
        timeEditText = (EditText) findViewById(R.id.timeInput);

        takeOff = (Button) findViewById(R.id.takeOffButton);
        takeOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (mMiniDrone.getFlyingState()) {
                    case ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                        mMiniDrone.takeOff();
                        break;
                    case ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                    case ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                        mMiniDrone.land();
                        break;
                    default:
                }
            }
        });

        moveShakalaka = (Button) findViewById(R.id.moveButton);

        moveShakalaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                time = Float.parseFloat(timeEditText.getText().toString());
                Handler maHandler = new Handler();
//
//                maHandler.post(moveFront);
//                maHandler.postDelayed(stopMoving, (int) time*1000);

                maHandler.post(takePicture);



            }
        });

        mBatteryLabel = (TextView) findViewById(R.id.batteryBla);

    }

    private final MiniDrone.Listener mMiniDroneListener = new MiniDrone.Listener() {
        @Override
        public void onDroneConnectionChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
            switch (state)
            {
                case ARCONTROLLER_DEVICE_STATE_RUNNING:
                    mConnectionProgressDialog.dismiss();
                    break;

                case ARCONTROLLER_DEVICE_STATE_STOPPED:
                    // if the deviceController is stopped, go back to the previous activity
                    mConnectionProgressDialog.dismiss();
                    finish();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onBatteryChargeChanged(int batteryPercentage) {
            mBatteryLabel.setText(String.format("%d%%", batteryPercentage));
        }

        @Override
        public void onPilotingStateChanged(ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
            switch (state) {
                case ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                    takeOff.setText("Take off");
                    takeOff.setEnabled(true);

                    break;
                case ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                case ARCOMMANDS_MINIDRONE_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                    takeOff.setText("Land");
                    takeOff.setEnabled(true);

                    break;
                default:
                    takeOff.setEnabled(false);

            }
        }

        @Override
        public void onPictureTaken(ARCOMMANDS_MINIDRONE_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
            //Log.i(TAG, "Picture has been taken");
        }

        @Override
        public void onMatchingMediasFound(int nbMedias) {

        }

        @Override
        public void onDownloadProgressed(String mediaName, int progress) {

        }

        @Override
        public void onDownloadComplete(String mediaName) {

        }
    };
}
