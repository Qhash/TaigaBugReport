package com.kipdev.taigabugreport.network;

import com.kipdev.taigabugreport.pojo.reponse.BaseResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Denis Dyakov on 19.01.2017.
 */

public interface IBugReportApi {

    @Multipart
    @POST("{url}")
    Call<BaseResponse> sendReport(@Path(value = "url", encoded = true) String url,
                                  @Part("token") RequestBody token,
                                  @Part("description") RequestBody  description,
                                  @Part("subject") RequestBody  subject,
                                  @Part("project_id") RequestBody project_id,
                                  @Part MultipartBody.Part file1,
                                  @Part MultipartBody.Part file2,
                                  @Part MultipartBody.Part file3,
                                  @Part MultipartBody.Part file4,
                                  @Part MultipartBody.Part file5,
                                  @Part MultipartBody.Part file6,
                                  @Part MultipartBody.Part file7,
                                  @Part MultipartBody.Part file8,
                                  @Part MultipartBody.Part file9);
}
