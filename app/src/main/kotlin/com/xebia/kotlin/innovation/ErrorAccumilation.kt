import arrow.core.Either
import arrow.core.Nel
import arrow.core.ValidatedNel
import arrow.core.continuations.either
import arrow.core.handleErrorWith
import arrow.core.invalidNel
import arrow.core.traverse
import arrow.core.validNel
import arrow.core.zip
import arrow.typeclasses.Semigroup

sealed class ValidationError(val msg: String) {
  data class DoesNotContain(val value: String) : ValidationError("Did not contain $value")
  data class MaxLength(val value: Int) : ValidationError("Exceeded length of $value")
  data class NotAnEmail(val reasons: Nel<ValidationError>) : ValidationError("Not a valid email")
}

data class FormField(val label: String, val value: String)
data class Email(val value: String)

/** strategies **/
sealed class Strategy {
  object FailFast : Strategy()
  object ErrorAccumulation : Strategy()
}

/** Abstracts away invoke strategy **/
object Rules {
  private fun FormField.contains(needle: String): ValidatedNel<ValidationError, FormField> =
    if (value.contains(needle, false)) validNel()
    else ValidationError.DoesNotContain(needle).invalidNel()

  private fun FormField.maxLength(maxLength: Int): ValidatedNel<ValidationError, FormField> =
    if (value.length <= maxLength) validNel()
    else ValidationError.MaxLength(maxLength).invalidNel()

  private fun FormField.validateErrorAccumulate(): ValidatedNel<ValidationError, Email> =
    contains("@").zip(
      Semigroup.nonEmptyList(), // accumulates errors in a non empty list, can be omited for NonEmptyList
      maxLength(1),
      maxLength(200),
      maxLength(2)
    ) { _, _, _, _ -> Email(value) }.handleErrorWith { ValidationError.NotAnEmail(it).invalidNel() }

  /** either blocks support binding over Validated values with no additional cost or need to convert first to Either **/
  private fun FormField.validateFailFast(): Either<Nel<ValidationError>, Email> =
    either.eager {
      contains("@").bind() // fails fast on first error found
      maxLength(250).bind()
      Email(value)
    }

  operator fun invoke(strategy: Strategy, fields: List<FormField>): Either<Nel<ValidationError>, List<Email>> =
    when (strategy) {
      Strategy.FailFast ->
        fields.traverse { it.validateFailFast() }

      Strategy.ErrorAccumulation ->
        fields.traverse(Semigroup.nonEmptyList()) {
          it.validateErrorAccumulate()
        }.toEither()
    }
}

fun main() {
  val fields = listOf(FormField("email", "asdf"));
  println(Rules(Strategy.FailFast, fields))
  println(Rules(Strategy.ErrorAccumulation, fields))
}
