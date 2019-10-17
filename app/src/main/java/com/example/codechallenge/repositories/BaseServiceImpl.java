package com.example.codechallenge.repositories;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.exception.ApolloException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import static com.example.codechallenge.utils.Constants.BASE_URL;
/*
    This class contains all the basic logic used to make network call
 */
class BaseServiceImpl {
    private Query query;
    private Mutation mutation;
    private ApolloClient apolloClient;
    private NetworkCallbacks networkCallbacks;
    private int responseCode;
    private boolean isMutationCall;

    BaseServiceImpl() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//Defining and Initializing OkHttpClient object and setting basic call properties
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        apolloClient = ApolloClient.builder()//Initializing ApolloClient object and feeding BASE_URL and OkHttpClient object
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)//Right now our policy is NetworkOnly
                .build();
    }

    void baseServiceQueryCall(Query query, NetworkCallbacks networkCallbacks, int responseCode) {
        isMutationCall = false;//this flag tell us that call is Query or Mutation
        this.query = query;//Setting Query
        this.networkCallbacks = networkCallbacks;//Setting callback listener
        this.responseCode = responseCode;//Setting responseCode
        initCall();//Method Call
    }

    void baseServiceMutationCall(Mutation mutation, NetworkCallbacks networkCallbacks, int responseCode) {
        isMutationCall = true;
        this.mutation = mutation;
        this.networkCallbacks = networkCallbacks;
        this.responseCode = responseCode;
        initCall();
    }

    private void initCall() {
        networkCallbacks.onPreServiceCall();//Trigger onPreServiceCall method
        if (isMutationCall) {//Check for call is Mutation or Query
            makeMutationCall();
        } else {
            makeQueryCall();
        }
    }

    private void makeQueryCall() {
        apolloClient.query(query).enqueue(new ApolloCall.Callback() {//Feeding query in ApolloClient object then enqueue the call
            @Override
            public void onResponse(Response response) {//onSuccess
                networkCallbacks.onSuccess(responseCode, response);//trigger onSuccess method with Response object and responseCode
                networkCallbacks.onPostServiceCall();//trigger onPostServiceCall method
            }

            @Override
            public void onFailure(ApolloException e) {//onFailure
                networkCallbacks.onFailure(responseCode, e);//trigger onFailure method with responseCode and exception
                networkCallbacks.onPostServiceCall();//trigger onPostServiceCall method
            }
        });
    }

    private void makeMutationCall() {
        apolloClient.mutate(mutation).enqueue(new ApolloCall.Callback() {
            @Override
            public void onResponse(Response response) {
                networkCallbacks.onSuccess(responseCode, response);
                networkCallbacks.onPostServiceCall();
            }

            @Override
            public void onFailure(ApolloException e) {
                networkCallbacks.onFailure(responseCode, e);
                networkCallbacks.onPostServiceCall();
            }
        });
    }
}
