# Roadmap

All notable planned changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0]

### Added

- Initial set of constraints: AvailabilityConstraint, AllInConstraint, PreferConstraint, SlotsConstraint,
  TimeTableConstraint
- Initial solver, MatchingSolver, supporting SlotsConstraint, AllInConstraint, AvailabilityConstraint

## [0.1.1]

### Added

- MatchingSolver supports PreferConstraint

### Changed

- Better documentation for solver and constraints
- MatchingSolver performance improved by using RangeSet