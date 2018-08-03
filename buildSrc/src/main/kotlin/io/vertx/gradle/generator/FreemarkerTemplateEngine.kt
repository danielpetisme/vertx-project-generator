package io.vertx.gradle.generator

import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import java.io.File

open class FreemarkerTemplateEngine(val from: File, val into: File, val templateExtension: String = ".ftl") {

  private val hasFreemarkerExtension = { _: File, name: String ->
    name.endsWith(templateExtension, true)
  }

  private val engine = Configuration(Configuration.VERSION_2_3_28).apply {
    setDirectoryForTemplateLoading(from)
    templateExceptionHandler = TemplateExceptionHandler.DEBUG_HANDLER
  }

  fun render(properties: Map<String, Any> = mapOf()) {
    from.list(hasFreemarkerExtension).forEach { template ->
      val destination = into.resolve(template.removeSuffix(templateExtension))
      destination.parentFile.mkdirs()
      engine.getTemplate(template).process(properties, destination.printWriter())
    }
  }
}
