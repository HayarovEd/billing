package com.impuls.chat.ui.uikit


import com.impuls.chat.R
import com.impuls.chat.domain.model.MimeTypes

fun MimeTypes.MimeType.toImage() =
    when (this) {
        MimeTypes.MimeType.MS_EXCEL -> R.drawable.xls
        MimeTypes.MimeType.MS_POWER_POINT -> R.drawable.ppt
        MimeTypes.MimeType.MS_WORD -> R.drawable.doc
        MimeTypes.MimeType.MS_WORD_OPEN_XML -> R.drawable.docx
        MimeTypes.MimeType.OPEN_DOCUMENT_PRESENTATION -> R.drawable.odp_file
        MimeTypes.MimeType.OPEN_DOCUMENT_SPREADSHEET -> R.drawable.ods_file
        MimeTypes.MimeType.OPEN_DOCUMENT_TEXT -> R.drawable.odt_file
        MimeTypes.MimeType.PDF -> R.drawable.pdf
        MimeTypes.MimeType.RTF -> R.drawable.rtf
        MimeTypes.MimeType.PLAIN -> R.drawable.txt
    }
