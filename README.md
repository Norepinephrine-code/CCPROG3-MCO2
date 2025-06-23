<h1 align="center">CCPROG3 MCO1</h1>

<p align="center"><em>A simple console implementation of <strong>Plants vs Zombies</strong> for CCPROG3 by Ramos & Vergara.</em></p> 
<p align="center"><em>VERSION CONTROL: 8:30 PM June 23, 2025</em></p> 

<h2>Important Logics and Parameters</h2>

<ol>
<li><strong>Board Setup</strong>
  <ul>
    <li>Grid size: <strong>5 rows x 9 columns</strong></li>
    <li>Column 0 represents the house; plants may be placed from columns 1 to 8</li>
    <li>Zombies spawn at the last column (8)</li>
    <li>Indexes displayed to user is Index-1</li>
  </ul>
</li>

<li><strong>Game Flow</strong>
  <ul>
    <li>The game runs for <strong>180 ticks</strong></li>
    <li>Each tick drops sun, spawns zombies, processes sunflower generation, allows plant placement, resolves plant attacks and cherrybomb explosions, moves zombies, and checks if any reach the house</li>
  </ul>
</li>

<li><strong>Sun Generation</strong>
  <ul>
    <li>Every 5 ticks a global drop adds 25 sun</li>
    <li>Each Sunflower produces 50 sun every 2 ticks</li>
    <li>Player starts with 150 sun</li>
  </ul>
</li>

<li><strong>Plant Parameters</strong>
  <ul>
    <li><strong>Sunflower</strong>: cost 50, health 40, generates sun</li>
    <li><strong>Peashooter</strong>: cost 100, health 50, range 9, damage 15 long-range / 20 close-range, fires once every 2 ticks</li>
    <li><strong>Cherrybomb</strong>: cost 150, health 1000, explodes after 3 ticks dealing 1800 damage in a 3x3 area</li>
  </ul>
</li>

<li><strong>Zombie Types</strong>
  <ul>
    <li>NormalZombie: health 70, speed 4, damage 10</li>
    <li>ConeheadZombie: health 70, speed 2</li>
    <li>FlagZombie: health 70, speed 3</li>
  </ul>
</li>

<li><strong>Zombie Wave Pattern</strong>
  <ul>
    <li>Ticks 30–80: spawn every 10 ticks</li>
    <li>Ticks 81–140: spawn every 5 ticks</li>
    <li>Ticks 141–170: spawn every 3 ticks</li>
    <li>Ticks 171–180: spawn each tick</li>
  </ul>
</li>

<li><strong>Win/Loss Conditions</strong>
  <ul>
    <li>Plants win if no zombie reaches column 0 by tick 180</li>
    <li>If any zombie enters the house column, zombies win immediately</li>
  </ul>
</li>
</ol>

