package uz.pdp.youtube.classes.video

data class Snippet(
    val categoryId: String,
    val channelId: String,
    val channelTitle: String,
    val description: String,
    val liveBroadcastContent: String,
    val localized: Localized,
    val publishedAt: String,
    val tags: List<String>,
    val thumbnails: Thumbnails,
    val title: String
)