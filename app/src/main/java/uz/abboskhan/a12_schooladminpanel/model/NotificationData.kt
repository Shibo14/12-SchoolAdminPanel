package uz.abboskhan.a12_schooladminpanel.model

class NotificationData {
    var id :String =""
    var timestamp :Long =0
    var title: String = ""
    var description: String = ""

    constructor(id: String, timestamp: Long,  title: String, description: String) {
        this.id = id
        this.timestamp = timestamp
        this.title = title
        this.description = description
    }

    constructor()

}