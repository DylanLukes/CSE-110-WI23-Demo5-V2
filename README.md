# Demo 5 V2

Herein, please find a modified version of the code from Demo 5.

There are a few things to take note of, which may be helpful for your projects.

## Use of `MediatorLiveData`

**The most important thing to do is to compare `TimeService` and `OrientationService`, and their
respective tests.** TimeService has been refactored to use 
[`MediatorLiveData`](https://developer.android.com/reference/android/arch/lifecycle/MediatorLiveData),
a very helpful implementation of `LiveData` that allows adding and removing sources. You can think
of it as a way to merge multiple other `LiveData`, or in our case **to switch data sources behind
the scenes without the observers needing to know, as shown in `TimeService`**.

**`OrientationService` has NOT been refactored this way.** As a result, you can see in its test
as an example, we need a call to `activity.reobserveOrientation()` to tell the activity _"hey you're
looking at an old data source, please ask the `OrientationService` for a new observable"_. 

This is definitely not SRP! Plus, imagine if _every_ activity that used orientation needed a method 
like this! It would be a huge mess! By using `MediatorLiveData` behind the scenes (it is still 
returned as an _interface_ `LiveData`), our activities never have to re-observe.

**Note:** this use of `MediatorLiveData` is kind of like the Adapter pattern for `LiveData`!

## `InstantTaskExecutorRule` and `CountdownLatch`

All of the tests included here work and should provide a good starting place for your own testing
if you're struggling to do so with `LiveData` involved. Note the added rule:

```
@Rule
public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
```

(It comes from the Gradle dependency `'androidx.arch.core:core-testing:2.1.0'`.)

This rule is needed for `LiveData` to work properly in tests. It tells AndroidX Test that we want
to run everything on the UI thread immediately, waiting before proceeding. You can think of it as 
making `runOnUiThread` _always_ return immediately.

We've included an example in `TaskExecutorRuleDemoTest`. You can see that the test fails without
the rule, and passes with it. This is because the `LiveData` is not updated before the test continues.

This test also shows the use of `CountdownLatch`, which is a helpful little tool for testing things
which might help in the background. You can see that we use it to wait for something to happen
inside an `observe` lambda, and then check the value. But beware, you should _never_ use this 
without a timeout, or you could end up waiting forever!

## Other Stuff

### Layout

This version of the app shows all three sensor values centered (regardless of rotation). You may
find this trick of using a centered container useful somewhere in your project, even if the contents
are different...

### Formatting

This version of the app formats all of the sensor data nicely. You won't need any of this in your
project, but it serves to show some ways you can work with this data. In particular, how you can
work with orientation with some standard library methods.

### Requesting Location Permissions

This version of the app also shows how to request location permissions "properly". The code I've
included is a bit different than what you might find in other places online, but it's more concise.

It lightly uses the Java Streams API to make the code more readable. 
