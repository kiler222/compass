package com.kiler.compassapp


import com.google.common.truth.Truth.assertThat
import org.junit.Test


class SetDestinationTest{

    val setDestination = SetDestination()

    @Test
    fun `coordinates empty`() {
        val result =  setDestination.validateCoordinates(
            "",
            ""
        )
        assertThat(result).isEqualTo(0)

    }

    @Test
    fun `latitude empty and longitude invalid`() {
        val result =  setDestination.validateCoordinates(
            "",
            "234.0"
        )
        assertThat(result).isEqualTo(0)

    }


    @Test
    fun `longitude empty and latitude invalid`() {
        val result =  setDestination.validateCoordinates(
            "190.2",
            ""
        )
        assertThat(result).isEqualTo(0)

    }

    @Test
    fun `longitude valid and latitude invalid`() {
        val result =  setDestination.validateCoordinates(
            "190.2",
            "99.1"
        )
        assertThat(result).isEqualTo(1)

    }

    @Test
    fun `latitude valid and longitude invalid`() {
        val result =  setDestination.validateCoordinates(
            "19.0",
            "-399.1"
        )
        assertThat(result).isEqualTo(2)

    }

    @Test
    fun `latitude valid and longitude valid`() {
        val result =  setDestination.validateCoordinates(
            "-19.0",
            "-39.1"
        )
        assertThat(result).isEqualTo(3)

    }


}