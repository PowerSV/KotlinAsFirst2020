@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Класс "Величина с размерностью".
 *
 * Предназначен для представления величин вроде "6 метров" или "3 килограмма"
 * Общая сложность задания - средняя, общая ценность в баллах -- 18
 * Величины с размерностью можно складывать, вычитать, делить, менять им знак.
 * Их также можно умножать и делить на число.
 *
 * В конструктор передаётся вещественное значение и строковая размерность.
 * Строковая размерность может:
 * - либо строго соответствовать одной из abbreviation класса Dimension (m, g)
 * - либо соответствовать одной из приставок, к которой
 * приписана сама размерность (Km, Kg, mm, mg)
 * - во всех остальных случаях следует бросить IllegalArgumentException
 */
class DimensionalValue(value: Double, dimension: String) : Comparable<DimensionalValue> {

    //private val dimensionString = dimension
    //private val firstValue = value
    //private val prefix = dimensionString.first().toString()
    //private val dimensionValues = Dimension.values()
    //private val prefixValues = DimensionPrefix.values()

    /**
     * Величина с БАЗОВОЙ размерностью (например для 1.0Kg следует вернуть результат в граммах -- 1000.0)
     */
    val value: Double
    /*= run {
        val prefix = dimension.first().toString()
        val dimensionValues = Dimension.values()
        val prefixValues = DimensionPrefix.values()
        when {
            dimensionValues.find { it.abbreviation == dimension } != null -> value
            (dimensionValues.find { dimension.endsWith(it.abbreviation) } != null) &&
                    (prefixValues.find { it.abbreviation == prefix } != null) &&
                    (dimensionValues.find { it.abbreviation == dimension } == null) ->
                value * prefixValues.find { it.abbreviation == prefix }!!.multiplier
            else -> throw IllegalArgumentException()
        }
    }*/


    /**
     * БАЗОВАЯ размерность (опять-таки для 1.0Kg следует вернуть GRAM)
     */
    val dimension: Dimension  /*= run {
        val dimensionValues = Dimension.values()
        val a = dimensionValues.find { it.abbreviation == dimension }
        val b = dimensionValues.find { it.abbreviation == dimension.removePrefix(dimension.first().toString()) }
        a ?: (b ?: throw java.lang.IllegalArgumentException())
    }*/

    init {
        val dimensionValues = Dimension.values()
        val prefixValues = DimensionPrefix.values()
        val suffix = dimensionValues.find { dimension.endsWith(it.abbreviation) }
            ?: throw java.lang.IllegalArgumentException()
        val myStringWithoutSuffix = dimension.removeSuffix(suffix.abbreviation)
        val prefix = prefixValues.find { myStringWithoutSuffix.startsWith(it.abbreviation) }

        if (prefix == null) {
            if (myStringWithoutSuffix.isNotEmpty()) throw java.lang.IllegalArgumentException()
            else this.value = value
        } else {
            if (myStringWithoutSuffix.removePrefix(prefix.abbreviation)
                    .isNotEmpty()
            ) throw java.lang.IllegalArgumentException()
            else this.value = value * prefix.multiplier
        }
        this.dimension = suffix
    }

    /**
     * Конструктор из строки. Формат строки: значение пробел размерность (1 Kg, 3 mm, 100 g и так далее).
     */
    constructor(s: String) : this(s.substringBefore(" ").toDouble(), s.substringAfter(" "))

    /**
     * Сложение с другой величиной. Если базовая размерность разная, бросить IllegalArgumentException
     * (нельзя складывать метры и килограммы)
     */
    operator fun plus(other: DimensionalValue): DimensionalValue {
        if (dimension == other.dimension) return DimensionalValue(value + other.value, dimension.abbreviation)
        else throw java.lang.IllegalArgumentException()
    }

    /**
     * Смена знака величины
     */
    operator fun unaryMinus(): DimensionalValue = DimensionalValue(-value, dimension.abbreviation)

    /**
     * Вычитание другой величины. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun minus(other: DimensionalValue): DimensionalValue {
        if (dimension == other.dimension) return this.plus(other.unaryMinus())
        else throw java.lang.IllegalArgumentException()
    }

    /**
     * Умножение на число
     */
    operator fun times(other: Double): DimensionalValue = DimensionalValue(value * other, dimension.abbreviation)

    /**
     * Деление на число
     */
    operator fun div(other: Double): DimensionalValue = DimensionalValue(value / other, dimension.abbreviation)

    /**
     * Деление на другую величину. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun div(other: DimensionalValue): Double {
        if (dimension == other.dimension) return value / other.value
        else throw IllegalArgumentException()
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean =
        this === other || other is DimensionalValue && (value == other.value) && (dimension == other.dimension)

    /**
     * Сравнение на больше/меньше. Если базовая размерность разная, бросить IllegalArgumentException
     */
    override fun compareTo(other: DimensionalValue): Int {
        if (dimension == other.dimension)
            return value.compareTo(other.value)
        else
            throw IllegalArgumentException()
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + dimension.hashCode()
        return result
    }
}

/**
 * Размерность. В этот класс можно добавлять новые варианты (секунды, амперы, прочие), но нельзя убирать
 */
enum class Dimension(val abbreviation: String) {
    METER("m"),
    GRAM("g");

}

/**
 * Приставка размерности. Опять-таки можно добавить новые варианты (деци-, санти-, мега-, ...), но нельзя убирать
 */
enum class DimensionPrefix(val abbreviation: String, val multiplier: Double) {
    KILO("K", 1000.0),
    MILLI("m", 0.001),
    NANO("n", 0.000000001)
}