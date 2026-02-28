package com.nassef.core

import com.nassef.core.data.error.GeneralErrorHandlerImpl
import com.nassef.core.data.model.exception.AppException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GeneralErrorHandlerImplTest {

    private lateinit var errorHandler: GeneralErrorHandlerImpl

    @Before
    fun setUp() {
        errorHandler = GeneralErrorHandlerImpl()
    }

    @Test
    fun `IOException maps to AppException Local IOOperation`() {
        val exception = IOException("Disk read failure")

        val result = errorHandler.getError(exception)

        assertTrue(result is AppException.Local.IOOperation)
        assertEquals("Disk read failure", result.message)
    }

    @Test
    fun `unknown throwable maps to AppException Unknown`() {
        val exception = RuntimeException("Unexpected crash")

        val result = errorHandler.getError(exception)

        assertTrue(result is AppException.Unknown)
        assertEquals("Unexpected crash", result.message)
    }

    @Test
    fun `throwable with null message maps to AppException Unknown with null message`() {
        val exception = RuntimeException(null as String?)

        val result = errorHandler.getError(exception)

        assertTrue(result is AppException.Unknown)
        assertNull(result.message)
    }

    @Test
    fun `IllegalArgumentException maps to AppException Unknown`() {
        val exception = IllegalArgumentException("Bad argument")

        val result = errorHandler.getError(exception)

        assertTrue(result is AppException.Unknown)
        assertEquals("Bad argument", result.message)
    }

    @Test
    fun `isUnauthorized returns true only for Client Unauthorized`() {
        val unauthorized = AppException.Client.Unauthorized
        val unknown = AppException.Unknown("other error")
        val ioError = AppException.Local.IOOperation(0, "io error")

        assertTrue(unauthorized.isUnauthorized())
        assertFalse(unknown.isUnauthorized())
        assertFalse(ioError.isUnauthorized())
    }
}
