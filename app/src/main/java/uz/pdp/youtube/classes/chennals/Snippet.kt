package uz.pdp.youtube.classes.chennals

data class Snippet(
    val country: String,
    val customUrl: String,
    val description: String,
    val localized: Localized,
    val publishedAt: String,
    val thumbnails: Thumbnails,
    val title: String
)