(ns doclojure.guides.clojure
  "# Starting with Clojure

  ## Introduction

  This is an interactive guide to Clojure. Once you finish working through it you should understand not just how to get things done in Clojure but also what makes Clojure an interesting and appealing programming language. We'll introduce Clojure's features with some examples, you'll complete them, and at each stage you'll learn about the key concepts that have shaped Clojure's design.

  Clojure programmers often make a distinction between something that's 'easy' (close-at-hand, similar to other things you already know) and something that's 'simple' (uncomplicated by definition). Complicated things mix concepts together in a way that makes them harder to negotiate, simple things do not. Clojure is designed to be 'simple', which means that at all times the language avoids mixing concepts that should remain separate. It isn't necessarily designed to be 'easy', for instance you might find at first that Clojure is quite different to other languages you've used. But don't worry, you'll quickly find that you appreciate Clojure's simplicity and before you know it you'll find it easy too.

  Throughout this guide you'll find blocks of code that are incomplete and you'll fill in the blanks to produce a result that's `true`. We store progress in your browser so you can leave and return when you like. If you get stuck, try consulting the [clojure.core docs](https://clojuredocs.org/clojure.core) and experimenting at [a REPL](https://www.tryclojure.com/)")

(defn lisp
  "## Lisp

Let's address the elephant in the room. For a lot of programmers, Clojure's syntax is initially strange and off-putting. Brackets, or parentheses, are used in a way you've not seen them used before, and they're everywhere. This is because Clojure comes from the family of languages known as Lisps. As your knowledge of Clojure grows, you'll find this method of constructing programs using words surrounded by brackets actually makes things simpler, since there's very little syntax to learn. There's a power and consistency to the way code is structured in a Lisp that can't be achieved by other languages and once you understand the benefits this brings you might find it difficult to accept the limitations of languages that don't structure code in this way.

If we take a look at some Clojure code, you can see that when we invoke a function we use an expression that looks like some words or values enclosed in brackets:

```clj
(println \"hello\" \"world\")

(str \"sling\" \"shot\")
```

  In languages like JavaScript, C, or Python, when we want to invoke a function we put the function name first then next the list of arguments enclosed in brackets, like `println(\"hello\", \"world\")`. In a Lisp we represent this kind of expression as a list, where the first item in the list is the function we want to invoke, and the subsequent items are the arguments to the function, so `(println \"hello\" \"world\")`. Why do this? Representing code as lists of items is a very powerful technique. It means our entire program is simply a list, of lists, of lists, and so on, forming a tree-like structure, and the code we write is really no different to any other data we work with. This means that Lisp code can be easily manipulated just like data (a technique known as meta programming) and it also means that the fundamental syntax of the language is very small, regular and stable.

When you see round brackets in Clojure (that is `(` and `)`) it means a function is being invoked, the name of the function is the first item inside those round brackets and the rest of the items between the brackets are the function arguments. These lists of symbols enclosed in brackets are known as _symbolic expressions_ or _S-expressions_. Clojure also uses square brackets (`[` and `]`) and curly braces (`{` and `}`), but we'll discuss those later.

In many languages, mathematical operators like `+` and `-` and comparators like `=`, `<` and `>` are a special syntax of the language. In Clojure, they're functions like any other function. One implication of the Lisp syntax is that in Clojure mathematical expressions are represented using prefix notation, because the operator (the function being invoked) must come first:"
  []
  '((= (+ 1 2) _)
    (= (+ 1 2 3 4 5) _)
    (= (* 1 2 3 4 5) _)
    (= (/ (+ 30 40) (- 20 10)) _)
    (= (< 1 10) _)
    (= (< 1 10 100 1000) _)
    (= (>= 10 20) _)))

