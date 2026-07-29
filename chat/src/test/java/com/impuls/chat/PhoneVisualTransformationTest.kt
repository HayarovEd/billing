package com.impuls.chat

import androidx.compose.ui.text.AnnotatedString
import com.impuls.chat.ui.uikit.PhoneVisualTransformation
import org.junit.Assert.assertEquals
import org.junit.Test

class PhoneVisualTransformationTest {

    private val mask = "+0(000)000-00-00"
    private val maskNumber = '0'
    private val transformation = PhoneVisualTransformation(mask, maskNumber)

    @Test
    fun `formatting empty string returns empty transformation`() {
        val result = transformation.filter(AnnotatedString(""))
        assertEquals("", result.text.text)
    }

    @Test
    fun `formatting partial number works correctly`() {
        val input = "7999123"
        val result = transformation.filter(AnnotatedString(input))
        // Expected: +7(999)123
        assertEquals("+7(999)123", result.text.text)
    }

    @Test
    fun `formatting full number works correctly`() {
        val input = "79991234567"
        val result = transformation.filter(AnnotatedString(input))
        // Expected: +7(999)123-45-67
        assertEquals("+7(999)123-45-67", result.text.text)
    }

    @Test
    fun `offset mapping originalToTransformed is correct`() {
        val mapper = transformation.filter(AnnotatedString("7999")).offsetMapping
        
        assertEquals(0, mapper.originalToTransformed(0)) // before "+"
        assertEquals(2, mapper.originalToTransformed(1)) // after "7", before "("
        assertEquals(4, mapper.originalToTransformed(2)) // after first "9"
        assertEquals(5, mapper.originalToTransformed(3)) // after second "9"
    }

    @Test
    fun `offset mapping transformedToOriginal is correct`() {
        val mapper = transformation.filter(AnnotatedString("7999")).offsetMapping
        
        // Transformed: +7(999
        assertEquals(0, mapper.transformedToOriginal(0)) // before "+"
        assertEquals(0, mapper.transformedToOriginal(1)) // after "+"
        assertEquals(1, mapper.transformedToOriginal(2)) // after "7"
        assertEquals(1, mapper.transformedToOriginal(3)) // after "("
        assertEquals(2, mapper.transformedToOriginal(4)) // after first "9"
    }
}
