package anfy.com.anfy.Interface;

import retrofit2.Response;

public interface  ResponseListener<T> {

    void onResponse(Response<T> response);
}
