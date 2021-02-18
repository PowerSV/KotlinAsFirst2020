package lesson11.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag

internal class DimensionalValueTest {

    private fun assertApproxEquals(expected: DimensionalValue, actual: DimensionalValue, eps: Double) {
        assertEquals(expected.dimension, actual.dimension)
        assertEquals(expected.value, actual.value, eps)
    }

    @Test
    @Tag("12")
    fun base() {
        val first = DimensionalValue(1.0, "Kg")
        assertEquals(1000.0, first.value)
        assertEquals(Dimension.GRAM, first.dimension)
        val second = DimensionalValue("200 m")
        assertEquals(200.0, second.value)
        assertEquals(Dimension.METER, second.dimension)
        val fourth = DimensionalValue(2.2, "Km")
        assertEquals(2200.0, fourth.value)
        assertEquals(Dimension.METER, fourth.dimension)
        val sixth = DimensionalValue("1000 mm")
        assertEquals(1.0, sixth.value)
        assertEquals(Dimension.METER, sixth.dimension)
        val seventh = DimensionalValue("1000000000 ng")
        assertEquals(1.0, seventh.value)
        assertEquals(Dimension.GRAM, seventh.dimension)
        /*val eighth = DimensionalValue(1.0, "Kkg")
        assertEquals(1.0, eighth.value)
        assertEquals(Dimension.GRAM, eighth.dimension)
        */
        assertThrows(IllegalArgumentException::class.java) {
            DimensionalValue(1.0, "Kkg")
        }
        assertThrows(IllegalArgumentException::class.java) {
            DimensionalValue("200 2 m")
        }
    }

    @Test
    @Tag("6")
    fun plus() {
        assertApproxEquals(DimensionalValue("2 Km"), DimensionalValue("1 Km").plus(DimensionalValue("1000 m")), 1e-8)
        assertThrows(IllegalArgumentException::class.java) {
            DimensionalValue("1 g") + DimensionalValue("1 m")
        }
    }

    @Test
    @Tag("4")
    operator fun unaryMinus() {
        assertApproxEquals(DimensionalValue("-2 g"), DimensionalValue("2 g").unaryMinus(), 1e-12)
    }

    @Test
    @Tag("6")
    fun minus() {
        assertApproxEquals(DimensionalValue("0 m"), DimensionalValue("1 Km").minus(DimensionalValue("1000 m")), 1e-10)
        assertThrows(IllegalArgumentException::class.java) {
            DimensionalValue("1 g").minus(DimensionalValue("1 m"))
        }
    }

    @Test
    @Tag("4")
    fun times() {
        assertApproxEquals(DimensionalValue("2 Kg"), DimensionalValue("2 g").times(1000.0), 1e-8)
    }

    @Test
    @Tag("6")
    fun divValue() {
        assertEquals(1.0, DimensionalValue("3 m").div(DimensionalValue("3000 mm")), 1e-10)
        assertEquals(3.0, DimensionalValue("12 m").div(DimensionalValue("4000 mm")), 1e-10)
        assertThrows(IllegalArgumentException::class.java) {
            DimensionalValue("1 g").div(DimensionalValue("1 m"))
        }
    }

    @Test
    @Tag("4")
    fun divDouble() {
        assertApproxEquals(DimensionalValue("42 mm"), DimensionalValue("42 m").div(1000.0), 1e-11)
    }

    @Test
    @Tag("4")
    fun equals() {
        assertEquals(DimensionalValue("1 Kg"), DimensionalValue("1 Kg"))
        assertEquals(DimensionalValue("3 mm"), DimensionalValue("3 mm"))
    }

    @Test
    @Tag("4")
    fun hashCodeTest() {
        assertEquals(DimensionalValue("1 Kg").hashCode(), DimensionalValue("1 Kg").hashCode())
    }

    @Test
    @Tag("4")
    fun compareTo() {
        assertTrue(DimensionalValue("1 Kg") < DimensionalValue("1500 g"))
        assertTrue(DimensionalValue("1 m") > DimensionalValue("900 mm"))
    }
}