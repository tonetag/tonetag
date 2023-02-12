package `is`.tonetag.data

import `is`.tonetag.model.Language
import java.nio.file.Paths

object TagLoader {

    fun getAvailableLanguages(): List<String> {
        val uri = this::class.java.getResource("/data/tags")?.toURI()
        val path = Paths.get(uri)

        return path.toFile().listFiles().map { it.name }
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