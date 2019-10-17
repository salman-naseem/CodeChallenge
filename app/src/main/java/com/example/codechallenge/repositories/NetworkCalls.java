package com.example.codechallenge.repositories;


import com.example.trimulabstask.GetArtworksQuery;

import static com.example.codechallenge.utils.Constants.GET_ARTICLES;

/*
    Network calls is implementing singleton pattern. This class is extending BaseServiceImpl which is a parent class
    and have all the basic common functionality of making network call.
*/
public class NetworkCalls extends BaseServiceImpl {
    /*
        All Queries and Mutation calls will be
        initialized here then baseServiceQueryCall and baseServiceMutationCall will be used to make calls
    */
    private static NetworkCalls instance;//making  static object of class
    private NetworkCallbacks networkCallbacks;//callback object

    private NetworkCalls() {//Access of constructor is private to avoid memory leaks
        super();
    }

    public static NetworkCalls getInstance() {// This method provides the instance of class
        if (instance == null) {// if instance is null, initialized it
            instance = new NetworkCalls();
        }
        return instance;//otherwise return the instance
    }

    public void setCallbackListener(NetworkCallbacks networkCallbacks) {
        this.networkCallbacks = networkCallbacks;//This method is use to define that which class is listening network callbacks
    }

    public void getArticles(int imageWidth, int imageHeight) {
        GetArtworksQuery query = GetArtworksQuery.builder()//GraphQL query, with 2 parameters of image width height
                .imagewidth(imageWidth)
                .imageHeight(imageHeight).build();
        baseServiceQueryCall(query, networkCallbacks, GET_ARTICLES);//this is the BaseServiceImpl method which need the Query/Mutation, callback object and expected responseCode
    }
}
