# SmallStepTrack
##### A simple execution track generator for the _While_ language, defined with Small Step operational semantics, written in Java 8 :coffee:.

Download the [.jar](https://github.com/ParsleyJ/SmallStepTrack/raw/master/out/artifacts/SmallStepTrack_jar/SmallStepTrack.jar)

Launch it with `java -jar SmallStepTrack.jar`

This program prints the execution track of a factorial program example written in the _While_ language, with `x` set to `3` in the Store in the initial configuration.

```java
  y := x; a := 1;
  while y > 0 do
    a := a * y;
    y := y - 1
```

### How it works

In the main, the program is already structured in this way:

```java
String sName = "S"; //the name of the IntegerStore, used by Variable and Assignment classes to access the store.
Program program = new Program(
    "Factorial",
    new SequentialComposition(
        new SequentialComposition(
            new Assignment(sName, new Variable(sName, "y"),
                new Variable(sName, "x")), // y := x;
            new Assignment(sName, new Variable(sName, "a"),
                new Numeral(1)) // a := 1;
        ),
        new WhileCommand(new GreaterIntegerComparison(new Variable(sName, "y"), new Numeral(0)), // while y > 0 do
            new SequentialComposition(
                new Assignment(sName, new Variable(sName, "a"),
                    new Multiplication(new Variable(sName, "a"), new Variable(sName, "y"))), // a := a * y;
                new Assignment(sName, new Variable(sName, "y"),
                    new Subtraction(new Variable(sName, "y"), new Numeral(1))) // y := y - 1
            )
        )
    )
);
```

Then the store is initialized and the execution starts.

```java
// creates a new store
IntegerStore store = new IntegerStore(sName);
// writes 3 to x in the store
store.write("x", 3);

//starts the execution
executeProgram(program, store);
```

During execution, `program.step(configuration)` is called until it returns true, which means that the program became `skip`. 
After each call of step() the state of the execution is printed to stdout.
```java
/**
 * Executes the program and prints the track of execution and the configuration state at each step.
 * @param program the program
 * @param configurationElements the configuration elements
 */
public static void executeProgram(Program program, ConfigurationElement... configurationElements){
    Configuration configuration = new Configuration(configurationElements);
    printState(program, configuration);
    while(!program.step(configuration)){
        printSepAndState(program, configuration);
    }
}
```

Each one of the classes SequentialComposition, Assignment, Variable, Numeral, etc... implements a `step(Configuration c)` method: 
the meaning of this is similar to the "_makes a step of computation_" concept of Small Step semantics.

The actual Small Step semantics of _While_ is "hardcoded" inside each one of these `step(Configuration c)` methods. For example, for
the `SequentialComposition` class:

```java
@Override
public Command step(Configuration c) {
    if (!a.isTerminal()) { //if a can make a step of computation
        Command a1 = a.step(c); // then let it do it

        // then return the new sequential composition made of the modified a command and the original b command.
        return new SequentialComposition(a1, b);

    } else { //else, a is terminal (skip)
        //then return the original b command.
        return b;

    }
}
```

Another example, `Sum.step(Configuration c)`:

```java
@Override
public IntegerExpression step(Configuration c) {
    if (!a.isTerminal()) { // if a can make a step of computation:
        IntegerExpression a1 = a.step(c); // a makes a step
        return new Sum(a1, b); // return a new Sum with a(modified) and b.

    } else if (!b.isTerminal()) { //if a cannot make a step, but b can:
        IntegerExpression b1 = b.step(c); // b makes a step
        return new Sum(a, b1); // return a new Sum with a and b(modified)

    } else { // neither a or b can make a step: they are both numerals!

        //return a new numeral corresponding to the sum of the two numeral operands:
        return new Numeral(a.getIntValue() + b.getIntValue());
    }
}
```


The result of the execution of this jar is something like this:

```
P  =  ((y := x; a := 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { x=3; }
------------------------------
P  =  ((y := 3; a := 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { x=3; }
------------------------------
P  =  ((skip; a := 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { x=3; y=3; }
------------------------------
P  =  (a := 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { x=3; y=3; }
------------------------------
P  =  (skip; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  while y > 0 do (a := a * y; y := y - 1)
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  if y > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  if 3 > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  if true then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  ((a := 1 * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  ((a := 1 * 3; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  ((a := 3; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=1; x=3; y=3; }
------------------------------
P  =  ((skip; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=3; }
------------------------------
P  =  (y := y - 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=3; }
------------------------------
P  =  (y := 3 - 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=3; }
------------------------------
P  =  (y := 2; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=3; }
------------------------------
P  =  (skip; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  while y > 0 do (a := a * y; y := y - 1)
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  if y > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  if 2 > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  if true then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  ((a := 3 * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  ((a := 3 * 2; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  ((a := 6; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=3; x=3; y=2; }
------------------------------
P  =  ((skip; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=2; }
------------------------------
P  =  (y := y - 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=2; }
------------------------------
P  =  (y := 2 - 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=2; }
------------------------------
P  =  (y := 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=2; }
------------------------------
P  =  (skip; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  while y > 0 do (a := a * y; y := y - 1)
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  if y > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  if 1 > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  if true then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  ((a := 6 * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  ((a := 6 * 1; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  ((a := 6; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  ((skip; y := y - 1); while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  (y := y - 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  (y := 1 - 1; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  (y := 0; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=1; }
------------------------------
P  =  (skip; while y > 0 do (a := a * y; y := y - 1))
S  =  { a=6; x=3; y=0; }
------------------------------
P  =  while y > 0 do (a := a * y; y := y - 1)
S  =  { a=6; x=3; y=0; }
------------------------------
P  =  if y > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=6; x=3; y=0; }
------------------------------
P  =  if 0 > 0 then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=6; x=3; y=0; }
------------------------------
P  =  if false then ((a := a * y; y := y - 1); while y > 0 do (a := a * y; y := y - 1)) else skip
S  =  { a=6; x=3; y=0; }
------------------------------
P  =  skip
S  =  { a=6; x=3; y=0; }
```

### State of this project
This is just an example; many things may be missing. If the original factorial program changes, some unhandled exceptions may raise. 
Especially for cases in which the program execution goes into a _stuck_ configuration, which is not handled at the moment. For example, I'm pretty sure that if the program tries to access to an undefined variable in the store, the application crashes with a NullPointerException, because the store relies on a simple HashMap<String, Integer>.
