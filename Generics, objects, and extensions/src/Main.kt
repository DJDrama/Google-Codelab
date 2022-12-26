class FillInTheBlankQuestion(
    val questionText: String,
    val answer: String,
    val difficulty: String
)

class TrueOrFalseQuestion(
    val questionText: String,
    val answer: Boolean,
    val difficulty: String
)

class NumericQuestion(
    val questionText: String,
    val answer: Int,
    val difficulty: String
)

/** Use Generics For above classes
 * Use Data class instead of Class(Class toString() will print unique identifier of the object)
 * Data class automatically implements toString, equals, hashCode, components, copy
 * !! data class cannot be abstract, open, sealed, or inner
 * **/
data class Question<T>(
    val questionText: String,
    val answer: T,
    val difficulty: Difficulty
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}

/** Singleton
 * Use 'object' for singleton
 * A singleton object can't have a constructor(can't create instances directly)
 */
/*object StudentProgress {
    var total: Int = 10
    var answered: Int = 3
}*/


class Quiz {
    val question1 = Question<String>(
        questionText = "Quoth the raven ___",
        answer = "nevermore",
        difficulty = Difficulty.MEDIUM
    )
    val question2 = Question<Boolean>(
        questionText = "The sky is green. True or false",
        answer = false,
        difficulty = Difficulty.EASY
    )
    val question3 = Question<Int>(
        questionText = "How many days are there between full moons?",
        answer = 28,
        difficulty = Difficulty.HARD
    )

    /** Scope Functions
     * Allows you to concisely access properties and methods from a class without having to
     * repeatedly access the variable name.
     * - Are higher-order functions that allow you to access properties and methods of an object
     * without referring to the object's name.
     * - Body of the function passed in takes on the scope of the object that the scope function is called with
     * - Makes your code more readable by allowing you to omit the object name when including it is redundant.
     */
    // let()
    // refer to an object in a lambda expression using the identifier it, instead of the object's actual name.
    // - helps you avoid using a long, more descriptive object name repeatedly when accessing more than one property.
    fun printQuiz() {
        question1.let {
            println(it.questionText)
            println(it.answer)
            println(it.difficulty)
        }
        println()
        question2.let {
            println(it.questionText)
            println(it.answer)
            println(it.difficulty)
        }
        println()
        question3.let {
            println(it.questionText)
            println(it.answer)
            println(it.difficulty)
        }
        println()
    }

    // apply()
    // - can call objects before they have even been assigned to a variable.
    // - called on an object using dot notation.
    // - returns a reference to that object so that it can be stored in a variable.

    /** Companion Object
     * You can define a singleton object inside another class using a companion object
     * It allows you to access its properties and methods from inside the class,
     * if the object's properties and methods belong to that class,
     * allowing for more concise syntax
     */
    companion object StudentProgress {
        var total: Int = 10
        var answered: Int = 3
    }
}

/** Extensions Properties and Methods
 * Extension Properties can't store data, so they must be get-only
 *
 * gives more options to expose your code to other developers.
 * Using dot syntax on other types can make your code easier to read, both for yourself and other developers.
 *  */
val Quiz.StudentProgress.progressText: String
    get() = "${answered} of ${total} answered"

fun Quiz.StudentProgress.printProgressBar() {
    repeat(Quiz.answered) { print("▓") }
    repeat(Quiz.total - Quiz.answered) { print("▒") }
    println()
    println(Quiz.progressText)
}


fun main() {
    // println("${StudentProgress.answered} of ${StudentProgress.total} answered.")
    // println("${Quiz.answered} of ${Quiz.total} answered.")
    // println(Quiz.progressText)
    Quiz.printProgressBar()
    val quiz = Quiz().apply{
        printQuiz()
    }
}