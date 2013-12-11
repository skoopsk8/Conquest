# Conquest
## What is conquest
Conquest is a Java school project. We choose to make a very simple game around the library LWJGL. All the game graphics are openGL primitives. The game is designed to be played with only one button.

The main concept is coming directly from the old Risk or more recently Europa Universalis. Your civilization must expand its territory, you must conquer enemy land and claim states!
![Alt_text](https://raw.github.com/Lord-Nazdar/Conquest/master/images/board.png)
## User Interface
### States
The world is divided into small states, each states have its own productivity and loyalty.

![Alt_text](https://raw.github.com/Lord-Nazdar/Conquest/master/images/state.png)

As you can see on the picture above, there is four types of loyalty.

1. The first color represent the unreachable land, it could be a desert, a sea or a mountain.
2. The second one is the unaligned. These states are, for the moment, loyal to none civilizations.
3. The green is the green civilization
4. The yellow is the yellow civilization
5. The red is the barbarian. They act randomly every turn attacking civilizations. They don't target the unaligned states.
6. The blue is the player civilization. 

In addition to its loyalty a state also have a productivity value. This value represent the ability for a state to help the main civilization in its conquest madness.

### Units
As long as you have at least one state, your are able to produce units. These units are generated automaticly every turn based on the productivity level of the state. You can do two things with your units.

* Combat
* Conquer

Each state cannot host more than two units at a time. Each units can only move one time each turn.

![Alt_text](https://raw.github.com/Lord-Nazdar/Conquest/master/images/unit.png)

As we can see on this example, there is five units on the board. Because of the two units in the bottom left state we will not be abble to move other units to this state.

## Game Mechanics
### Turn
Since we are not in a turn based strategy game, the game is not waiting for you. You must act in real time. However the map is really big and in end game you could end up with too much units. Therfore you can rely on the active pause. By simply pressing the space bar you completly stop the game. You can check the progress of the turn by looking to progress bar in the bottom of the screen

The time unit is the second, this is why every second you have something to do. However all the unit production is based on the state productivity value, thus a state with a productivity value of four will needs four seconds to produce a unit. A unit can only make one action per turn.

### Move
You can move your units by simply clicking on them and clicking on the desired moving location. The unit can only move from one state at a time each turn.

### Combat
To engage a combat you have to move a unit on a state occupied by an enemy unit. When your unit is arrived the combat is engaged automatically. The outcome of the battle is calculated accordingly to the productivity level of the civilization and some territorial factor.

In enemy territory the equation is:

> (Enemy productivity)*2 < (player productivity)

In neutral territory the equation is:

> (Enemy productivity) < (player productivity)

In your territory the equation is:

> (Enemy productivity) < (player productivity)*2

If the the result is true you will win the fight and the enemy unit will be destroyed.
However if there is two units on the state and the attacker wins the fight, his unit will be moved back to its previous location.

### Conquest
To claim a state you must have a unit standing on it.

![Alt_text](https://raw.github.com/Lord-Nazdar/Conquest/master/images/capture.png)

The capture time is equal to the productivity level of the state.

## End game
### Victory
You are victorious in your world conquest as soon as you claimed all the other civilizations states.
### Defeat
You are defeated as soon as you lost all your states.
