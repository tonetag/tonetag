package `is`.tonetag.model

data class Indicator(
    val tag: String,
    val aliases: List<String>,
    val hiddenAliases: List<String> = emptyList(),
    val long: String,
    val definition: String,
    val description: String,
    val emoji: List<String>,
    val colour: String,
    val hidden: Boolean = false,
)
