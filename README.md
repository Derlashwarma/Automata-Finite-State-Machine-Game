## Description

| Current State | Event                                   | Next State | Action/Description                                   |
| --------------|-----------------------------------------|-------------|-----------------------------------------------------|
| START         | Game starts                            | PLAYING     | Initialize the game and player.                     |
| PLAYING       | Enemy spawned                          | PLAYING     | Spawn an enemy and update game state.               |
| PLAYING       | Player health drops to 0 or below     | GAME_OVER   | Transition to game over state and handle game over logic. |
| GAME_OVER     | Game restarted (optional)             | START       | Reset game state and initialize a new game.         |
