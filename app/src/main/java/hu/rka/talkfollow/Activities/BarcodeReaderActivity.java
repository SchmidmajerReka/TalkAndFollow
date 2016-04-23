package hu.rka.talkfollow.Activities;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import hu.rka.talkfollow.R;

/**
 * Created by Réka on 2016.01.13..
 */
public class BarcodeReaderActivity extends CaptureActivity {
    @Override
    protected CompoundBarcodeView initializeContent() {
        setContentView(R.layout.activity_barcode_scan);
        return (CompoundBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }
}
