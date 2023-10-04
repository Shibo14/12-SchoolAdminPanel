package uz.abboskhan.a12_schooladminpanel.model

class LibraryData {
    var id: String = ""
    var title: String = ""
    var imageUrlPdf: String = ""

    var timesTamp: Long? = 0
    var urlPdf: String = ""


    constructor(
        id: String,
        title: String,
        imageUrlPdf: String,

        timesTamp: Long?,
        urlPdf: String
    ) {
        this.id = id
        this.title = title
        this.imageUrlPdf = imageUrlPdf

        this.timesTamp = timesTamp
        this.urlPdf = urlPdf
    }

    constructor()
}