package uz.abboskhan.a12_schooladminpanel.model

class LibraryData {
    var id: String = ""
    var title: String = ""
    var description: String = ""

    var timesTamp: Long? = 0
    var urlPdf: String = ""


    constructor(
        id: String,
        title: String,
        description: String,

        timesTamp: Long?,
        urlPdf: String
    ) {
        this.id = id
        this.title = title
        this.description = description

        this.timesTamp = timesTamp
        this.urlPdf = urlPdf
    }

    constructor()
}