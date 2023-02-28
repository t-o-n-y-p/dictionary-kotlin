package com.tonyp.dictionary

class Dictionary {

    private fun listToString(word: String, args: List<Any>): String {
        if (args.isEmpty()) {
            return "Не знаю значений этого слова."
        }
        if (args.size == 1) {
            return "$word:\n\n${args[0]}"
        }
        val jointArgs = args.withIndex()
            .joinToString("\n") {
                    (i, m) -> "${i + 1}. $m"
            }
        return "$word:\n\n$jointArgs"
    }

    fun getMeaningsMessage(word: String): String {
        if (word == "растение") {
            return listToString(word, emptyList())
        }
        if (word == "цветок") {
            return listToString(
                word, listOf("орган размножения растений с венчиком из лепестков вокруг пестика и тычинок"))
        }
        return listToString(
            word, listOf(
                "травянистые растения, обладающие лечебными свойствами, входящие в лекарственные сборы",
                "о чем-н. не имеющем вкуса, безвкусном (разг.)"))
    }

}