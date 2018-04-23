package anfy.com.anfy.Service;


import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sotra on 5/10/2017.
 */
public interface UploadFileService {


    @Multipart
    @POST("upload")
    Call<ArrayList<String>> upload(
            @Part ArrayList<MultipartBody.Part> files
    );
}
