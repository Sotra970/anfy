package anfy.com.anfy.Service;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created by ahmed on 15/11/2017.
 */

public class PDFProgressRequestBody extends ProgressRequestBody {

    public PDFProgressRequestBody(File file, UploadCallbacks listener, int current, int total) {
        super(file, listener, current, total);
    }

    public PDFProgressRequestBody(File file, UploadCallbacks listener) {
        super(file, listener);
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/pdf");
    }
}
