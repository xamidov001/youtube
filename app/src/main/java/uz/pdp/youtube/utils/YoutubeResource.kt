package uz.pdp.youtube.utils

import uz.pdp.youtube.classes.Item
import uz.pdp.youtube.classes.PlayVideo

sealed class YoutubeResource {

    object Loading: YoutubeResource()

    data class Success(val list: PlayVideo): YoutubeResource()

    data class Error(val message: String): YoutubeResource()

}