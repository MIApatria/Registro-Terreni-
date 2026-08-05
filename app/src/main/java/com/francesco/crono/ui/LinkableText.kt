package com.francesco.crono.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

private val linkRegex = Regex(
    // URL | email | phone (+39 ... o numeri lunghi con spazi)
    "(https?://\\S+)|([\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,})|(\\+?\\d[\\d ]{7,}\\d)"
)

/**
 * Testo che rende cliccabili numeri di telefono, email e URL.
 * Toccando un numero parte il dialer, un'email apre la mail, un URL il browser.
 */
@Composable
fun LinkableText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    val context = LocalContext.current
    val linkColor = MaterialTheme.colorScheme.primary

    val annotated: AnnotatedString = buildAnnotatedString {
        var last = 0
        for (m in linkRegex.findAll(text)) {
            if (m.range.first > last) append(text.substring(last, m.range.first))
            val raw = m.value
            val uri = when {
                raw.startsWith("http") -> raw
                raw.contains("@") -> "mailto:$raw"
                else -> "tel:" + raw.replace(" ", "")
            }
            pushStringAnnotation(tag = "link", annotation = uri)
            withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.Medium)) {
                append(raw)
            }
            pop()
            last = m.range.last + 1
        }
        if (last < text.length) append(text.substring(last))
    }

    ClickableText(
        text = annotated,
        modifier = modifier,
        style = style.copy(color = MaterialTheme.colorScheme.onSurface),
        onClick = { offset ->
            annotated.getStringAnnotations("link", offset, offset).firstOrNull()?.let { ann ->
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ann.item)))
                } catch (_: ActivityNotFoundException) {
                    // Nessuna app in grado di gestire l'azione: ignora.
                }
            }
        }
    )
}
