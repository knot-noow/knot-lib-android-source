/*
 *
 *  Copyright (c) 2017, CESAR.
 *  All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the BSD license. See the LICENSE file for details.
 *
 */

package sample.knot.cesar.org.br.drinkingfountain.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

import br.org.cesar.knot.lib.util.LogLib;
import sample.knot.cesar.org.br.drinkingfountain.R;

public class AboutActivity extends AppCompatActivity {

    private static final String UTF_STRING_FORMAT_TYPE = "UTF-8";
    private static final String STRING_FORMAT_TEXT_HTML = "text/html";

    public static final String LICENSE_HTML = "drinkfountain_open_licenses.html";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        WebView licenseWebView = (WebView) findViewById(R.id.licenseMessage);


        AssetManager assetManager = getApplicationContext().getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open(LICENSE_HTML);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            licenseWebView.loadData(new String(buffer, UTF_STRING_FORMAT_TYPE), STRING_FORMAT_TEXT_HTML, UTF_STRING_FORMAT_TYPE);

            inputStream.close();
        } catch (IOException e) {
            LogLib.printD(String.format("File does not exists"));
        }

    }


}
