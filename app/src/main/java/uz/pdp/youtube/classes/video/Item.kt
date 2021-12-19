package uz.pdp.youtube.classes.video

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet,
    val statistics: Statistics
)