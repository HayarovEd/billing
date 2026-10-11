package com.impuls.chat.domain.model

interface MimeEntry {
    val extensions: Array<String>
    val mimeType: String
}

object MimeTypes {

    enum class MimeType(override val extensions: Array<String>, override val mimeType: String) : MimeEntry {

        /** Microsoft Excel */
        MS_EXCEL(arrayOf(".xls"), "application/vnd.ms-excel"), //

        /** Microsoft PowerPoint */
        MS_POWER_POINT(arrayOf(".ppt"), "application/vnd.ms-powerpoint"), //

        /** Microsoft Word */
        MS_WORD(arrayOf(".doc"), "application/msword"), //

        /** Microsoft Word (OpenXML) */
        MS_WORD_OPEN_XML(arrayOf(".docx"), "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),

        /** OpenDocument presentation document */
        OPEN_DOCUMENT_PRESENTATION(arrayOf(".odp"), "application/vnd.oasis.opendocument.presentation"),//

        /** OpenDocument spreadsheet document */
        OPEN_DOCUMENT_SPREADSHEET(arrayOf(".ods"), "application/vnd.oasis.opendocument.spreadsheet"),//

        /** OpenDocument text document */
        OPEN_DOCUMENT_TEXT(arrayOf(".odt"), "application/vnd.oasis.opendocument.text"),//

        /** Adobe Portable Document Format (PDF) */
        PDF(arrayOf(".pdf"), "application/pdf"),//

        /** Rich Text Format (RTF) */
        RTF(arrayOf(".rtf"), "application/rtf"), //
        /** Text, (generally ASCII or ISO 8859-n) */
        PLAIN(arrayOf(".txt"), "text/plain"),

    }

    enum class Audio(override val extensions: Array<String>, override val mimeType: String) : MimeEntry {
        /** AAC audio */
        AAC(arrayOf(".aac"), "audio/aac"),

        /** Musical Instrument Digital Interface (MIDI) */
        MIDI(arrayOf(".mid", ".midi"), "audio/midi"),

        /** Musical Instrument Digital Interface (MIDI) */
        X_MIDI(arrayOf(".mid", ".midi"), "audio/x-midi"),

        /** Matroska Multimedia Container */
        MKA(arrayOf(".mka"), "audio/x-matroska"),

        /** MP3 audio */
        MP3(arrayOf(".mp3"), "audio/mpeg"),

        /** MP4 audio */
        MP4(arrayOf(".mp4"), "audio/mp4"),

        /** OGG audio */
        OGG(arrayOf(".oga"), "audio/ogg"),

        /** Opus audio */
        OPUS(arrayOf(".opus"), "audio/opus"),

        /** Waveform Audio Format */
        WAV(arrayOf(".wav"), "audio/wav"),

        /** WEBM audio */
        WEBM(arrayOf(".weba"), "audio/webm")
    }

    enum class Image(override val extensions: Array<String>, override val mimeType: String) : MimeEntry {
        /** Windows OS/2 Bitmap Graphics */
        BMP(arrayOf(".bmp"), "image/bmp"),//

        /** Graphics Interchange Format (GIF)*/
        GIF(arrayOf(".gif"), "image/gif"), //

        /** Icon format */
        ICON(arrayOf(".ico"), "image/vnd.microsoft.icon"),

        /** JPEG images */
        JPEG(arrayOf(".jpg", ".jpeg"), "image/jpeg"),//

        /** Portable Network Graphics */
        PNG(arrayOf(".png"), "image/png"),//

        /** Scalable Vector Graphics (SVG) */
        SVG_XML(arrayOf(".svg"), "image/svg+xml"),//

        /** WEBP image */
        WEBP(arrayOf(".webp"), "image/webp"), //

    }


    enum class Video(override val extensions: Array<String>, override val mimeType: String) : MimeEntry {
        /** AVI: Audio Video Interleave */
        AVI(arrayOf(".avi"), "video/x-msvideo"),//

        /** Matroska Multimedia Container */
        MKV(arrayOf(".mkv"), "video/x-matroska"),//

        /** MP4 video */
        MP4(arrayOf(".mp4"), "video/mp4"),//

        /** MPEG Video */
        MPEG(arrayOf(".mpg", ".mpeg"), "video/mpeg"),//

        /** OGG video */
        OGG(arrayOf(".ogv"), "video/ogg"),//

        /** QuickTime */
        QUICKTIME(arrayOf(".mov", ".qt"), "video/quicktime"),//

        /** WEBM video */
        WEBM(arrayOf(".webm"), "video/webm")//
    }

    fun getMimeTypeString(value: String): String {
        if (value.contains("/")) return value

        val extension = when {
            value.contains(".") -> "." + value.substringAfterLast(".").lowercase()
            value.startsWith(".") -> value.lowercase()
            else -> ".$value".lowercase()
        }

        return MimeType.entries.find { it.extensions.contains(extension) }?.mimeType
            ?: Audio.entries.find { it.extensions.contains(extension) }?.mimeType
            ?: Image.entries.find { it.extensions.contains(extension) }?.mimeType
            ?: Video.entries.find { it.extensions.contains(extension) }?.mimeType
            ?: "application/octet-stream"
    }

    fun convertToMimeType(mime: String): MimeEntry? {
        return MimeType.entries.find { it.mimeType == mime }
            ?: Audio.entries.find { it.mimeType == mime }
            ?: Image.entries.find { it.mimeType == mime }
            ?: Video.entries.find { it.mimeType == mime }
    }
}
