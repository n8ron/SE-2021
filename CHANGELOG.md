# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0]

### Added 

- Support for UserAvailabilityConstraint in MatchingSolver

### Changed

- Better documentation for solver and constraints
- Constraints Renaming and interface changing
- MatchingSolver performance improved by using RangeSet


### Fixed

- Remove intersecting of slots constraints because it's undefined

## [0.1.0]

### Added

- Initial set of constraints: AvailabilityConstraint, AllInConstraint, PreferConstraint, SlotsConstraint,
  TimeTableConstraint
- Initial solver, MatchingSolver, supporting SlotsConstraint, AllInConstraint
