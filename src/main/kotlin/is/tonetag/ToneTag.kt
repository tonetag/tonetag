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
import javax.ws.rs.NotFoundException
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

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
            ?: throw NotFoundException()
        return tagTemplate.data("indicator", indicator)
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{name}/{language}")
    fun tonetag(name: String, language: String): TemplateInstance {
        val indicator = service.findIndicator(tag = name)
            ?: throw NotFoundException() // Not implemented languages yet, but using it to bypass caching
        return tagTemplate.data("indicator", indicator)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/tag/{name}")
    fun tonetagJson(name: String): Indicator? {
        return service.findIndicator(tag = name)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/list")
    fun list(): Array<Indicator> {
        return service.getLanguage().indicators
    }

}

@Provider
class ToneTagExceptionMapper : ExceptionMapper<NotFoundException> {
    @Inject
    @Location("404.html")
    lateinit var notFoundTemplate: Template

    @Produces(MediaType.TEXT_HTML)
    override fun toResponse(exception: NotFoundException?): Response? {
        return notFoundTemplate.data("exception", exception).render().let {
            Response.status(Response.Status.NOT_FOUND).entity(it).build()
        }
    }
}