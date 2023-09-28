package uz.abboskhan.a12_schooladminpanel.model

class TableData {
    var id: String = ""
    var classId: String = ""
    var day: String = ""
    var lesson1: String = ""
    var lesson2: String = ""
    var lesson3: String = ""
    var lesson4: String = ""
    var lesson5: String = ""
    var lesson6: String = ""
    var lesson7: String = ""
    var timesTamp: Long? = 0
    var isExpandable: Boolean = false

    constructor()
    constructor(
        id: String,
        classId: String,
        day: String,
        lesson1: String,
        lesson2: String,
        lesson3: String,
        lesson4: String,
        lesson5: String,
        lesson6: String,
        lesson7: String,
        timesTamp: Long?,
        isExpandable: Boolean
    ) {
        this.id = id
        this.classId = classId
        this.day = day
        this.lesson1 = lesson1
        this.lesson2 = lesson2
        this.lesson3 = lesson3
        this.lesson4 = lesson4
        this.lesson5 = lesson5
        this.lesson6 = lesson6
        this.lesson7 = lesson7
        this.timesTamp = timesTamp
        this.isExpandable = isExpandable

    }

}