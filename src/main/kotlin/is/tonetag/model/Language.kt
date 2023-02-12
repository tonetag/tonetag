package `is`.tonetag.model

import com.fasterxml.jackson.module.kotlin.*
import java.nio.file.Paths

data class Language(
    val version: String,
    val id: String,
    val name: String,
    val indicators: Array<Indicator>
) {
    companion object {

        val languages = HashMap<String, Language>()
        fun loadLanguage(language: String): Language {
            val uri = this::class.java.getResource("/data/tags/$language")?.toURI()
            val path = Paths.get(uri)

            val file = path.toFile()
            val json = file.readText()

            val mapper = jacksonObjectMapper()

            return mapper.readValue<Language>(json)
        }
    }

    private val indicatorTable: HashMap<String, Indicator> = HashMap()

    init {
        for (indicator in indicators) {
            indicatorTable[indicator.tag] = indicator
            for (alias in indicator.aliases) {
                indicatorTable[alias] = indicator
            }
        }
    }

    fun getIndicator(tag: String): Indicator? {
        return indicatorTable[tag]
    }

}