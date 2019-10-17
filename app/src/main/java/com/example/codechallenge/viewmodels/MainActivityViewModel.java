package com.example.codechallenge.viewmodels;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.codechallenge.repositories.NetworkCallbacks;
import com.example.codechallenge.repositories.NetworkCalls;
import com.example.codechallenge.utils.Validations;
import com.example.trimulabstask.GetArtworksQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.codechallenge.utils.Constants.GET_ARTICLES;
import static com.example.codechallenge.utils.Constants.LOG_TAG;

/*
    This is the ViewModel class which fetch data from API and provide that data to MainActivity to display it.
    All operations on data will be also done in this ViewModel class.
    This class help us to separate our Business Logic from UI related tasks.
 */
public class MainActivityViewModel extends ViewModel implements NetworkCallbacks {
    private MutableLiveData<List<GetArtworksQuery.Artwork>> originalData;
    private MutableLiveData<List<GetArtworksQuery.Artwork>> listArticles;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init() {
        if (listArticles != null) {
            return;
        }
        NetworkCalls networkCalls = NetworkCalls.getInstance();//Getting Singleton instance
        networkCalls.setCallbackListener(this);//providing Callback Listener
        originalData = new MutableLiveData<>();//Initializing MutableLiveData object
        listArticles = new MutableLiveData<>();//Initializing MutableLiveData object
        networkCalls.getArticles(264, 264);//Making network Call
    }

    @Override
    public void onPreServiceCall() {// This callback will trigger before network call start
        Handler mHandler = new Handler(Looper.getMainLooper());//Switching from background Thread to Main Thread
        mHandler.post(() -> mIsUpdating.setValue(true)); //By setting true telling the Observers that Network call start
    }

    @Override
    public void onSuccess(int responseCode, Response response) {// This callback will trigger onSuccessful network call
        if (responseCode == GET_ARTICLES) {//Checking network call response code (This is helpful when we make multiple calls in same class)
            handleCallbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {// This callback will trigger onFailed network call
        Log.e(LOG_TAG, Objects.requireNonNull(exception.getMessage()));//Printing exception message
    }

    @Override
    public void onPostServiceCall() {// This callback will trigger when network call end
        Handler mHandler = new Handler(Looper.getMainLooper());//Switching from background Thread to Main Thread
        mHandler.post(() -> mIsUpdating.setValue(false));//By setting false telling the Observers that Network call finished
    }

    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response.data())) {//Validating data object for not Null and Empty
            GetArtworksQuery.Data data = (GetArtworksQuery.Data) response.data();//Parsing Data
            if (Validations.isObjectNotEmptyAndNull(data.artworks())) {//Validating the parse data
                Handler mHandler = new Handler(Looper.getMainLooper());//Switching from background Thread to Main Thread
                mHandler.post(() -> {
                    listArticles.setValue(data.artworks());//setting data in object
                    originalData.setValue(data.artworks());//setting data in object
                });
            }
        }
    }

    public LiveData<List<GetArtworksQuery.Artwork>> getDataList() {
        return listArticles; //Providing LiveData object to observe
    }

    public void restoreOriginalData() {//This method is used to restore the list after search complete or cancelled
        listArticles.setValue(originalData.getValue());
    }

    public void search(String str) {//Search method is used to search the list by Artwork or Artist

        List<GetArtworksQuery.Artwork> searchList = new ArrayList<>(originalData.getValue());

        String[] strArray = str.split(" ");//uncomment
        searchList = isContain(searchList, strArray[0]);
        
        if(strArray.length > 1) {
            searchList = isContain(searchList, strArray[1]);
        }

        listArticles.setValue(searchList);//Setting the searchList value
    }

    private List<GetArtworksQuery.Artwork> isContain(List<GetArtworksQuery.Artwork> listToSearchIn, String str){
        for (int i = 0; i < listToSearchIn.size(); i++) {
            if (listToSearchIn.get(i).title().toLowerCase().contains(str.toLowerCase()) || listToSearchIn.get(i).artist_names().toLowerCase().contains(str.toLowerCase())) {
                continue;
            } else {
                listToSearchIn.remove(i);
                i--;
            }
        }
        return listToSearchIn;
    }

    public LiveData<Boolean> getIsUpdating() {//Returning the LiveData object to observe
        return mIsUpdating;
    }
}
