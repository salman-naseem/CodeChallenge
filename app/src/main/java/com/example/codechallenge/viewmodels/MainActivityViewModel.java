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

        //TODO:  I struggled while implementing the logic of search both (by Artwork and Artist)

        /*
        The logic i implemented is to check that search string contains " ". If " " exist then split the string by " " and search both
        words in list. If word contain in Artist or Artwork add that item in searchList. By this logic we got the list of items but not
        the exact same item. We can counter this by again looping the search list and eliminate the extra items but i'm not liking that
        logic. Kindly guide me with better solution.
        */

        List<GetArtworksQuery.Artwork> searchList = new ArrayList<>();

        /*Search by both logic is commented below*/

//        String[] strArray = str.split(" ");//uncomment
//        for (String s : strArray) {//uncomment
//            if (s.isEmpty()) {//uncomment
//                continue;//uncomment
//            }//uncomment
            for (GetArtworksQuery.Artwork item : originalData.getValue()) {
                //TODO: If you uncommented the "Search By Both" logic, then Kindly change the below "str" variable to "s". Thanks:)
                if (item.title().toLowerCase().contains(str.toLowerCase())) {//Check if search string contain in Title
                    searchList.add(item);
                } else if (item.artist_names().toLowerCase().contains(str.toLowerCase())) {//Check if search string contain in Artist
                    searchList.add(item);
                }
            }
//        }//uncomment
        listArticles.setValue(searchList);//Setting the searchList value
    }

    public LiveData<Boolean> getIsUpdating() {//Returning the LiveData object to observe
        return mIsUpdating;
    }
}
