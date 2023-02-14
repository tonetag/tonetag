package `is`.tonetag.data

import `is`.tonetag.model.Language
import kotlin.collections.ArrayList

object TagLoader {

    private fun getAvailableLanguages(): List<String> {
        return listOf("en_gb")
    }

    fun loadLanguages() {
        val languages = getAvailableLanguages()
        val loadedLanguages = ArrayList<Language>()

        for (language in languages) {
            println("Loading language file $language")
            loadedLanguages.add(Language.loadLanguage(language))
        }

        for (language in loadedLanguages) {
            Language.languages[language.id] = language
        }

    }
}