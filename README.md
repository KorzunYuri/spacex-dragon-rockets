# spacex-dragon-rockets

A simple repository allowing you to launch SpaceX missions.
The repository is represented by a few classes described below:

## Rocket
A rocket is a... well, a rocket! It can be assigned to one mission at a time and can have the following statuses:
  * **ON_GROUND** – initial status, where the rocket is not assigned to any mission.
    A rocket returns to this state after its mission ends.
  * **IN_SPACE** – the rocket has been assigned to a mission. **Applies automatically when mission starts**.
  * **IN_REPAIR** – the rocket is due to repair. Implies “Pending” status of the mission while on the ground.

## Mission
Can have 0 to N rockets assigned to it. Can have statuses:
  * **SCHEDULED** – initial status, where no rockets are assigned
  * **PENDING** – at least one rocket is assigned and one or more assigned rockets are in repair
  * **IN_PROGRESS** – at least one rocket is assigned and none of them is in repair
  * **ENDED** – the final stage of the mission, at this point rockets should not be assigned
     anymore to a mission (i.e. mission is "archived")

## MissionControl
MissionControl is a "command center" for centralized mission management. 
It provides centralized methods for creating, managing, and querying missions and rockets.

## MissionControlInfoProducer
An interface for classes that extract any relevant information from MissionControl. The library has a default implementation
that returns a list of strings representing missions and rockets info.

## MissionConfig & RocketConfig
Building rockets requires some discipline, doesn't it? The library applies some strictness to objects creation.
Use configuration classes to make sure you haven't missed any important parameters when creating your rocket!


# Usage

```
// create a MissionControl once
MissionControl missionControl = new MissionControl();

// create a Mission
MissionConfig missionConfig = MissionConfig.builder().name("Heading Mars").build();
Mission mission = missionControl.createMission(missionConfig);

// create a Rocket
RocketConfig rocketConfig = RocketConfig.builder().name("My first rocket").build();
Rocket rocket = missionControl.createRocket(rocketConfig);

// assign
missionControl.assignRocketToMission(rocket, mission);

// go to space!
missionControl.changeMissionStatus(mission, MissionStatus.IN_PROGRESS);
```

## Automated behaviour
When Mission or Rocket changes its status, the related instance changes respectively (e.g. rocket going to IN_REPAIR status
forces mission to go to PENDING status). Other than that, you are free to apply any valid statuses to rockets and missions
as long as they make sense. However, the library protects you from applying some invalid state transitions
(e.g. you cannot repair a rocket when it's in space, or you cannot send it to space when the mission hasn't started).
_**The list of valid transitions is up to come as some requirements are still being clarified.**_

Please note that Missions and Rockets can be created independently of MissionControl, but
MissionControl recognizes and gives you ability to operate **only with those instances it created**.
To sum up - you can rule Missions and Rockets yourself, but it's more fun to do it via MissionControl.


# Reactive version

Reactive version of the library is on demo stage and is represented by MissionControlReactive class 
from**spacex-rockets-reactive** module.
To save time, the demo reused domain model from syncronous-approach module. To make it fully reactive, 
some adjustments must be applied to the model classes.


# Unresolved questions

* it is not clear from the requirements, whether mission should start automatically when assigned a rocket. For safety,
the autostart is disabled until it's clear.
* How to answer a request to set a rocket to IN_REPAIR state when in space? 
Should the mission fail, or should the command be denied?
