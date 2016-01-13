package hu.rka.talkfollow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
/**
 * Created by Réka on 2016.01.13..
 */
public class BarcodeReaderActivity extends CaptureActivity {
    @Override
    protected CompoundBarcodeView initializeContent() {
        setContentView(R.layout.activity_barcode_scan);
        return (CompoundBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
