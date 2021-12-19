package uz.pdp.youtube.repository

import kotlinx.coroutines.flow.flow
import uz.pdp.youtube.retro.ApiService

class YoutubeRepository(private val apiService: ApiService)  {

    fun getYoutube(query: String, page: String) = flow { emit(apiService.getVideos(query, page)) }

    fun getYoutube1(query: String) = flow { emit(apiService.getVideos1(query)) }

    fun getYoutubeSearch(query: String) = flow { emit(apiService.getSearchVideo(query)) }
}