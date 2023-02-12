package `is`.tonetag

import io.quarkus.arc.Priority
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import io.quarkus.runtime.StartupEvent
import `is`.tonetag.data.TagLoader
import `is`.tonetag.model.Indicator
import `is`.tonetag.model.Language
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
class ToneTagService {

    fun onStart(@Observes @Priority(1) event: StartupEvent) {
        TagLoader.loadLanguages()
    }

    fun getLanguage(language: String = "en_gb"): Language {
        return Language.languages[language] ?: Language.languages["en_gb"]!!
    }

    fun findIndicator(language: String = "en_gb", tag: String): Indicator? {
        val lang = Language.languages[language]
        return lang?.getIndicator(tag)
    }

}

@Path("/")
class ToneTagResource {

    @Inject
    @field: Default
    lateinit var service: ToneTagService

    @Inject
    @Location("tag.html")
    lateinit var tagTemplate: Template

    @Inject
    @Location("index.html")
    lateinit var indexTemplate: Template


    @GET
    @Produces(MediaType.TEXT_HTML)
    fun index(): TemplateInstance {
        val indicators: Array<Indicator> = service.getLanguage().indicators
        return indexTemplate.data("indicators", indicators)
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{name}")
    fun tonetag(name: String): TemplateInstance {
        val indicator = service.findIndicator(tag = name)
        return tagTemplate.data("indicator", indicator)
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{name}/{language}")
    fun tonetag(name: String, language: String): TemplateInstance {
        val indicator = service.findIndicator(tag = name) // Not implemented languages yet, but using it to bypass caching
        return tagTemplate.data("indicator", indicator)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("json/{name}")
    fun tonetagJson(name: String): Indicator? {
        return service.findIndicator(tag = name)
    }

}
