package uz.abboskhan.a12_schooladminpanel.model

class RatingData{
     var id :String =""
     var timestamp :Long =0
     var imageUrl: String = ""
     var studentName: String = ""
     var studentSuraName: String = ""
     var studentAge: String = ""
     var studentScience: String = ""
     var studentClassNumber: String = ""


    constructor(
        id: String,
        timestamp: Long,
        imageUrl: String,
        studentName: String,
        studentSuraName: String,
        studentAge: String,
        studentScience: String,
        studentClassNumber: String
    ) {
        this.id = id
        this.timestamp = timestamp
        this.imageUrl = imageUrl
        this.studentName = studentName
        this.studentSuraName = studentSuraName
        this.studentAge = studentAge
        this.studentScience = studentScience
        this.studentClassNumber = studentClassNumber
    }

    constructor()
}