package com.francesco.crono.model

/** A driving leg shown at the top of a day. */
data class Segment(
    val route: String,
    val info: String,
    val mapsQuery: String? = null
)

/** A single timed entry in the day's timeline. */
data class Event(
    val time: String,
    val title: String,
    val detail: String,
    /** true = a highlighted / not-to-be-missed moment (fixed ticket, sunrise, etc.). */
    val highlight: Boolean = false
)

enum class BoxKind { TICKET, WARNING, TIP }

/** A colored callout box (fixed tickets, warnings, reminders). */
data class InfoBox(
    val title: String,
    val body: String,
    val kind: BoxKind
)

data class Day(
    val number: Int,
    val date: String,        // e.g. "Sab 3 ottobre"
    val cityShort: String,   // short label for the dashboard cell
    val title: String,       // full route title
    val timezone: String,
    val segments: List<Segment>,
    val events: List<Event>,
    val boxes: List<InfoBox>,
    val footer: String,
    /** accent color (hex) used for the dashboard cell + day header. */
    val colorHex: Long
)

data class ChecklistItem(
    val id: String,
    val text: String
)
