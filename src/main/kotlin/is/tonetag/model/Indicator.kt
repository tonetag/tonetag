package `is`.tonetag.model

data class Indicator(
    val tag: String,
    val aliases: List<String>,
    val long: String,
    val definition: String,
    val description: String,
    val emoji: List<String>,
    val colour: String,
)