(defn values
  "## Values

  What's a value? It may be a word you've never given much thought to, but for Clojure programmers and users of other functional programming languages a value is something that has some important characteristics. Values are immutable (they can't be changed 'in-place', instead we must create a new value and leave the old one alone), they have a precise meaning that is not hidden or obscured, so they can easily be compared for equality with other values. Values can't 'do' anything, since they are simple facts that don't have behaviour. We can combine values to make a new value e.g. I have the values `1` and `2`, I can combine these into the list `'(1 2)` and this list is also a value.

Clojure allows us to create values easily. We have:

**Numbers:** e.g. `1`, `2`, `90000000`.

In Clojure these numbers are instances of java.lang.Long. If we write a large number outside the range −2^63 and 2^63 − 1 (the limits of Long) then Clojure will automatically use a BigInt to represent the value. If we explicitly want a BigInt we can add an `N` suffix, like `2N`. Though rarely used, there's also an octal syntax `0762` and hex syntax `0x123`.

**Decimals:** e.g. `1.1`, `5.5`, `2.44e9`.

These are instances of java.lang.Double. If we need arbitrary precision we can add an `M` suffix to create a BigDecimal, like `1.23903981039822091230M`.

**Ratios**: `1/2`, `7/9`.

Ratios are great for avoiding rounding errors, and they're created automatically by Clojure when division results in a value that is not a round number.

**Strings:** e.g. `\"hello world\"`, `\"a string \\\"with\\\" double quotes inside\"`

 **Characters:** e.g. `\\a`, `\\b`, `\\c`, `\\u00a9` (also written as `\\©`)

**Booleans:** `true` and `false`

And finally `nil`, which is similar to Java and JavaScript's `null` or Ruby's `nil`"
  []
  '((= (= '(1 2 3) '(1 2 3)) _)
    (= (inc 5) _)
    (= (inc 5.55) _)
    (= (/ 7 8) _)
    (= (type 5.5) _)
    (= (type 30) _)
    (= (first "hello") _)))

(defn keywords
  "### Keywords

  Keywords are words preceded by a colon, like `:foo` and `:bar` and `:first-name`. These values are used often in Clojure. Keywords are interned strings, which simply means that two appearances of the same keyword in your program will always refer to the exact same value in memory (which makes them very fast to compare). The Clojure keywords `:foo` and `:foo` will always be equal, so keywords are ideal as a key in an index like a map.

Keywords also have some other handy capabilities. You can execute a keyword as a function and pass in an associative structure like a map as an argument, the keyword will look itself up in the map."
  []
  '((= (= :hello :hello) _)
    (= (keyword "abc") _)
    (= (:first-name {:first-name "Sky"}) _)))

