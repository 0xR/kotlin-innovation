# Kotlin FP libs


---

# Either

- We like either
- Avoids throwing excepitons

```kotlin {all|14-18|all}
fun parse(s: String): Either<NumberFormatException, Int> =
  if (s.matches(Regex("-?[0-9]+"))) Either.Right(s.toInt())
  else Either.Left(NumberFormatException("$s is not a valid integer."))

fun reciprocal(i: Int): Either<IllegalArgumentException, Double> =
  if (i == 0) Either.Left(IllegalArgumentException("Cannot take reciprocal of 0."))
  else Either.Right(1.0 / i)

fun stringify(d: Double): Either<Nothing, String> = Either.Right(d.toString())
fun eitherExample(s: String): Either<IllegalArgumentException, String> =
  parse(s).flatMap(::reciprocal).flatMap(::stringify)
  
fun main() {
  // prints: Either.Left(java.lang.NumberFormatException: blaa! is not a valid integer.)
  println(eitherExample("blaa!"))
  
  // prints: Either.Right(0.3333333333333333)
  println(eitherExample("3"))
}
```

---

# Algebraic Data Types in Kotlin

```kotlin {all|1|3-7|9-11}
sealed interface FileError

@JvmInline
value class SecurityError(val msg: String?) : FileError

@JvmInline
value class FileNotFound(val path: String) : FileError

object EmptyPath : FileError {
  override fun toString() = "EmptyPath"
}
```

---

# Effects

```kotlin {all|1|8,10|6|2-3}
fun readFile(path: String?): Effect<FileError, Content> = effect {
  ensureNotNull(path) { EmptyPath }
  ensure(path.isNotEmpty()) { EmptyPath }
  try {
    val lines = File("${System.getProperty("user.dir")}/$path").readLines()
    Content(lines)
  } catch (e: FileNotFoundException) {
    shift(FileNotFound(path))
  } catch (e: SecurityException) {
    shift(SecurityError(e.message))
  }
}
```

---

# Validation

```kotlin {all|4,10|6,12}
// Nel = NonEmptyList
fun FormField.contains(needle: String): ValidatedNel<ValidationError, FormField> =
  if (value.contains(needle, false))
    validNel()
  else
    ValidationError.DoesNotContain(needle).invalidNel()

fun FormField.maxLength(maxLength: Int): ValidatedNel<ValidationError, FormField> =
  if (value.length <= maxLength)
    validNel()
  else
    ValidationError.MaxLength(maxLength).invalidNel()
```

---

# Either - Fail Fast

- either blocks support binding over Validated values with no additional cost or need to convert first to Either

```kotlin {}
fun FormField.validateFailFast(): Either<Nel<ValidationError>, Email> =
    either.eager {
      contains("@").bind() // fails fast on first error found
      maxLength(250).bind()
      Email(value)
    }
    
fun main() {
    val fields = listOf(FormField("email", "asdf"));
    // prints: Either.Left(NonEmptyList(DoesNotContain(value=@)))
    println(fields.traverse { it.validateFailFast() })
}
```

---

# Either - Collect all errors
-  Where we get a bit lost

```kotlin {all|3|2,4-5|7|8|9}
fun FormField.validateErrorAccumulate(): ValidatedNel<ValidationError, Email> =
  contains("@").zip(
    Semigroup.nonEmptyList(), // accumulates errors in a non empty list
    maxLength(200),
    maxLength(10),
  ) {
    _, _, _ -> Email(value) 
  }.handleErrorWith { 
    ValidationError.NotAnEmail(it).invalidNel() 
  }

fun main() {
  val fields = listOf(FormField("email", "asdf"));
  fields.traverse(Semigroup.nonEmptyList()) {
    it.validateErrorAccumulate()
  }.toEither()
}
```

---

# Components

<div grid="~ cols-2 gap-4">
<div>

You can use Vue components directly inside your slides.

We have provided a few built-in components like `<Tweet/>` and `<Youtube/>` that you can use directly. And adding your
custom components is also super easy.

```html

<Counter :count="10"/>
```

<!-- ./components/Counter.vue -->
<Counter :count="10" m="t-4" />

