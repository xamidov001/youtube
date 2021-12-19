package uz.pdp.youtube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.pdp.youtube.R
import uz.pdp.youtube.adapters.RecAdapter
import uz.pdp.youtube.classes.Item
import uz.pdp.youtube.classes.chennals.ChannelsClass
import uz.pdp.youtube.classes.comment.CommentClass
import uz.pdp.youtube.classes.video.VideoData
import uz.pdp.youtube.databinding.FragmentPlayBinding
import uz.pdp.youtube.network.NetworkHelper
import uz.pdp.youtube.retro.ApiClient
import uz.pdp.youtube.utils.YoutubeResource
import uz.pdp.youtube.viewmodel.ViewModelFactory
import uz.pdp.youtube.viewmodel.YoutubeViewModel

class PlayFragment : Fragment(R.layout.fragment_play) {

    private val binding by viewBinding(FragmentPlayBinding::bind)
    private lateinit var playVideo: Item
    private lateinit var recAdapter: RecAdapter
    private lateinit var list: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playVideo = it.getSerializable("play") as Item
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = ArrayList()

        binding.apply {

            youtubePlayerView.initialize({
                it.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        it.loadVideo(playVideo.id.videoId, 0f)
                    }
                })
            }, true)

            recAdapter = RecAdapter(requireContext())

            val linearLayoutManager = LinearLayoutManager(requireContext())
            recycle.layoutManager = linearLayoutManager
            recycle.adapter = recAdapter

            getCommentCons.setOnClickListener {
                getComment()
            }

            val networkHelper = NetworkHelper(requireContext())


            if (networkHelper.isNetworkConnect()) {
                ApiClient.appService.getChannelData(playVideo.snippet.channelId)
                    .enqueue(object : Callback<ChannelsClass> {
                        override fun onResponse(
                            call: Call<ChannelsClass>,
                            response: Response<ChannelsClass>
                        ) {
                            if (response.isSuccessful) {
                                val item = response.body()!!.items[0]
                                Glide.with(requireContext()).load(item.snippet.thumbnails.medium.url).into(channelImg)
                                subscribeTxt.text = item.statistics.subscriberCount
                            }
                        }

                        override fun onFailure(call: Call<ChannelsClass>, t: Throwable) {

                        }

                    })
            }

            if (networkHelper.isNetworkConnect()) {
                ApiClient.appService.getVideoData(playVideo.id.videoId)
                    .enqueue(object : Callback<VideoData> {
                        override fun onResponse(
                            call: Call<VideoData>,
                            response: Response<VideoData>
                        ) {
                            if (response.isSuccessful) {
                                val item = response.body()!!.items[0]
                                val commentCount = item.statistics.commentCount
                                val dislikeCount = item.statistics.dislikeCount
                                val likeCount = item.statistics.likeCount
                                val viewCount = item.statistics.viewCount
                                val date = item.snippet.publishedAt.substring(0, 10)
                                binding.likeCountTv.text = likeCount
                                binding.dislikeCountTv.text = dislikeCount
                                binding.countLike.text = "$viewCount views • Premiered $date"
                                binding.commentsCount.text = "Comments $commentCount"
                                titleVideo.text = item.snippet.title
                                titleTxt.text = item.snippet.channelTitle
                                countLike.text = item.snippet.liveBroadcastContent
                            }
                        }

                        override fun onFailure(call: Call<VideoData>, t: Throwable) {

                        }

                    })
            }
        }
    }

    fun getComment() {
        val networkHelper = NetworkHelper(requireContext())
        if (networkHelper.isNetworkConnect()) {
            ApiClient.appService.getComments(playVideo.id.videoId)
                .enqueue(object : Callback<CommentClass> {
                    override fun onResponse(
                        call: Call<CommentClass>,
                        response: Response<CommentClass>
                    ) {
                        if (response.isSuccessful) {
                            recAdapter.submitList(response.body()!!.items)
                        }
                    }

                    override fun onFailure(call: Call<CommentClass>, t: Throwable) {

                    }

                })
        }
    }



}