# Entity Locker

Entity locker provides a possibility to get exclusive access to the entity.

For example,
```
EntityLocker<Integer> locker = DefaultEntityLocker.<Integer>builder()
                .build();
Integer entityId = 1;
locker.lock(id1);
try {
    ... 
    // do operation which needs exclusive access
    ...
} finally {
    locker.unlock(id1)
}
```
"Exclusive access" means that if `locker.lock(id)` succeeded in one thread, then another thread, which called `locker.lock(id)` for the same id, would wait until the former release it.

Entity Locker provides following features:
- reentrancy
- lock method with timeout
- protection from deadlock
- global locking

#### !!! IMPORTANT !!!
This project is considered to be an exercise. Nevertheless, feel free to reach me out if you have any questions.

## Reentrancy

```
DefaultEntityLocker.<Integer>builder()
                // true/false - turn on/off reentrancy.
                // withReentrancy(true) == withReentrancyHandler(new DefaultReentrancyHandler<>())
                .withReentrancy(false) 
                // turn on reentrancy handling and use provided handler
                .withReentrancyHandler(reentrancyHandler)
                .build();
```

## Protection from deadlock
There is a possibility to detect some cases, when deadlock takes place.

Example of detectable deadlock:
1. thread 1 (T1) successfully locks entity 1 (E1)
2. thread 2 (T2) successfully locks entity 2 (E2)
3. T1 tries to lock E2
4. T2 tries to lock E1

Within T1 or/and T2 will be raised an DeadlockDetectedException.

Current implementation doesn't guarantee that it will happen within T1 or T2 or both of them, but that there definitely will be DeadlockDetectedException.

## Global locking
There is a possibility to acquire global lock, which prevents all other threads from acquiring any of entities until global lock is released.

Acquiring of global lock means that all other threads doesn't have any entities locked at the moment. They could try to lock, but will need to wait for release of global lock.
