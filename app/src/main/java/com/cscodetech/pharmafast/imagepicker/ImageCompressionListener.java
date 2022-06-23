package com.cscodetech.pharmafast.imagepicker;

public interface ImageCompressionListener {
    void onStart();

    void onCompressed(String filePath);
}
