@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import lesson1.task1.sqr
import java.lang.IllegalArgumentException

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
fun parse(s: String): Pair<Double, Double> {
    val myString = s.replace(" ", "")
    if (myString.isEmpty()) throw  IllegalArgumentException()
    val result = Regex("""(-?\d+(?:\.\d+)?)?(?:([+-]\d+(?:\.\d+)?)i)?""").matchEntire(myString)
        ?: throw IllegalArgumentException()
    val re = result.groupValues[1].toDoubleOrNull() ?: 0.0
    val im = result.groupValues[2].toDoubleOrNull() ?: 0.0
    return re to im
}

class Complex(val re: Double = 0.0, val im: Double = 0.0) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Конструктор из строки вида x+yi
     */
    constructor(s: String) : this(parse(s).first, parse(s).second)


    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(other.re + re, im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-re, -im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex = Complex(re * other.re - im * other.im, im * other.re + re * other.im)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex =
        Complex(
            (re * other.re + im * other.im) / (sqr(other.re) + sqr(other.im)),
            (im * other.re - re * other.im) / (sqr(other.re) + sqr(other.im))
        )

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean =
        this === other || other is Complex && other.re == re && other.im == im

    /**
     * Преобразование в строку
     */
    override fun toString(): String = if (im >= 0) "Complex($re+${im}i)"
    else "Complex($re${im}i)"

    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }
}
