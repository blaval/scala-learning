# Useful methods on Seq collection

Available in Scala 2.13

## Table of content

1. [I do not want to do any transformation on the collection elements](#i-do-not-want-to-do-any-transformation-on-the-collection-elements)
    1. [I want only 1 result](#i-want-only-1-result)
        1. [The result must be true or false](#the-result-must-be-true-or-false)
        2. [The result must be Int](#the-result-must-be-int)
        3. [The result must be an element of the collection](#the-result-must-be-an-element-of-the-collection)
        4. [The result must be a computation based on the collection elements](#the-result-must-be-a-computation-based-on-the-collection-elements)
    2. [I want to change the order of the elements](#i-want-to-change-the-order-of-the-elements)
    3. [I want a part of the collection](#i-want-a-part-of-the-collection)
    4. [I want to remove elements](#i-want-to-remove-elements)
    5. [I want to cast to another collection type](#i-want-to-cast-to-another-collection-type)
2. [I want to do some transformation on the collection elements](#i-want-to-do-some-transformation-on-the-collection-elements)
    1. [I want only one result](#i-want-only-one-result)
    2. [I want to get the same number of elements or less](#i-want-to-get-the-same-number-of-elements-or-less)
        1. [I want to get the same number of elements](#i-want-to-get-the-same-number-of-elements)
        2. [I want to remove and transform elements](#i-want-to-remove-and-transform-elements)
        3. [I want to combine elements](#i-want-to-combine-elements)
    3. [I want to do something else](#i-want-to-do-something-else)

## I do not want to do any transformation on the collection elements

### I want only 1 result

#### The result must be true or false

[**.contains**](#table-of-content)

```scala
// Return true if the element is in the collection.
Seq(1, 2).contains(1) // true
Seq(1, 2).contains(3) // false
```

[**.containsSlice**](#table-of-content)

```scala
// Return true if this sequence contains a slice with the same elements as that, otherwise false.
Seq(1, 2, 4, 3).containsSlice(Seq(2, 4)) // true
Seq(1, 2, 4, 3).containsSlice(Seq(2, 3)) // false
```

[**.corresponds**](#table-of-content)

```scala
/*
Return true if both collections have the same length and the given condition is true for all 
corresponding elements x of this iterator and y of that, otherwise false.
*/
Seq(1, 2, 3).corresponds(Seq(1, 2, 3))(_ == _) // true
Seq(1, 2, 3).corresponds(Seq(1, 2))(_ == _) // false
Seq(1, 2, 3).corresponds(Seq(1, 2, 3, 4))(_ == _) // false
Seq(1, 2, 4, 3).corresponds(Seq(1, 0, 0, 1))(_ % 2 == _) // true
Seq(1, 2, 4, 3).corresponds(Seq(1, 0, 0, 0))(_ % 2 == _) // false
```

[**.exists**](#table-of-content)

```scala
// Return true if the given condition is satisfied by at least one element of this collection, otherwise false.
Seq(1, 2, 3).exists(_ % 2 == 0) // true
Seq(1, 3, 3).exists(_ % 2 == 0) // false
```

[**.forall**](#table-of-content)

```scala
// Return true if the given condition is satisfied for all elements of this collection, otherwise false.
Seq(2, 4, 6).forall(_ % 2 == 0) // true
Seq(2, 4, 5).forall(_ % 2 == 0) // false
```

[**.isEmpty**](#table-of-content)

```scala
// Return true if collection is empty, otherwise false
Seq(2, 4, 6).isEmpty // false
Seq().isEmpty // true
```

[**.sameElements**](#table-of-content)

```scala
/*
Return true if all the elements of this collection are the same and in the same order as those 
of the other given collection.
 */
Seq(2, 4, 6).sameElements(Seq(2, 4, 6)) // true
Seq(2, 4, 6).sameElements(Seq(2, 4, 6, 7)) // false
Seq(2, 4, 6).sameElements(Seq(2, 4)) // false
Seq(2, 4, 6).sameElements(Seq(2, 6, 4)) // false
```

#### The result must be Int

[**.indexOf**](#table-of-content)

```scala
/*
Return the index >= 0 of the first element of this sequence that is equal (as determined by ==) to elem, 
or -1, if none exists.
 */
Seq(2, 4, 6).indexOf(2) // 0
Seq(2, 4, 6).indexOf(6) // 2
Seq(2, 4, 6).indexOf(7) // -1
```

[**.indexOfSlice**](#table-of-content)

```scala
/*
Return the first index >= 0 such that the elements of this sequence starting at this index match the 
elements of sequence that, or -1 of no such subsequence exists.
 */
Seq(2, 4, 6).indexOfSlice(Seq(2, 4)) // 0
Seq(2, 4, 6).indexOfSlice(Seq(2, 4), 1) // -1
Seq(2, 4, 6).indexOfSlice(Seq(2, 6)) // -1
Seq(2, 4, 6).indexOfSlice(Seq(4, 4)) // -1
Seq(2, 4, 6).indexOfSlice(Seq(4, 6)) // 1
Seq(2, 4, 6).indexOfSlice(Seq(6, 4)) // -1
```

[**.indexWhere**](#table-of-content)

```scala
/*
Return the index >= from of the first element of this sequence that satisfies the given condition, 
or -1, if none exists.
 */
Seq(2, 4, 6).indexWhere(_ > 5) // 2
Seq(2, 4, 6).indexWhere(_ > 6) // -1
```

#### The result must be an element of the collection

[**.find**](#table-of-content)

```scala
/*
Return an option value containing the first element in the collection that satisfies the given condition, 
or None if none exists.
 */
Seq(2, 4, 6, 7).find(_ > 5) // Some(6)
Seq(2, 4, 6, 6).find(_ > 6) // None
```

[**.findLast**](#table-of-content)

```scala
/*
Return an option value containing the last element in the collection that satisfies the given condition, 
or None if none exists.
 */
Seq(2, 4, 6, 7).findLast(_ > 5) // Some(7)
Seq(2, 4, 6, 6).findLast(_ > 6) // None
```

[**.head**](#table-of-content)

```scala
// Return the first element of this iterable collection.
Seq(2, 4, 6, 7).head // Some(2)
Seq().head // java.util.NoSuchElementException: head of empty list
```

[**.headOption**](#table-of-content)

```scala
// Return the first element of this collection if it is non empty, None if it is empty.
Seq(2, 4, 6, 7).headOption // Some(2)
Seq().headOption // None
```

[**.maxBy**](#table-of-content)

```scala
/*
Return the first element of this collection with the largest value measured by function f with respect 
to the implicit ordering.
 */
Seq(2, 4, 6, 7).maxBy(identity) // 7
Seq(2, 4, 6, 7).maxBy(_ * -1) // 2

case class Person(age: Int)

Seq(Person(28), Person(80), Person(40)).maxBy(_.age) // Person(80)
Seq[Int]().maxBy(_ > 3) // java.lang.UnsupportedOperationException: empty.maxBy
```

[**.maxByOption**](#table-of-content)

```scala
/*
Return an option value containing the first element of this collection with the largest value measured 
by function f with respect to the implicit ordering.
*/
Seq(2, 4, 6, 7).maxByOption(identity) // Some(7)
Seq(2, 4, 6, 7).maxByOption(_ * -1) // Some(2)

case class Person(age: Int)

Seq(Person(28), Person(80), Person(40)).maxByOption(_.age) // Some(Person(80))
Seq[Int]().maxByOption(_ > 3) // None
```

[**.min**](#table-of-content)

```scala
// Return the smallest element of this collection with respect to the implicit ordering.
Seq(2, 4, 6, 7).min // 2
Seq(2, 4, 6, 7).min // 7
```

[**.minBy**](#table-of-content)

```scala
/*
Return the first element of this collection with the smallest value measured by function f with respect 
to the implicit ordering.
 */
Seq(2, 4, 6, 7).minBy(identity) // 2
Seq(2, 4, 6, 7).minBy(_ * -1) // 7

case class Person(age: Int)

Seq(Person(28), Person(80), Person(40)).minBy(_.age) // Person(28)
Seq[Int]().minBy(_ > 3) // java.lang.UnsupportedOperationException: empty.minBy
```

[**.minByOption**](#table-of-content)

```scala
/*
Return an option value containing the first element of this collection with the smallest value measured 
by function f with respect to the implicit ordering.
 */
Seq(2, 4, 6, 7).minByOption(identity) // Some(7)
Seq(2, 4, 6, 7).minByOption(_ * -1) // Some(2)

case class Person(age: Int)

Seq(Person(28), Person(80), Person(40)).minByOption(_.age) // Some(Person(80))
Seq[Int]().minByOption(_ > 3) // None
```

#### The result must be a computation based on the collection elements

[**.count**](#table-of-content)

```scala
// Return the number of elements satisfying the given condition.
Seq(2, 4, 6, 7).count(_ % 2 == 0) // 3
Seq(2, 4, 6, 7).count(_ > 5) // 2
```

[**.length**](#table-of-content)

```scala
// Return the length (number of elements) of this collection.
Seq(2, 4, 6, 7).length // 4
```

[**.size**](#table-of-content)

```scala
// Return the length (number of elements) of this collection. size is an alias for length in Seq collections.
Seq(2, 4, 6, 7).size // 4
```

[**.sum**](#table-of-content)

```scala
// Return the sum of all elements of this collection with respect to the + operator in num.
Seq(2, 4, 6, 7).sum // 4
```

### I want to change the order of the elements

[**.reverse**](#table-of-content)

```scala
// Return a new collection with all elements of this sequence in reversed order.
Seq(2, 4, 6, 7).reverse // Seq(7, 6, 4, 2)
```

[**.sortBy**](#table-of-content)

```scala
// Return a collection consisting of the elements of this collection sorted according to the given ordering where x < y if ord.lt(f(x), f(y)).
Seq("The", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog").sortBy(x => (x.length, x.head)) //  Seq("The", "dog", "fox", "the", "lazy", "over", "brown", "quick", "jumped") this works because scala.Ordering will implicitly provide an Ordering[Tuple2[Int, Char]]
Seq("The", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog").sortBy(_.length) //  Seq("The", "fox", "the", "dog", "over", "lazy", "quick", "brown", "jumped")
Seq("The", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog").sortBy(_.length)(Ordering.Int.reverse) //  Seq("jumped", "quick", "brown", "over", "lazy", "The", "fox", "the", "dog")
```

[**.sorted**](#table-of-content)

```scala
// Return a collection consisting of the elements of this collection sorted according to the ordering ord
Seq("The", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog").sorted //  Seq("The", "brown", "dog", "fox", "jumped", "lazy", "over", "quick", "the")
Seq("The", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog").sorted(Ordering.String) //  Seq("The", "brown", "dog", "fox", "jumped", "lazy", "over", "quick", "the")
Seq("The", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog").sorted(Ordering.String.reverse) //  Seq("the", "quick", "over", "lazy", "jumped", "fox", "dog", "brown", "The")
```

[**.sortWith**](#table-of-content)

```scala
// Return a collection consisting of the elements of this collection sorted according to the given comparison function lt=lowerthan.
Seq("The", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog").sortWith(_.length > _.length) //  Seq("jumped", "quick", "brown", "over", "lazy", "The", "fox", "the", "dog")
Seq("Steve", "Tom", "John", "Bob").sortWith(_.compareTo(_) < 0) //  Seq("Bob", "John", "Steve", "Tom")
```

### I want a part of the collection

[**.indices**](#table-of-content)

```scala
// Return a Range value from 0 to one less than the length of this sequence.
Seq(1, 2, 2, 3, 4, 3).indices // Range 0 until 6
Seq(1).indices // Range 0 until 6
Seq.empty[Int].indices // empty Range 0 until 0
```

[**.init**](#table-of-content)

```scala
/*
Return the initial part of the collection without its last element. Note: Even when applied to 
a view or a lazy collection it will always force the elements.
 */
Seq(1, 2, 2, 3, 4, 3).init // Seq(1, 2, 2, 3, 4)
Seq(1).init // Seq()
Seq.empty[Int].init // java.lang.UnsupportedOperationException: init of empty list
```

[**.inits**](#table-of-content)

```scala
// Return an iterator over all the inits of this iterable collection
Seq(1, 2, 3).inits // Iterator(List(1, 2, 3), List(1, 2), List(1), Nil)
Seq(1).inits // Iterator(List(1), Nil)
Seq.empty[Int].inits // Iterator(List(1), Nil)
```

[**.tail**](#table-of-content)

```scala
// Return the rest of the collection without its first element.
Seq(1, 2, 3).tail // Seq(2, 3)
Seq(1).tail // Seq()
Seq.empty[Int].tail // java.lang.UnsupportedOperationException: tail of empty list
```

### I want to remove elements

[**.diff**](#table-of-content)

```scala
/* 
Return a new collection which contains all elements of this collection except some of occurrences 
of elements that also appear in the other collection. If an element value x appears n times in the 
other collection, then the first n occurrences of x will not form part of the result, 
but any following occurrences will.
 */
Seq(1, 2, 3, 4, 4).diff(Seq(0, 2, 4)) // Seq(1, 3, 4)
Seq.empty[Int].diff(Seq(0, 2, 4)) // Seq()
```

[**.distinct**](#table-of-content)

```scala
// Return a new collection containing all the elements of the original collection without duplicates.
Seq(1, 2, 2, 3, 4, 3).distinct //  Seq(1, 2, 3, 4)
```

[**.distinctBy**](#table-of-content)

```scala
/*
Return a new collection consisting of all the elements of this collection without duplicates.
The duplicates are evaluated from the transforming function applied on each element
 */
Seq(1, 2, 2, 3, 4, 3).distinctBy(identity) //  Seq(1, 2, 3, 4) same result as Seq(1, 2, 2, 3, 4, 3).distinct
Seq(1, 2, 2, 3, 4, 3).distinctBy(_ % 2 == 0) //  Seq(1, 2)
```

[**.drop**](#table-of-content)

```scala
/* 
Return a collection consisting of all elements of this collection except the first n ones,
or else the empty collection, if this collection has less than n elements. If n is negative, do not drop any elements.
 */
Seq(1, 2, 3).drop(-1) //  Seq(1, 2, 3) do nothing
Seq(1, 2, 3).drop(0) //  Seq(1, 2, 3) do nothing
Seq(1, 2, 3).drop(1) //  Seq(2, 3)
```

[**.dropRight**](#table-of-content)

```scala
/* 
Return a iterable collection consisting of all elements of this iterable collection except 
the last n ones, or else the empty iterable collection, if this iterable collection has less 
than n elements. If n is negative, don not drop any elements.
 */
Seq(1, 2, 3).dropRight(-1) //  Seq(1, 2, 3) do nothing
Seq(1, 2, 3).dropRight(0) //  Seq(1, 2, 3) do nothing
Seq(1, 2, 3).dropRight(1) //  Seq(1, 2)
```

[**.dropWhile**](#table-of-content)

```scala
/* 
Return an iterable collection consisting of all elements of this iterable collection except the last n ones, 
or else the empty iterable collection, if this iterable collection has less than n elements. 
If n is negative, do not drop any elements.
 */
Seq(1, 2, 3, 2).dropWhile(_ < 3) //  Seq(3, 2)
```

[**.filter**](#table-of-content)

```scala
/* 
Return a new collection consisting of all elements of this collection that satisfy the given condition. 
The order of the elements is preserved.
 */
Seq(1, 2, 3, 2, 4).filter(_ < 3) //  Seq(1, 2, 2)
```

[**.filterNot**](#table-of-content)

```scala
/* 
Return a new collection consisting of all elements of this collection that do not satisfy the given condition. 
The order of the elements is preserved.
 */
Seq(1, 2, 3, 2, 4).filterNot(_ < 3) //  Seq(3, 4)
```

[**.intersect**](#table-of-content)

```scala
/* 
Return a new sequence which contains all elements of this sequence which also appear in that. 
If an element value x appears n times in that, then the first n occurrences of x will be retained in 
the result, but any following occurrences will be omitted
 */
Seq(1, 2).intersect(Seq(0, 2, 2, 10)) // Seq(2)
Seq(1, 2, 2).intersect(Seq(0, 2, 10)) // Seq(2)
```

[**.partition**](#table-of-content)

```scala
/* 
Return a pair of, first, all elements that satisfy the given condition and, second, all elements that do not. 
It splits a collection into two based on a given condition.
 */
Seq(1, 2, 2, 3).partition(_ > 1) // (Seq(2, 2, 3),Seq(1))
Seq.empty[Int].partition(_ > 1) // (Seq(),Seq())
```

[**.slice**](#table-of-content)

```scala
/* 
Return a collection containing the elements greater than or equal to index from extending 
up to (but not including) index until of this collection.
 */
Seq(1, 2, 3, 4).slice(2, 3) // Seq(3)
Seq(1, 2, 3, 4).slice(2, 2) // Seq()
Seq(1, 2, 3, 4).slice(2, 10) // Seq(3, 4)
Seq(1, 2, 3, 4).slice(-1, 2) // Seq(1, 2)
```

[**.span**](#table-of-content)

```scala
/* 
Return a pair consisting of the longest prefix of this collection whose elements all satisfy the given condition, 
and the rest of this collection.
 */
Seq(1, 2, 3, 4).span(_ < 3) // (Seq(1, 2),Seq(3, 4))
Seq(3, 2, 3, 4).span(_ < 3) // (Seq(),Seq(1, 2, 3, 4))
```

[**.splitAt**](#table-of-content)

```scala
// Return a pair of collections consisting of the first n elements of this collection, and the other elements.
Seq(1, 2, 3, 4).splitAt(1) // (Seq(1),Seq(2, 3, 4))
Seq(1, 2, 3, 4).splitAt(-1) // (Seq(),Seq(1, 2, 3, 4))
Seq(1, 2, 3, 4).splitAt(0) // (Seq(),Seq(1, 2, 3, 4))
```

[**.take**](#table-of-content)

```scala
/* 
Return a collection consisting only of the first n elements of this collection, or else 
the whole collection, if it has less than n elements. If n is negative, returns an empty collection.
 */
Seq(1, 2, 3, 4).take(2) // Seq(1, 2)
Seq(1, 2, 3, 4).take(0) // Seq()
Seq(1, 2, 3, 4).take(-1) // Seq()
```

[**.takeRight**](#table-of-content)

```scala
/* 
Return an iterable collection consisting only of the last n elements of this iterable collection, 
or else the whole iterable collection, if it has less than n elements. 
If n is negative, returns an empty iterable collection.
 */
Seq(1, 2, 3, 4).takeRight(2) // Seq(3, 4)
Seq(1, 2, 3, 4).takeRight(0) // Seq()
Seq(1, 2, 3, 4).takeRight(-1) // Seq()
```

[**.takeWhile**](#table-of-content)

```scala
// Return the longest prefix of this iterable collection whose elements all satisfy the given condition.
Seq(1, 2, 3, 4).takeWhile(_ > 3) // Seq()
Seq(1, 2, 3, 4).takeWhile(_ < 3) // Seq(1, 2)
```

### I want to cast to another collection type

[**.toList**](#table-of-content)

```scala
// Return a List containing the same elements as in the collection in the same order.
Seq(1, 2, 3, 4).toList // List(1, 2, 3, 4)
```

[**.toSet**](#table-of-content)

```scala
// Return a Set containing the de-duplicated elements from the collection in the same order.
Seq(1, 2, 2, 4, 4).toSet // Set(1, 2, 4)
```

[**.toMap**](#table-of-content)

```scala
// Return a Map containing the same elements as in the collection.
Seq((1, 2), (3, 4)).toMap // Map[Int,Int] = Map(1 -> 2, 3 -> 4)
```

[**.toVector**](#table-of-content)

```scala
// Return a Map containing the same elements as in the collection in the same order.
Seq(1, 2, 3, 4).toVector // Vector(1, 2, 3, 4)
```

## I want to do some transformation on the collection elements

[**.foreach**](#table-of-content)

```scala
// Return Nothing. Apply the transformation `f` to each element for its side effects
Seq(1, 2).foreach(println) // will println 1 then 2 and return nothing 
```

### I want only one result

[**.collectFirst**](#table-of-content)

```scala
/*
Return an option value containing partial function applied to the first value for which it is defined, 
or None if none exists.
*/
case class Adult(age: Int)

Seq(16, 30, 45, 90).collectFirst { case age if age > 18 && age < 65 => Adult(age) } // Some(Adult(30))
```

[**.mkString**](#table-of-content)

```scala
/*
Return a string representation of this collection. In the resulting string, the string representations 
(w.r.t. the method toString) of all elements of this collection are separated by the string sep.
*/
Seq(16, 30, 90).mkString("; ") // "16; 30; 90"
Seq(16, 30, 90).mkString("[", "; ", "]") // "[16; 30; 90]"
```

### I want to get the same number of elements or less

#### I want to get the same number of elements

[**.map**](#table-of-content)

```scala
/*
Return a new collection resulting from applying the given function f to each element of 
this collection and collecting the results.
*/
Seq(16, 30, 90).map(_ + 1) // Seq(17, 31, 91)
```

#### I want to remove and transform elements

[**.collect**](#table-of-content)

```scala
/*
Return a new collection resulting from applying the given partial function pf to each element 
on which it is defined and collecting the results. The order of the elements is preserved.
Similar as map(f).filter(p)
*/
case class Adult(age: Int)

Seq(16, 30, 45, 90).collect { case age if age > 18 && age < 65 => Adult(age) } // Seq(Adult(30), Adult(45))
```

#### I want to combine elements

[**.fold**](#table-of-content)

```scala
// Return the result of applying the fold operator op between all the elements and z, or z if this collection is empty.
Seq(1, 2, 3).fold(1)(_ + _) // 7
```

[**.foldLeft**](#table-of-content)

```scala
/*
Return the result of inserting op between consecutive elements of this collection, going left to right 
with the start value z on the left: op(...op(z, x,,1,,), x,,2,,, ..., x,,n,,) where x,,1,,, ..., x,,n,, 
are the elements of this collection. Returns z if this collection is empty.
 */
Seq(1, 2, 3).foldLeft(1)(_ + _) // 7
Seq(1, 2, 3).foldLeft(Seq.empty[String]) { case (acc, elt) => acc :+ s"value $elt" } // Seq("value 1", "value 2", "value 3")
```

[**.foldRight**](#table-of-content)

```scala
/*
Return the result of inserting op between consecutive elements of this collection, going right to left 
with the start value z on the right: op(x,,1,,, op(x,,2,,, ... op(x,,n,,, z)...)) where x,,1,,, ..., x,,n,, 
are the elements of this collection. Returns z if this collection is empty.
 */
Seq(1, 2, 3).foldRight(1)(_ + _) // 7
Seq(1, 2, 3).foldRight(Seq.empty[String]) { case (elt, acc) => acc :+ s"value $elt" } // Seq("value 3", "value 2", "value 1")
```

[**.groupBy**](#table-of-content)

```scala
/*
Return a Map from keys to iterable collections such that the following invariant holds:
                 (xs groupBy f)(k) = xs filter (x => f(x) == k)
That is, every key k is bound to a iterable collection of those elements x for which f(x) equals k.
 */
Seq(1, 2, 3).groupBy(_ % 2) // Map[Int,Seq[Int]] = HashMap(0 -> List(2), 1 -> List(1, 3))
Seq(1, 2, 3).groupBy(_ % 2 == 0) // Map[Boolean,Seq[Int]] = HashMap(false -> List(1, 3), true -> List(2))
```

[**.groupMapReduce**](#table-of-content)

```scala
/*
Partitions this iterable collection into a Map according to a discriminator function key. All the values that 
have the same discriminator are then transformed by the f function and then reduced into a single value with 
the reduce function.
It is equivalent to groupBy(key).mapValues(_.map(f).reduce(reduce)), but more efficient.

   def occurrences[A](as: Seq[A]): Map[A, Int] =
     as.groupMapReduce(identity)(_ => 1)(_ + _)
 
Note: Even when applied to a view or a lazy collection it will always force the elements.
 */
Seq(1, 2, 3).groupMapReduce(_ % 2)(_ + 1)(_ + _) // Map[Int,Int] = Map(0 -> 3, 1 -> 6)
```

[**.reduce**](#table-of-content)

```scala
// Return the result of applying reduce operator op between all the elements if the collection is non empty.
Seq(1, 2, 3).reduce(_ - _) // -4
Seq(1, 2, 3).reduce(_ * _) // 6 same as Seq(1, 2, 3).product
```

[**.reduceLeft**](#table-of-content)

```scala
/*
Return the result of inserting op between consecutive elements of this collection, going left to right: 
op( op( ... op(x,,1,,, x,,2,,) ..., x,,n-1,,), x,,n,,) where x,,1,,, ..., x,,n,, are the elements of this collection.
 */
Seq(1, 2, 3).reduceLeft(_ - _) // -4
Seq(1, 2, 3).reduceLeft(_ * _) // 6 same as Seq(1, 2, 3).product
```

[**.reduceRight**](#table-of-content)

```scala
/*
Return the result of inserting op between consecutive elements of this collection, going right to left: 
op(x,,1,,, op(x,,2,,, ..., op(x,,n-1,,, x,,n,,)...)) where x,,1,,, ..., x,,n,, are the elements of this collection.
 */
Seq(1, 2, 3).reduceRight(_ - _) // 2
Seq(1, 2, 3).reduceRight(_ * _) // 6 same as Seq(1, 2, 3).product
```

[**.sliding**](#table-of-content)

```scala
/*
Return an iterator producing iterable collections of size size, except the last element 
(which may be the only element) will be smaller if there are fewer than size elements remaining to be grouped.
 */
Seq(1, 2, 3, 4, 5).sliding(2, 2) // Iterator(List(1, 2), List(3, 4), List(5))
Seq(1, 2, 3, 4, 5).sliding(2, 3) // Iterator(List(1, 2), List(4, 5))
```

### I want to do something else

[**.flatMap**](#table-of-content)

```scala
/*
Return a new collection resulting from applying the given collection-valued function f to each element 
of this collection and concatenating the results.
 */
Seq(1, 2, 3, 4, 5).flatMap(i => Seq(i)) // Seq(1, 2, 3, 4, 5)
Seq(1, 2, 3, 4, 5).flatMap(i => Seq(i, i + 1)) //  Seq(1, 2, 2, 3, 3, 4, 4, 5, 5, 6)
Seq(1, 2, 3, 4, 5).flatMap(i => Vector(i, i + 1)) // Seq(1, 2, 2, 3, 3, 4, 4, 5, 5, 6)
Seq(Some(1), None, Some(3)).flatMap(i => i) // Seq(1, 3) same as Seq(Some(1), None, Some(3)).flatten
```

[**.patch**](#table-of-content)

```scala
/*
Return a new sequence consisting of all elements of this sequence except that replaced elements 
starting from `from` are replaced by all the elements of `other`.
 */
Seq(1, 2, 3, 4, 5).patch(2, Seq(10, 11), 0) // Seq(1, 2, 10, 11, 3, 4, 5) starting from index 2 remove 0 element and insert Seq(10, 11) at this position
Seq(1, 2, 3, 4, 5).patch(2, Seq(10, 11), 1) // Seq(1, 2, 10, 11, 4, 5) starting from index 2 remove 1 element and insert Seq(10, 11) at this position
Seq(1, 2, 3, 4, 5).patch(2, Seq(10, 11), 100) // Seq(1, 2, 10, 11) starting from index 2 remove 100 elements and insert Seq(10, 11) at this position
```

[**.scan**](#table-of-content)

```scala
// Return a new iterable collection containing the prefix scan of the elements in this iterable collection
Seq(1, 2, 3, 4, 5).scan(0)(_ + _) // Seq(0, 1, 3, 6, 10, 15)
Seq(1, 2, 3, 4, 5).scan(1)(_ + _) // Seq(1, 2, 4, 7, 11, 16)
Seq(1, 2, 3, 4, 5).scan(2)(_ - _) // Seq(1, 0, -2, -5, -9, -14)
```

[**.unzip**](#table-of-content)

```scala
/*
Return a pair of iterable collections, containing the first, respectively second half of each 
element pair of this iterable collection.
 */
Seq((1, 2), (3, 4)).unzip // (Seq(1, 3), Seq(2, 4))
```

[**.unzip3**](#table-of-content)

```scala
/*
Return a triple of iterable collections, containing the first, second, respectively third member of each 
element triple of this iterable collection.
 */
Seq((1, 2, 3), (4, 5, 6)).unzip3 // (Seq(1, 4), Seq(2, 5), Seq(3, 6))
```

[**.zip**](#table-of-content)

```scala
/*
Return a new iterable collection containing pairs consisting of corresponding elements of this iterable collection 
and that. The length of the returned collection is the minimum of the lengths of this iterable collection and that.
 */
Seq(1, 2, 3).zip(Seq("a", "b", "c")) // Seq((1, "a"), (2, "b"), (3, "c"))
Seq(1, 2).zip(Seq("a", "b", "c")) // Seq((1, "a"), (2, "b"))
Seq(1, 2, 3, 4).zip(Seq("a", "b", "c")) // Seq((1, "a"), (2, "b"), (3, "c"))
```

[**.zipAll**](#table-of-content)

```scala
/*
Return a new collection of type That containing pairs consisting of corresponding elements of this iterable 
collection and that. The length of the returned collection is the maximum of the lengths of this iterable collection 
and that. If this iterable collection is shorter than that, thisElem values are used to pad the result. 
If that is shorter than this iterable collection, thatElem values are used to pad the result.
 */
Seq("1", "2", "3").zipAll(Seq("a", "b", "c"), "firstCol", "secondCol") // Seq(("1", "a"), ("2", "b"), ("3", "c"))
Seq("1", "2").zipAll(Seq("a", "b", "c"), "firstCol", "secondCol") // Seq(("1", "a"), ("2", "b"), ("firstCol", "c")
Seq("1", "2", "3", "4").zipAll(Seq("a", "b", "c"), "firstCol", "secondCol") // Seq(("1", "a"), ("2", "b"), ("3", "c"), ("4", "secondCol"))
```

[**.zipWithIndex**](#table-of-content)

```scala
/*
Return a triple of iterable collections, containing the first, second, respectively third member of each 
element triple of this iterable collection.
 */
Seq("This", "is", "how", "to", "access", "the", "array", "index").zipWithIndex // Seq(("This", 0), ("is", 1), ("how", 2), ("to", 3), ("access", 4), ("the", 5), ("array", 6), ("index", 7))
```