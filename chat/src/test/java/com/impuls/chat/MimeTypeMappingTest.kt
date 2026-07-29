package com.impuls.chat

import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.ui.uikit.toImage
import org.junit.Assert.assertEquals
import org.junit.Test

class MimeTypeMappingTest {

    @Test
    fun `MimeType toImage maps correctly`() {
        assertEquals(R.drawable.pdf, MimeTypes.MimeType.PDF.toImage())
        assertEquals(R.drawable.xls, MimeTypes.MimeType.MS_EXCEL.toImage())
        assertEquals(R.drawable.doc, MimeTypes.MimeType.MS_WORD.toImage())
    }

    @Test
    fun `getMimeTypeString resolves extensions correctly`() {
        // With dot
        assertEquals("application/pdf", MimeTypes.getMimeTypeString(".pdf"))
        assertEquals("application/vnd.ms-excel", MimeTypes.getMimeTypeString(".xls"))
        
        // Without dot
        assertEquals("application/pdf", MimeTypes.getMimeTypeString("pdf"))
        
        // Mixed case
        assertEquals("application/pdf", MimeTypes.getMimeTypeString(".PDF"))
        
        // Full file name
        assertEquals("application/pdf", MimeTypes.getMimeTypeString("test.pdf"))
        
        // Unknown
        assertEquals("application/octet-stream", MimeTypes.getMimeTypeString("unknown_ext"))
    }

    @Test
    fun `getMimeTypeString returns original if already a mime type`() {
        val mime = "application/json"
        assertEquals(mime, MimeTypes.getMimeTypeString(mime))
    }
}
