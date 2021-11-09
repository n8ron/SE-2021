# plameet
[![Validate Kotlin Code](https://github.com/n8ron/SE-2021/actions/workflows/main.yml/badge.svg)](https://github.com/n8ron/SE-2021/actions/workflows/main.yml) 
[![License](https://img.shields.io/github/license/n8ron/SE-2021.svg)](https://img.shields.io/github/license/n8ron/SE-2021)

Plameet is a Kotlin library, which helps to make a schedule that takes into account the constraints imposed by the user

More about this project in the [presentation](https://docs.google.com/presentation/d/1pMcP8d6MpbV968C78p0aTP1jW8-B3RdK3BpL4ce9zPw/edit?usp=sharing)
## Roadmap

Roadmap for this project can be found [here](https://github.com/n8ron/SE-2021/blob/main/ROADMAP.md)

Changelog located [here](https://github.com/n8ron/SE-2021/blob/main/CHANGELOG.md)

## Installation
You can use [jitpack](https://www.jitpack.io) for install this library via gradle:

```
repositories {
    ...
    maven { url = uri("https://www.jitpack.io") } 
}

dependencies {
    ...
    implementation("com.github.n8ron:SE-2021:main-SNAPSHOT")
}
```
## Usage
```kotlin
// list of users
val users = listOf(User(0), User(1), User(2), User(3))

// list of event (every event has some participant)
val events = listOf(
    Event(0, Duration(1), listOf(users[0], users[1])),
    Event(1, Duration(2), listOf(users[2], users[3])),
    Event(2, Duration(2), listOf(users[0], users[2]))
)
// slots for our schedule (that may be rooms in office)
val slots = TimeSlotsConstraint(
    listOf(TimeStamp(1), TimeStamp(5), TimeStamp(9)),
    Duration(2)
)

// every user is available in some time range
val availability = UserAvailabilityConstraint(
    mapOf(
        users[0] to listOf(
            TimeStamp(1).until(TimeStamp(3)),
            TimeStamp(5).until(TimeStamp(7))
        ),
        users[1] to listOf(
            TimeStamp(5).until(TimeStamp(7))
        ),
        users[2] to listOf(
            TimeStamp(1).until(TimeStamp(3)),
            TimeStamp(9).until(TimeStamp(11))
        ),
        users[3] to listOf(
            TimeStamp(9).until(TimeStamp(11))
        )
    )
)
// all events should be in schedule
val eventsRequiredConstraint = EventsRequiredConstraint(events)
// solve with Matching Solver
val solution = MatchingSolver.solve(events, listOf(slots, availability, eventsRequiredConstraint))
/* 
solution:
event0 at the TimeStamp(5)
event1 at the TimeStamp(9)
event2 at the TimeStamp(1)
 */
```

## Contributing
Pull requests are welcome. Thank you for your suggestions!

## Project status
It's a study project in HSE university for SE course. It is currently under development. 

## Authors
- Nikita Abramov (GitHub: n8ron)
- Roman Venediktov (GitHub: e2e4b6b7)
- Valery Golovin (GitHub: vsgol)

## Acknowledgements
Special thanks to our teachers!

- Vladislav Tankov
- Timofey Bryksin

## License
[Eclipse Public License 2.0](https://www.eclipse.org/legal/epl-2.0/)
