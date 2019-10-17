package com.example.codechallenge.repositories;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

public interface NetworkCallbacks<T> {
    void onPreServiceCall();
    void onSuccess(int responseCode, Response response);
    void onFailure(int responseCode, ApolloException exception);
    void onPostServiceCall();
}
