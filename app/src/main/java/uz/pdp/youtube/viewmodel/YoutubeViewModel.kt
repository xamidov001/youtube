package uz.pdp.youtube.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.youtube.network.NetworkHelper
import uz.pdp.youtube.repository.YoutubeRepository
import uz.pdp.youtube.retro.ApiClient
import uz.pdp.youtube.utils.YoutubeResource

class YoutubeViewModel(private val networkHelper: NetworkHelper): ViewModel() {

    private val photoRepository = YoutubeRepository(ApiClient.appService)

    fun fetchVideo(query: String, page: String): LiveData<YoutubeResource> {
        val liveData  = MutableLiveData<YoutubeResource>(YoutubeResource.Loading)
        if (networkHelper.isNetworkConnect()) {
            liveData.postValue(YoutubeResource.Loading)
            viewModelScope.launch {
                photoRepository.getYoutube(query, page).catch {
                    liveData.postValue(YoutubeResource.Error("Error"))
                }.collect {
                    liveData.postValue(YoutubeResource.Success(it.body()!!))
                }
            }
        } else {
            liveData.postValue(YoutubeResource.Error("Problem with internet"))
        }

        return liveData
    }

    fun fetchVideo1(query: String): LiveData<YoutubeResource> {
        val liveData  = MutableLiveData<YoutubeResource>(YoutubeResource.Loading)
        if (networkHelper.isNetworkConnect()) {
            liveData.postValue(YoutubeResource.Loading)
            viewModelScope.launch {
                photoRepository.getYoutube1(query).catch {
                    liveData.postValue(YoutubeResource.Error("Error"))
                }.collect {
                    liveData.postValue(YoutubeResource.Success(it.body()!!))
                }
            }
        } else {
            liveData.postValue(YoutubeResource.Error("Problem with internet"))
        }

        return liveData
    }

    fun fetchVideoSearch(query: String): LiveData<YoutubeResource> {
        val liveData  = MutableLiveData<YoutubeResource>(YoutubeResource.Loading)
        if (networkHelper.isNetworkConnect()) {
            liveData.postValue(YoutubeResource.Loading)
            viewModelScope.launch {
                photoRepository.getYoutubeSearch(query).catch {
                    liveData.postValue(YoutubeResource.Error("Error"))
                }.collect {
                    liveData.postValue(YoutubeResource.Success(it.body()!!))
                }
            }
        } else {
            liveData.postValue(YoutubeResource.Error("Problem with internet"))
        }

        return liveData
    }

}