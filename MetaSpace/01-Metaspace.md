# Java Metaspace: The Memory Area That Can Crash Your Production JVM

Imagine this production incident:

**2:37 AM — Payment Service is healthy.**

* CPU: normal
* Java Heap: 55% used
* Application: suddenly failing

The error:

```text
java.lang.OutOfMemoryError: Metaspace
```

The first question is usually:

> "But the heap isn't full. How can Java run out of memory?"

That's exactly why **Metaspace** deserves attention.

---

## What is Metaspace?

Before Java 8, JVM class metadata was primarily stored in **PermGen (Permanent Generation)**.

Java 8 removed PermGen and introduced **Metaspace**.

A simplified JVM memory model looks like this:

```text
Java Process
│
├── Java Heap
│   ├── Young Generation
│   └── Old Generation
│
└── Native Memory
    ├── Metaspace
    ├── Compressed Class Space
    ├── Code Cache
    ├── Thread Stacks
    └── Other native allocations
```

So when you configure:

```text
-Xmx2g
```

you are saying:

> "The Java heap can grow up to 2 GB."

You are **not** saying:

> "The JVM process can consume only 2 GB."

Metaspace is outside the normal Java heap and is backed by **native memory**.

---

## What actually goes into Metaspace?

Suppose the JVM loads:

```java
class PaymentService {
    void pay() {}
}
```

The `.class` file isn't simply copied into Metaspace.

The JVM creates internal metadata describing the class:

```text
PaymentService
      │
      ├── Class information
      ├── Fields
      ├── Methods
      ├── Inheritance information
      ├── Interfaces
      ├── Runtime metadata
      └── JVM internal structures
              │
              ▼
          Metaspace
```

Therefore, applications that load a large number of classes can consume significant amounts of Metaspace.

---

# The ClassLoader Is the Real Story

This is one of the most important concepts when troubleshooting Metaspace problems.

A Java class is not identified only by its class name.

Conceptually:

```text
Class Name + Defining ClassLoader
```

defines the identity of a loaded class.

For example:

```text
Employee loaded by Loader-A
        !=
Employee loaded by Loader-B
```

Even though both classes are:

```text
com.example.Employee
```

This becomes particularly important when applications repeatedly create new `ClassLoader` instances.

For example:

```text
Loader-1 → 3,000 classes
Loader-2 → 3,000 classes
Loader-3 → 3,000 classes
Loader-4 → 3,000 classes
...
```

Now imagine an application accidentally retaining references to those old ClassLoaders:

```text
Static Cache
    │
    ├── ClassLoader-1
    │      └── 3,000 classes
    │
    ├── ClassLoader-2
    │      └── 3,000 classes
    │
    ├── ClassLoader-3
    │      └── 3,000 classes
    │
    └── ClassLoader-4
           └── 3,000 classes
```

If those old ClassLoaders are still reachable, the JVM cannot unload the classes associated with them.

Over time:

```text
More ClassLoaders
        ↓
More loaded classes
        ↓
More class metadata
        ↓
Metaspace keeps growing
        ↓
java.lang.OutOfMemoryError: Metaspace
```

This is why a Metaspace OOM can sometimes indicate a **ClassLoader leak**, rather than simply "not enough memory."

---

# How Do You Troubleshoot Metaspace?

Don't immediately increase:

```text
-XX:MaxMetaspaceSize
```

First determine whether:

1. The application genuinely needs more Metaspace
2. The application is loading an unusually large number of classes
3. Classes are being unloaded correctly
4. ClassLoaders are being leaked

---

## 1. Check JVM Configuration

Use:

```bash
jcmd <PID> VM.flags
```

and:

```bash
jcmd <PID> VM.command_line
```

Look for:

```text
-XX:MaxMetaspaceSize
```

Also check whether the JVM is running with a configured Metaspace limit.

---

## 2. Monitor Class Loading

Use:

```bash
jstat -class <PID> 5000
```

This periodically shows class-loading information.

Pay particular attention to:

```text
Loaded
Unloaded
```

For example:

```text
Loaded
18,000
22,000
28,000
35,000
```

If the number of loaded classes keeps increasing over time and doesn't come back down after garbage collection, that is worth investigating.

---

## 3. Inspect ClassLoaders

Use:

```bash
jcmd <PID> VM.classloader_stats
```

This can help identify ClassLoader activity and accumulation.

The key question isn't simply:

> "How many classes are loaded?"

The more important question is:

> "Why are these classes still loaded?"

---

# Four Questions That Help During an Incident

When investigating a Metaspace OOM, ask:

### 1. How much Metaspace is being used?

Is the application approaching its configured Metaspace limit?

### 2. Are loaded classes continuously increasing?

If yes, investigate why classes are being loaded continuously.

### 3. Are ClassLoaders accumulating?

Repeated creation of ClassLoaders can be a strong indicator of a ClassLoader lifecycle problem.

### 4. Does the post-GC Metaspace baseline keep increasing?

This is particularly useful.

If Metaspace grows:

```text
Before GC → 500 MB
After GC  → 350 MB

Before GC → 600 MB
After GC  → 450 MB

Before GC → 700 MB
After GC  → 550 MB
```

the important observation is that the **post-GC baseline is also increasing**.

That suggests the JVM is retaining more class metadata over time.

---

# The Important Takeaway

A JVM can run out of memory even when the Java heap isn't full.

```text
-Xmx2g
   │
   └── Controls Java Heap

        NOT

   ┌─────────────────────────┐
   │ Entire JVM Process      │
   │                         │
   │ Heap                    │
   │ Metaspace               │
   │ Code Cache              │
   │ Thread Stacks           │
   │ Other Native Memory     │
   └─────────────────────────┘
```

So when you see:

```text
java.lang.OutOfMemoryError: Metaspace
```

don't immediately conclude:

> "We need more memory."

Instead, investigate:

```text
Metaspace usage
       ↓
Class loading
       ↓
Class unloading
       ↓
ClassLoader lifecycle
       ↓
Possible ClassLoader leak
```

Understanding the relationship between **classes, ClassLoaders, garbage collection, and Metaspace** can turn a frightening production OOM into a structured investigation.

---

## Useful Commands

```bash
# JVM flags
jcmd <PID> VM.flags

# JVM command line
jcmd <PID> VM.command_line

# Monitor class loading
jstat -class <PID> 5000

# Inspect ClassLoaders
jcmd <PID> VM.classloader_stats
```

**Key lesson:** Heap usage tells you only part of the JVM memory story. Always remember that the JVM process uses memory outside the Java heap.