(defn truthiness
  "### Truthiness

Clojure divides the world of values into two camps, those that are considered to be logically true (truthy) and those that are considered to be logically false (falsey). This means that, for instance, when we use an `if` expression we can use any value as the 'condition', it doesn't have to be a boolean.

The boolean value `false` and the value `nil` are both considered 'logically false'. *Everything else* is considered logically true. This is another winning move for brevity, since this:

```clj
(if (not (nil? first-name))
  (dothis)
  (dothat))
```

can become this:

```clj
(if first-name
  (dothis)
  (dothat))
```

You'll often find opportunities to take advantage of the truthy/falsey nature of Clojure values to simplify your code. One thing to note is that Clojure is similar to Scheme and Racket in that the empty list is considered logically true (it is not `nil` or `false`). In Common Lisp an empty list is equivalent to `nil`, and considered logically false."
  []
  '((= (if 1 :a :b) _)
    (= (if '() :a :b) _)
    (= (if nil :a :b) _)))

(defn collections
  "### Collections

Clojure provides a variety of collection types that are easy to create and convenient to use. One important feature of these collections is that they are persistent. Once created, a collection is an immutable value that will never change, but if we want to add or remove a value we create a new collection based on the original and the original remains untouched. This means that you can share collections freely without worrying about unexpected modifications (you can even share them between threads). It would be highly inefficient to copy an entire collection each time we made a change, but Clojure implements a technique known as 'structural sharing' which means that most of the existing collection structure is reused as-is. Again, it's safe to reuse because it will never change. Every modification is achieved by creating a new collection.

Clojure uses a sophisticated and highly optimized implementation for its immutable collections based on hash array mapped tries (HAMTs). The HAMT is a widely-branching tree structure that allows fast lookups and efficient storage. This internal tree structure is also ideal to support the process of structural sharing, since entire subtrees can easily be reused when creating a new collection.

  Something to note about Clojure collections is that they don't have a type parameter - you can place values of different types into the same collection. This means that Clojure's collections are 'heterogeneous'. Also, Clojure lets us create collections using a 'literal' syntax, so we can create new collections (with values inside) very easily without having to invoke a series of functions to build them.

Let's look at some of these collection types."
  []
  ())

(defn collections-vectors
  "### Vectors

Vectors are similar to arrays, in that they are an ordered structure that allows efficient look-up by index. We define vectors in Clojure using square brackets:

```clj
[1, 2, 3]
[1 2 3)
[\"a\" \"b\" \"c\"]
[:foo true 1.5]
[]
```

Notice how our first example uses commas between the values but the subsequent examples omit the commas. This is because commas in Clojure are entirely optional - they're treated like whitespace - so you can add them if you think they improve readability but you don't have to (and they're most often omitted).

The vector is the structure you will reach for often when you want to create any ordered sequence of items, or an n-tuple like a pair.

  You can use the `get` function to retrieve an item from a vector, but vectors themselves are also functions. They're a function of their indexes, so invoked as a function and given an index, the vector will return the value at that index. Being able to treat a vector as a function can often shorten and simplify your code."
  []
  '((= (get [1 2 3 4 5] 3) _)
    (= ([:foo :bar :baz :qux] 2) _)
    (= (= [1 2 3] [1 2 3]) _)))

(defn collections-maps
  "### Maps

The map is the work-horse of Clojure data structures. Clojure programmers use maps all day. Where a Java programmer might reach to create a new class with getters and setters, a C# programmer might create a new class full of properties, a Scala programmer might create a case class, the Clojure programmer uses a map, typically using keywords as the map keys.

Maps are created using curly braces `{` and `}`, enclosing a list of keys and values:

```clj
{:a 1 :b 2}

{:first-name \"John\" :last-name \"Smith\" :height 180}
```

  Clojure maps are functions of their keys (what is a map, other than a simple function that takes a key and returns a value?) so you can invoke a map like a function and give it a key. When using a map in this way, you can also provide a second argument that will be used as the default return value in case the key you have given is not found."
  []
  '((= (get {:a 1 :b 2 :c 3} :b) _)
    (= ({:first "Jane" :last "Doe" :height 180} :last) _)
    (= ({:first "Jane" :last "Doe" :height 180} :age) _)
    (= ({:first "Jane" :last "Doe" :height 180} :age 200) _)))

