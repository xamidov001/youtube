package uz.pdp.youtube.retro

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.pdp.youtube.classes.PlayVideo
import uz.pdp.youtube.classes.chennals.ChannelsClass
import uz.pdp.youtube.classes.comment.CommentClass
import uz.pdp.youtube.classes.video.VideoData

interface ApiService {

    @GET("v3/search?key=AIzaSyA71xgkfChkIx3TKo2mfg5wZdAzFG7bFS0&part=snippet,id&maxResults=10")
    suspend fun getVideos(@Query("q") q: String = "", @Query("pageToken") page: String = ""): Response<PlayVideo>

    @GET("v3/search?key=AIzaSyDcz4YDB96RrlSq4YvVx0CQvpscMnhLdls&part=snippet,id&maxResults=10")
    suspend fun getVideos1(@Query("q") q: String = ""): Response<PlayVideo>

    @GET("v3/search?key=AIzaSyA71xgkfChkIx3TKo2mfg5wZdAzFG7bFS0&part=snippet,id&maxResults=10")
    suspend fun getSearchVideo(@Query("q") q: String = ""): Response<PlayVideo>

    @GET("v3/commentThreads?part=snippet&maxResults=20&key=AIzaSyA71xgkfChkIx3TKo2mfg5wZdAzFG7bFS0")
    fun getComments(@Query("videoId") videoId: String): Call<CommentClass>

    @GET("v3/channels?part=snippet,id,statistics&key=AIzaSyA71xgkfChkIx3TKo2mfg5wZdAzFG7bFS0")
    fun getChannelData(@Query("id") id: String): Call<ChannelsClass>

    @GET("v3/videos?&key=AIzaSyA71xgkfChkIx3TKo2mfg5wZdAzFG7bFS0&part=snippet,statistics")
    fun getVideoData(@Query("id") id: String): Call<VideoData>
}