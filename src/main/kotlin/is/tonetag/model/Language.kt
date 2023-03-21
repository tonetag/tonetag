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
            val inputStream = this::class.java.getResourceAsStream("/META-INF/data/tags/$language.json")

            val json = inputStream.bufferedReader().use { it.readText() }

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
            for (alias in indicator.hiddenAliases) {
                indicatorTable[alias] = indicator
            }
        }
    }

    fun getIndicator(tag: String): Indicator? {
        return indicatorTable[tag]
    }

}