(defn collections-sets
  "### Sets

  Clojure's sets are created using curly braces too but with a `#` symbol before the opening brace. A set is a collection of unique values, and you can check whether a value exists in a set in near constant time (it is in fact _O(log<sub>32</sub> n))_, rather than _O(1)_, but _log<sub>32</sub> n_ remains very small even for large _n_).

```clj
#{1 2 3}

#{\"a\" \"b\" \"c\"}
```

Just like a map, a set is a function too. When invoked as a function and given a value, either the value itself will be returned (it was present in the set) or `nil` will be returned (the value was not present)."
  []
  '((= (get #{:bear :cat :dog} :cat) _)
    (= (get #{:bear :cat :dog} :fox) _)
    ))

(defn collections-lists
  "### Lists

Clojure provides singly-linked lists, and you can create them using round brackets like:

```clj
'(1, 2, 3)
'(1 2 3)
'(\"a\" \"b\" \"c\")
'(:foo true 1.5)
```

When Clojure code is read, we've discussed how round brackets indicate that a function will be invoked (with the first item in the list indicating the function name). We also use round brackets to define a list of data values, but in this case we don't want to try to execute this as an expression so we indicate this by adding a quote symbol (`'`) before the opening bracket. This quoting sounds like an annoyance, but in practice you'll rarely use these singly-linked lists in Clojure so it's not something you'll have to think about often.

Looking at the lists above, you can see how Clojure code itself is also made up of expressions that are lists."
  []
  ())

(defn collections-as-functions
  "## Collections as functions

Collections in Clojure have another very nice feature: they are functions. A vector can be used as a function that when given an index produces the value at that index. A map can be used as a function that when given a key will produce the value stored with that key. A set can be used as a function that when given a value will produce the same value if (and only if) that value was present in the set.

This feature of Clojure often helps us with brevity. For instance, if we have a set, and we want to filter another sequence of values based on whether each value is present in the set, we can use the set itself as a filtering function. So this:

```clj
(filter #(get myset %) myvalues)
```

becomes this:

```clj
(filter myset myvalues)
```"
  []
  ())

(defn sequences
  "## Sequences

One of the most important abstractions for a Clojure developer is the sequence, or 'seq' for short. We've discussed a number of concrete collection types that Clojure allows you to create, but one of the best things about Clojure is that it provides a large core library of functions that can operate on any of these collections, *because they are all sequences* (internally, they implement the ISeq interface, but that's an implementation detail that Clojure developers never see or really have to think about).

  When you want to transform data in Clojure, it's usually helpful to think about your data as a sequence and then try to think through the series of sequence transformations that will get you to your destination.

Let's imagine that we want to find the total size of all image files in a directory. In an imperative language, we might implement a solution along these lines:

```clj
long total = 0;
for (f in directory.getFiles()) {
  if (isJpeg(f)) {
    total = total + f.size();
  }
}
return total;
```

We have a mutable variable `total` and we approach the problem by iterating through a collection of things and modifying our variable only when it's appropriate. The exact details aren't important, but iterating and mutating are commonly used together in this way.

So how would we approach this task in Clojure, a language that avoids mutable state and promotes problem solving by transforming sequences?

Let's break it down into three stages:

1. Filter our list of files down to only JPGs (take in a sequence of files, and `filter` it to create a smaller sequence)
2. Focus on the 'size' of our files (take in the filtered sequence of files and use `map` create turn this into a sequence of file sizes)
3. Sum the sizes to create a total (take in the sequence of file sizes, and `reduce` it with the `+` function to get the sum)

So with Clojure:
```clj
(reduce +
  (map :size
    (filter jpg? files)))
```

We've introduced `map`, `filter` and `reduce` which are all functions from Clojure's core library that operate on any sequence. There are many more functions like these in the Clojure core library and since these functions are written to operation on the abstract 'sequence' they can be applied to all kinds of collections. Clojure enthusiasts often cite the following quote:

> _\"It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures.\"_ —Alan Perlis

This approach, also described as 'many functions upon few abstractions', maximises reuse and library interoperability because all Clojure code can operate on the same small set of abstractions (functions and sequences).

All Clojure collections can be treated as 'sequences'. Maps are sequences of pairs (key and value). Vectors, Sets, Lists and Java Arrays are sequences of values. Strings are sequences of characters. We can treat all of these data structures as sequences implicitly, or we can explicitly create a sequence using the `seq` function.

We can also treat any Java Map, Set or List as a sequence since Clojure implements its sequencing behaviour against the Java interfaces java.util.Map and java.lang.Iterable and Clojure's persistent, immutable data structures implement the relevant Java interfaces like java.util.Map, java.util.List."
  []
  '((= (seq "hello") _)
    (= (seq {:a 1 :b 2 :c 3}) _)
    (= (filter odd? [1 2 3 4 5]) _)
    (= (map inc [1 2 3 4 5]) _)
    (= (reduce + [1 2 3 4 5]) _)))

