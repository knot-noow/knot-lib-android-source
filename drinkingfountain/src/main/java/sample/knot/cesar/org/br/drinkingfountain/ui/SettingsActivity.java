package sample.knot.cesar.org.br.drinkingfountain.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sample.knot.cesar.org.br.drinkingfountain.R;
import sample.knot.cesar.org.br.drinkingfountain.util.PreferenceUtil;
import sample.knot.cesar.org.br.drinkingfountain.util.Util;

public class SettingsActivity extends Activity {

    private EditText mEdtEndPoint;
    private EditText mEdtUUID;
    private EditText mEdtToken;
    private Button mBtnSave;
    private CoordinatorLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    private void initView() {
        mEdtEndPoint = (EditText) findViewById(R.id.edt_end_point);
        mEdtUUID = (EditText) findViewById(R.id.edt_uuid_owner);
        mEdtToken = (EditText) findViewById(R.id.edt_token_owner);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.rootCoordinatorLayout);

        mBtnSave.setOnClickListener(saveListener);
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(iSCanSave()) {
                storeInformation();
                Intent homeActivity = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(homeActivity);
                finish();
            } else {
                Snackbar.make(mRootLayout, "Todos os campos devem ser preenchido", Snackbar.LENGTH_LONG);
            }
        }
    };

    private void storeInformation() {
        PreferenceUtil.getInstance().setEndPoint(mEdtEndPoint.getText().toString());
        PreferenceUtil.getInstance().setUuid(mEdtUUID.getText().toString());
        PreferenceUtil.getInstance().setToken(mEdtToken.getText().toString());
    }

    private boolean iSCanSave() {
        return mEdtEndPoint != null &&  !mEdtEndPoint.getText().equals(Util.EMPTY_STRING)  &&
                mEdtUUID != null && !mEdtUUID.equals(Util.EMPTY_STRING) &&
                mEdtToken != null && mEdtToken.equals(Util.EMPTY_STRING);
    }
}
