package com.example.template.model.backend;


import static com.example.template.utils.Constants.SERVER_URL_PHP;

/**
 * Created by Chike on 12/4/2016.
 */

public class ApiUtils {

    private ApiUtils() {
    }

    public static APIService getAPIService() {

        //clearRetrofit();
        return RetrofitClient.getClient(SERVER_URL_PHP).create(APIService.class);
    }
}
