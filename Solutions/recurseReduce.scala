@annotation.tailrec
def reduce[A, B](start: A, iterable: Iterable[B], func: (A, B) => (A)): A = {
  if (iterable.size == 0) {
    return start;
  } else {
    return reduce(func(start, iterable.head), iterable.tail, func)
  }
}
