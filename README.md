# BBBT - Bonntastischer Bonntakk BBall Timer
![Version](https://img.shields.io/badge/version-1.1-blue.svg)

A specialized basketball possession timer for referees, designed for precise 24/14 second rule enforcement.

## Use Case
Basketball referees need to track possession time during games according to FIBA rules:
- 24 seconds: Standard possession time countdown
- 14 seconds: Special case starting at 10 seconds, counting to 24 seconds

## Core Features
- Dual Timer Modes:
  - 24 second countdown from 0.0 to 24.0
  - 14 second mode starting at 10.0 to 24.0
- Precise Control:
  - One-second adjustments (+/- buttons)
  - Tap timer display to start/stop
- Visual Feedback:
  - Color-coded states:
    - Green: Running
    - Yellow: Stopped
    - Red: Finished
  - Pulse animation during last 5 seconds
  - Vibration feedback for all actions
  - Clear time display with 0.1s precision

## Controls
- Timer Display:
  - Tap to start/stop
- Main Buttons:
  - 24s: Start 24-second countdown
  - 14s: Start at 10 seconds, count to 24
- Adjustment Controls:
  - +1s: Add one second to stopped time
  - -1s: Subtract one second from stopped time
  - Clear: Reset timer to initial state

## Technical Features
- Portrait and landscape support
- Haptic feedback
- High-precision timer (0.1s accuracy)
- State preservation during screen rotation