Check out [the guides](https://sli.dev/builtin/components.html) for more.

</div>
<div>

```html

<Tweet id="1390115482657726468"/>
```

<Tweet id="1390115482657726468" scale="0.65" />

</div>
</div>

<!--
Presenter note with **bold**, *italic*, and ~~striked~~ text.

Also, HTML elements are valid:
<div class="flex w-full">
  <span style="flex-grow: 1;">Left content</span>
  <span>Right content</span>
</div>
-->


---
class: px-20
---

# Themes

Slidev comes with powerful theming support. Themes can provide styles, layouts, components, or even configurations for
tools. Switching between themes by just **one edit** in your frontmatter:

<div grid="~ cols-2 gap-2" m="-t-2">

```yaml
---
theme: default
---
```

```yaml
---
theme: seriph
---
```

<img border="rounded" src="https://github.com/slidevjs/themes/blob/main/screenshots/theme-default/01.png?raw=true">

<img border="rounded" src="https://github.com/slidevjs/themes/blob/main/screenshots/theme-seriph/01.png?raw=true">

</div>

Read more about [How to use a theme](https://sli.dev/themes/use.html) and
check out the [Awesome Themes Gallery](https://sli.dev/themes/gallery.html).

---
preload: false
---

# Animations

Animations are powered by [@vueuse/motion](https://motion.vueuse.org/).

```html

<div
        v-motion
        :initial="{ x: -80 }"
        :enter="{ x: 0 }">
    Slidev
</div>
```

<div class="w-60 relative mt-6">
  <div class="relative w-40 h-40">
    <img
      v-motion
      :initial="{ x: 800, y: -100, scale: 1.5, rotate: -50 }"
      :enter="final"
      class="absolute top-0 left-0 right-0 bottom-0"
      src="https://sli.dev/logo-square.png"
    />
    <img
      v-motion
      :initial="{ y: 500, x: -100, scale: 2 }"
      :enter="final"
      class="absolute top-0 left-0 right-0 bottom-0"
      src="https://sli.dev/logo-circle.png"
    />
    <img
      v-motion
      :initial="{ x: 600, y: 400, scale: 2, rotate: 100 }"
      :enter="final"
      class="absolute top-0 left-0 right-0 bottom-0"
      src="https://sli.dev/logo-triangle.png"
    />
  </div>

  <div
    class="text-5xl absolute top-14 left-40 text-[#2B90B6] -z-1"
    v-motion
    :initial="{ x: -80, opacity: 0}"
    :enter="{ x: 0, opacity: 1, transition: { delay: 2000, duration: 1000 } }">
    Slidev
  </div>
</div>

<!-- vue script setup scripts can be directly used in markdown, and will only affects current page -->
<script setup lang="ts">
const final = {
  x: 0,
  y: 0,
  rotate: 0,
  scale: 1,
  transition: {
    type: 'spring',
    damping: 10,
    stiffness: 20,
    mass: 2
  }
}
</script>

<div
  v-motion
  :initial="{ x:35, y: 40, opacity: 0}"
  :enter="{ y: 0, opacity: 1, transition: { delay: 3500 } }">

[Learn More](https://sli.dev/guide/animations.html#motion)

</div>

---

# LaTeX

LaTeX is supported out-of-box powered by [KaTeX](https://katex.org/).

<br>

Inline $\sqrt{3x-1}+(1+x)^2$

Block
$$
\begin{array}{c}

\nabla \times \vec{\mathbf{B}} -\, \frac1c\, \frac{\partial\vec{\mathbf{E}}}{\partial t} &
= \frac{4\pi}{c}\vec{\mathbf{j}} \nabla \cdot \vec{\mathbf{E}} & = 4 \pi \rho \\

\nabla \times \vec{\mathbf{E}}\, +\, \frac1c\, \frac{\partial\vec{\mathbf{B}}}{\partial t} & = \vec{\mathbf{0}} \\

\nabla \cdot \vec{\mathbf{B}} & = 0

\end{array}
$$

<br>

[Learn more](https://sli.dev/guide/syntax#latex)

---

# Diagrams

You can create diagrams / graphs from textual descriptions, directly in your Markdown.

<div class="grid grid-cols-3 gap-10 pt-4 -mb-6">

```mermaid {scale: 0.5}
sequenceDiagram
    Alice->John: Hello John, how are you?
    Note over Alice,John: A typical interaction
```

```mermaid {theme: 'neutral', scale: 0.8}
graph TD
B[Text] --> C{Decision}
C -->|One| D[Result 1]
C -->|Two| E[Result 2]
```

```plantuml {scale: 0.7}
@startuml

package "Some Group" {
  HTTP - [First Component]
  [Another Component]
}

node "Other Groups" {
  FTP - [Second Component]
  [First Component] --> FTP
}

cloud {
  [Example 1]
}


database "MySql" {
  folder "This is my folder" {
    [Folder 3]
  }
  frame "Foo" {
    [Frame 4]
  }
}


[Another Component] --> [Example 1]
[Example 1] --> [Folder 3]
[Folder 3] --> [Frame 4]

@enduml
```

</div>

[Learn More](https://sli.dev/guide/syntax.html#diagrams)

---
src: ./pages/multiple-entries.md
hide: false
---

---
layout: center
class: text-center
---

# Learn More

[Documentations](https://sli.dev) ?? [GitHub](https://github.com/slidevjs/slidev)
?? [Showcases](https://sli.dev/showcases.html)
