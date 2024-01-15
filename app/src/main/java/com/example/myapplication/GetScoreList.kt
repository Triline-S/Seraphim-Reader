package com.example.myapplication


import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.TAG_USER_COMMENT
import java.io.File


object GetScoreList {
    var scoreDir = File("${Environment.getExternalStorageDirectory().absolutePath}/Documents/Scores/Proprium")
    var ordoDir = File("${Environment.getExternalStorageDirectory().absolutePath}/Documents/Scores/Ordinarium")

    var ordoList = mutableListOf<Score>(
        Score(title = "Amen / I z duchem twoim ton prosty", desc = "", path = "${ordoDir}/Amen Simplex", id = 0),
        Score(title = "Amen / I z duchem twoim ton uroczysty", desc = "", path = "${ordoDir}/Amen Solemnis", id = 1),
        Score(title = "Chwała tobie Panie / Chryste", desc = "", path = "${ordoDir}/Verbum Domini", id = 2),
        Score(title = "Dialog przed prefacją", desc = "", path = "${ordoDir}/Prefacja", id = 3),
        Score(title = "Aklamacja po przeistoczeniu", desc = "", path = "${ordoDir}/Przeistoczenie", id = 4),
        Score(title = "Kyrie", desc = "", path = "${ordoDir}/Kyrie", id = 5),
        Score(title = "Gloria", desc = "", path = "${ordoDir}/Glorie", id = 6),
        Score(title = "Sanctus", desc = "", path = "${ordoDir}/Sanctus", id = 7),
        Score(title = "Ojcze nasz", desc = "", path = "${ordoDir}/Pater Noster", id = 8),
        Score(title = "Agnus Dei", desc = "", path = "${ordoDir}/Agnus Dei", id = 9),
        Score(title = "Rozesłanie", desc = "", path = "${ordoDir}/Koniec", id = 10)
    )

    fun changeMass(mass: String){
        ordoList[5].path = "${ordoDir}/Kyrie (${mass})"
        ordoList[6].path = "${ordoDir}/Gloria (${mass})"
        ordoList[7].path = "${ordoDir}/Sanctus (${mass})"
        ordoList[9].path = "${ordoDir}/Agnus Dei/ (${mass})"
    }
    var scoreList = mutableListOf<Score>()

    fun getScores(scoreDir: String): List<Score> {
        val scores = mutableListOf<Score>()
        var id = 0
        File(scoreDir).list()?.forEach { title ->
            val firstPage = File("${scoreDir}/${title}/01.jpg")
            val tags: List<String>? = if (firstPage.exists()){
                val scoreExif = ExifInterface(firstPage.absolutePath)
                scoreExif.getAttribute(TAG_USER_COMMENT)?.split(";")
            } else {
                null
            }
            scores += Score(title = title, desc = "", path = "${scoreDir}/${title}", tags = tags, id = id)
            id += 1
        }
        scoreList = scores
        return scores
    }

}