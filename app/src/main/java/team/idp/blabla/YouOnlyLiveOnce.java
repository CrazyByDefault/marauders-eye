package team.idp.blabla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class YouOnlyLiveOnce extends AppCompatActivity {

    private Button doneBt;
    private EditText rollSpeedInput, timeInput, yawInput, waitInput, picWaitInput, gazInput;
    private float time, wait, picInt;
    private int yaw, roll, gaz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_only_live_once);

        timeInput = (EditText) findViewById(R.id.timeInput);
        yawInput = (EditText) findViewById(R.id.yawInput);
        rollSpeedInput = (EditText) findViewById(R.id.rollInput);
        gazInput = (EditText) findViewById(R.id.gazInput);
        waitInput = (EditText) findViewById(R.id.waitTimeInput);
        picWaitInput = (EditText) findViewById(R.id.shutterInput);
        doneBt = (Button) findViewById(R.id.yoloDoneBt);

        doneBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = Float.parseFloat(timeInput.getText().toString());
                wait = Float.parseFloat(waitInput.getText().toString());
                yaw = Integer.parseInt(yawInput.getText().toString());
                picInt = Float.parseFloat(picWaitInput.getText().toString());
                roll = Integer.parseInt(rollSpeedInput.getText().toString());
                gaz = Integer.parseInt(gazInput.getText().toString());

                Bundle args = new Bundle();

                args.putFloat("TIME", time);
                args.putFloat("WAIT", wait);
                args.putInt("YAW", yaw);
                args.putFloat("SHUTTER", picInt);
                args.putInt("ROLL", roll);
                args.putInt("GAZ", gaz);

                Intent byebye = new Intent();
                byebye.putExtras(args);

                setResult(0, byebye);
                finish();
            }
        });

    }


}
