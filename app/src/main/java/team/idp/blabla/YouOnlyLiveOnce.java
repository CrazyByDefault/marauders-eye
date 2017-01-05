package team.idp.blabla;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class YouOnlyLiveOnce extends AppCompatActivity {

    private Button doneBt;
    private EditText rollSpeedInput, timeInput, yawInput, waitInput, yawInc, gazInput;
    private float time = 0, wait = 0, yawIncInt = 0;
    private int yaw = 0, roll = 0, gaz = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_only_live_once);

        timeInput = (EditText) findViewById(R.id.timeInput);
        yawInput = (EditText) findViewById(R.id.yawInput);
        rollSpeedInput = (EditText) findViewById(R.id.rollInput);
        gazInput = (EditText) findViewById(R.id.gazInput);
        waitInput = (EditText) findViewById(R.id.waitTimeInput);
        yawInc = (EditText) findViewById(R.id.yawStep);
        doneBt = (Button) findViewById(R.id.yoloDoneBt);

        SharedPreferences settings = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("Saved_time" ,time);
        editor.putFloat("Saved_wait" ,wait);
        editor.putInt("Saved_yaw" ,yaw);
        editor.putFloat("Saved_yawInc", yawIncInt);
        editor.putInt("Saved_roll", roll);
        editor.putInt("Saved_gaz", gaz);
        editor.commit();

        yawInput.setText(settings.getInt("Saved_yaw", 0));
        timeInput.setText(Float.toString(settings.getFloat("Saved_time", 0)));
        rollSpeedInput.setText(Float.toString(settings.getFloat("Saved_wait", 0)));
        waitInput.setText(Float.toString(settings.getFloat("Saved_yawInc", 0)));
        rollSpeedInput.setText(settings.getInt("Saved_roll", 0));
        gazInput.setText(settings.getInt("Saved_gaz", 0));

        doneBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = Float.parseFloat(timeInput.getText().toString());
                wait = Float.parseFloat(waitInput.getText().toString());
                yaw = Integer.parseInt(yawInput.getText().toString());
                yawIncInt = Float.parseFloat(yawInc.getText().toString());
                roll = Integer.parseInt(rollSpeedInput.getText().toString());
                gaz = Integer.parseInt(gazInput.getText().toString());

                Bundle args = new Bundle();

                args.putFloat("TIME", time);
                args.putFloat("WAIT", wait);
                args.putInt("YAW", yaw);
                args.putFloat("YAWINCR", yawIncInt);
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
