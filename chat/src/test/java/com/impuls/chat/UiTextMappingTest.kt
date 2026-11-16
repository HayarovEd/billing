package com.impuls.chat

import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.ui.main_screen.MessageErrorEvent
import com.impuls.chat.ui.uikit.UiText
import com.impuls.chat.ui.uikit.asUiText
import com.impuls.chat.ui.uikit.asText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UiTextMappingTest {

    @Test
    fun `ChatDataError Network mapping is correct`() {
        val error = ChatDataError.Network.NO_INTERNET
        val uiText = error.asUiText()
        
        assertTrue(uiText is UiText.StringResource)
        assertEquals(R.string.no_internet, (uiText as UiText.StringResource).id)
    }

    @Test
    fun `ChatDataError CustomErrorChat mapping is correct`() {
        val message = "Test custom error"
        val error = ChatDataError.CustomErrorChat(message)
        val uiText = error.asUiText()
        
        assertTrue(uiText is UiText.DynamicString)
        assertEquals(message, (uiText as UiText.DynamicString).value)
    }

    @Test
    fun `ChatDataError FileErrorChat mapping is correct`() {
        val error = ChatDataError.FileErrorChat.WRITE_ERROR
        val uiText = error.asUiText()
        
        assertTrue(uiText is UiText.StringResource)
        assertEquals(R.string.write_file_error, (uiText as UiText.StringResource).id)
    }

    @Test
    fun `MessageErrorEvent mapping is correct`() {
        val event = MessageErrorEvent.MESSAGE_EMPTY
        val uiText = event.asText()
        
        assertTrue(uiText is UiText.StringResource)
        assertEquals(R.string.write_message, (uiText as UiText.StringResource).id)
    }
}
