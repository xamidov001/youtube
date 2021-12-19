package uz.pdp.youtube.classes.comment

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)