(defn functions
  "## Functions

Since Cojure is a functional programming language, functions are 'first-class' values too. This means they can be passed as arguments to other functions, and a function can be a return value. So let's look at how we create functions.

When we want to declare a function and give it a name so that it can be called from elsewhere in our Clojure programme, we use `defn`. The first argument to `defn` is the function name (typically kebab-case) and the second argument is a vector of parameter names:

```clj
(defn add-three-things
  [a b c]
  (+ a b c))
```

Now we can call the function like `(add-three-things 3 9 7)`.

We can also create anonymous functions (functions that don't have an enduring name) like `(fn [a b c] (+ a b c))` or using a convenient shorthand `#(+ %1 %2 %3)`."
  []
  '((= (#(str %1 %2) "blue" "whale") _)
    (= ((fn [x] (* 2 x)) 44) _)))

(defn localbindings
  "## Local bindings

In imperative languages we often use local variables to hold and name intermediate values. To name local values in Clojure we use `let`, which is an expression that creates a 'binding' of a symbol (or name) to a value only within the _lexical_ scope of the `let` expression itself. Expressions within the body of a `let` will be evaluated and these expressions can refer to the symbols (or names) that are bound by the let.

Let's say we wanted to split some arithmetic into a few steps for readability, we could use `let` like:

```clj
(let [seconds-in-minute 60
      minutes-in-hour 60
      hours-in-day 24
      days-in-year 365]
  (* seconds-in-minute minutes-in-hour hours-in-day days-in-year))
```

The `let` receives a vector of bindings, which must be pairs of a symbol (name) and a value, then next the `let` receives some expression or expressions to execute with the bindings available. When we declare our bindings, we can also refer to symbols (names) we have already bound:

```clj
(let [cars 10
      wheels (* 4 cars)
      nuts (* 5 wheels)]
  nuts)
```"
  []
  '())

(defn evaluation-and-returns
  "## Evaluation and return values
  Clojure code is built by composing expressions, where the evaluation of an expression always produces a value. In some places though, we want to evaluate more than one expression and we don't care about the values produced, because those expressions have an important side-effect but the return value is not important.  A simple example of this would be a function that calls `println` many times to print to the console. We can evaluate many expressions, one after the other, using `do`:

```clj
(do (println \"one\")
    (println \"two\")
    (println \"three\")
```

  Clojure is not a 'pure' functional language, and so in many convenient places we are allowed to execute many expressions (presumably with side-effects) and keep the return value only of the last one. `do` allows us to explicitly group expressions together to create one expression, but within the body of `let`, or `when`, or `fn`, there is an 'implicit `do`' in place so that we can evaluate many expressions if necessary:

```clj
(let [p \"potato\"]
  (println \"one\" p)
  (println \"two\" p)
  (println \"three\" p))
```

  At times, an explicit `do` is required, for instance when we want to do more than one thing within one of the branches of an `if`. The reason being that the `if` form only allows us to pass one expression as each branch, so we often need a way of joining many expressions into one:

```clj
(if p
  (do (println \"one\" p)
      (println \"two\" p)
      (println \"three\" p))
  (println \"there is no p!\"))
```"
  []
  '())

(defn macros
  "## Macros

Macros are one of Clojure's super powers. We've talked already about how Clojure code is a tree-like structure of nested lists. This highly regular structure means that it's possible to manipulate Clojure code just like any other data. Macros are a way of using Clojure code _to produce Clojure code_. Macros can receive Clojure code and transform it into something else.

To understand how useful macros are, let's take a look at `cond` from the Clojure core:

```clj
cond
Takes a set of test/expr pairs. It evaluates each test one at a
time.  If a test returns logical true, cond evaluates and returns
the value of the corresponding expr and doesn't evaluate any of the
other tests or exprs.
```

So `cond` takes pairs, each is a condition with an expression. It's similar to a many if/else expressions chained together:

```clj
(cond
 (nil? x) \"x was nil\"
 (even? x) \"x was even\"
 (odd? x) \"x was odd\")
```

Since `cond` is a macro, it receives the code it is given (in this case, as a list) before anything is executed:

```
(nil? x) \"x was nil\" (even? x) \"x was even\" (odd? x) \"x was odd\"
```

So `cond` can manipulate this list of items (just like any other transformation of data), producing the following Clojure code:

```clj
(if (nil? x)
 \"x was nil\"
 (if (even? x)
    \"x was even\"
    (if (odd? x)
    \"x was odd\" nil)))
```

We can see how the original `cond` is neater and more convenient than writing our own long chain of `if` expressions. We can also see how `cond` effectively adds a high-level language feature by rewriting your code to use a more primitive existing feature (`if`).

  Clojure programmers don't often create new macros of their own. Creating your own macros can lead to programs that are harder to understand, because the actual Clojure code produced by your macro may not be obvious. Macros also bring limitations since a macro is not a first class value like a function, so cannot be passed to other functions or be composed with other functions in the way that all functions can. As a Clojure programmer, you'll regularly use macros from Clojure's core (and other libraries) even if you don't often create your own.

One final interesting benefit of macros receiving unevaluated Clojure code is that a macro can be used whenever we need to avoid evaluation. For instance, `cond` would be pretty useless if every argument (every condition and every expression) was evaluated to produce results before `cond` was invoked. In the next sections, we'll expand on this and discuss the ways in which Clojure can be 'lazy' and when that's useful."
  []
  ())

(defn strictness
  "## Strictness, non-strictness and delayed execution

Clojure uses 'strict evaluation' (also known as 'eager evaluation'). This means that the arguments to Clojure functions are always evaluated before the function is called. This is the approach you'll be used to if you're coming from languages such as Java, JavaScript, Python, C# or Scala (and many others).

For example, the arguments in expression:

```clj
(+ 10 (+ 40 50) (+ 80 20))
```

Will be evaluated like this:

```clj
10 => 10
(+ 40 50) => 90
(+ 80 20) => 100
(+ 10 90 100) => 200
```

This is in contrast to languages which provide non-strict evaluation, where an expression given as an argument can remain unevaluated until (and unless) the function being called requires the value. If the function being called has some conditional logic and executes a path in which some arguments are never used, those arguments are never evaluated. Haskell is one of the few modern languages to provide non-strict evaluation by default.

  These rules apply to function calls, but since all code in Clojure is an expression, what do we do if we need to avoid evaluating the arguments to a function before the function is called? For instance, let's take `when` as an example:

```clj
(when mycondition
 (dothis))
```

This code would be pretty useless if we evaluated `mycondition` _and_ `(dothis)` before passing the result of both to `when`! That wouldn't be a conditional statement at all.

  The answer is that `when` in Clojure isn't a function, it's a macro. Remember, macros receive Clojure code so they have a chance to manipulate or transform it before it is executed. In the example above, the `when` macro will receive the code `mycondition (dothis)` and can decide exactly what should be evaluated. So macros are a great way to delay evaluation.

Another good example would be the use of macros for logging. If we consider a macro `warn` that receives a body and logs a warning, the macro may evaluate the body (and produce a value to be logged) only when the configured logging level indicates that the value would be logged."
  []
  ())

(defn lazy-sequences
  "## Lazy sequences

There's another important Clojure feature related to delayed evaluation: lazy sequences. A lazy sequence is one that calculates its contents only as it is needed. If we don't consume the sequence, then the values remain unrealized.

What's great about lazy sequences is that they can be infinite (for instance, the sequence of positive integers, or a sequence containing the digits of pi, or a sequence that contains the string `\"hello\"` repeated _ad infinitum_). As long as we only use a limited number of items from this kind of sequence, Clojure can represent these infinite, lazily-evaluated sequences and be ready to generate values on demand."
  []
  '((= (take 3 (range)) _)
    (= (take 5 (iterate #(* 2 %) 1)) _)
    (= (take 3 (repeat "hello")) _)))

(defn threading
  "## Threading

  We've discovered so far that Clojure code is made up of nested expressions, and that these expressions form a regular tree-like structure that can easily be manipulated by macros. The Clojure core library includes a family of very useful macros known as the 'threading' macros. The macros `->` and `->>` (and others that are related) allow us to write our code more clearly, as a series of steps, because the macros will rewrite that code into the nested structure required for execution.

Let's revisit our sequence transformation example, in which we filtered, mapped and reduced a sequence of files:

```clj
(reduce +
  (map :size
    (filter jpg? files)))
```

On seeing this code you might have felt that reading it is a strange experience. You start reading in the centre and work outwards to understand the order in which the three stages take place. One solution would be to create local bindings using `let`, which would make it more obvious that this is a series of steps:

```clj
(let [jpg-files (filter jpg? files)
      sizes (map :size jpg-files)
      total (reduce + sizes)
  total)
```

We can certainly see the three steps more clearly now, and we no longer have to read this code 'inside out'. However we've had to do quite a lot of work here and introduce a lot of local bindings. Let's try the same using the `->>` macro:

```clj
(->> files
     (filter jpg?)
     (map :size)
     (reduce +))
```

  The threading macro `->>` will take a value it is given and place it into the last argument position of the next expression. It will rewrite our code so that `files` is inserted into the `(filter jpg?)` expression at the end. Then it will continue to rewrite so that the resulting code is placed into the last position in the next expression, and so on. The result is that we can write our code as a very clear list of steps, but the code is rewritten to produce the correct nesting. We can use the Clojure function `macroexpand` to see what the macro produces:

```clj
(macroexpand '(->> files
                   (filter jpg?)
                   (map :size)
                   (reduce +)))

=>
(reduce + (map :size (filter jpg? files)))
```

The `->` macro is similar to `->>`, but it places the value given into the *first* argument position in the next expression. So we can write code like:

```clj
(-> \"123\" (str \"abc\") (str \"xyz\"))
```

And know that it will be rewritten to:

```clj
(str (str \"123\" \"abc\") \"xyz\")
```

  You might initially find the symbols `->>` and `->` seem arcane and off-putting. It's possible to write useful Clojure programs without ever using these threading macros. When you read Clojure code written by others though, you'll see these macros used often because of the way they make the order of a multi-step process very clear.

One thing to note, when you include an expression in your threading that doesn't require any other arguments than the one that will be threaded, the parentheses in this expression are optional, therefore the following are equivalent:

```clj
(pos? (dec (inc 1)))

(-> 1
    (inc)
    (dec)
    (pos?))

(-> 1 inc dec pos?)
```

You may notice that Clojure functions that operate on sequences tend to receive the seq as the last argument, so we can compose them with `->>`. Functions that transform a single value tend to receive that value as the first argument, so we can compose them with `->`. If you apply these rules of thumb when creating your own functions you'll find that your functions fit naturally into threaded operations too."
  []
  '((= (-> 5 inc inc (* 2)) _)
    (= (->> "hello world!" reverse (take 2) reverse first) _)
    (= (->> (range) (take 10) (filter even?) (reduce +)) _)
    (= (->> "abcdefg" (partition 2) second) _)))

(def ^:private vars
  [#'lisp
   #'values
   #'keywords
   #'truthiness
   #'collections
   #'collections-vectors
   #'collections-maps
   #'collections-sets
   #'collections-lists
   #'collections-as-functions
   #'sequences
   #'functions
   #'localbindings
   #'evaluation-and-returns
   #'macros
   #'strictness
   #'lazy-sequences
   #'threading])
