package uz.abboskhan.a12_schooladminpanel.model

class TeacherData{
     var id :String =""
     var timestamp :Long =0
     var imageUrl: String = ""
     var techName: String = ""
     var techSuraName: String = ""
     var techAge: String = ""
     var techScience: String = ""
     var techPhoneNumber: String = ""

    constructor()


    constructor(
        id :String,
        timestamp :Long,
        imageUrl: String, techName: String,
        techSuraName: String, techScience: String, techAge: String, techPhoneNumber: String
    ) {
        this.id = id
        this.timestamp = timestamp
        this.imageUrl = imageUrl
        this.techName = techName
        this.techSuraName = techSuraName
        this.techAge = techAge
        this.techPhoneNumber = techPhoneNumber
        this.techScience = techScience

    }


}