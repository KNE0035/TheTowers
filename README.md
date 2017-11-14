# TheTowers
Hra na styl tower defense kde hráč staví věže proti ai jednotkám a zároven posílá svoje jednotky proti ai, která reaguje stavbou vlastní obrany.

# Popis
Hra bude obsahovat více typů jednotek a věží s jednoduchým armor systémem (Různé typy věží jsou dobré proti určitým jednotkám.). Hra bude ukládat vaše nejlepší score. S postupem času se bude hra stále ztěžovat (ai bude posílat čím dál více jednotek za jednotku času). Jednotky se budou pohybovat různou rychlostí a můžou se blokovat. Ten kdo první prorazí x jednotkama na druhou stranu soupeře vyhrál. Hra bude obsahovat zvuky. Bude se využívat 2d canvas (SurfaceView)

# Jednotky
Každá jednotka bude mít svůj armor type, který jí bude chránit proti určitým projektilům, také bude mít počet hit bodů a svoji rychlost.

Tank |  Quad bike | Hardened tank | Behemoth
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
 ![static_tank](https://user-images.githubusercontent.com/32388847/32766298-9bcc0694-c90e-11e7-85e9-c3ebf4267218.png)   | ![static_quad_bike](https://user-images.githubusercontent.com/32388847/32766292-991db2d0-c90e-11e7-8cc8-cabe675d89fb.png) | ![static_hardened_tank](https://user-images.githubusercontent.com/32388847/32766284-91128ac0-c90e-11e7-9e2f-ae3cc74fc0a2.png) | ![static_behemoth](https://user-images.githubusercontent.com/32388847/32766277-88f99810-c90e-11e7-9242-c2e1271a93bf.png)
 
 # Obranné věže
 Každá věž bude mít svůj svoje projektily, které budou efektivní jen na určité typy armoru, také bude mít určen poškození a svůj radius dostřelu.
 
Cannon |  Machine gun | Missile tower| Photon cannon
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
![static_cannon](https://user-images.githubusercontent.com/32388847/32766788-b9c40dd4-c910-11e7-92ae-1aaa064781bf.png)  | ![static_machine_gun](https://user-images.githubusercontent.com/32388847/32766792-bc2b5b04-c910-11e7-9fc7-54118e24dbf2.png) | ![static_missile_tower](https://user-images.githubusercontent.com/32388847/32766794-bd67028e-c910-11e7-8159-5c0949c3bce0.png) | ![static_photon_cannon](https://user-images.githubusercontent.com/32388847/32766796-be84138c-c910-11e7-909e-6942018968c9.png)

 # Armor system
 Jak již bylo zmíněno jednotky mají svůj armor type a věže projektily, které jsou efektivní jen na určité typy armoru. Pokud projektil,  který je efektivní na daný armor tento armor zasáhne, projeví se to tak, že se poškození věže multiplikuje o nějakou konstantu (např. *2).
 Může se ale taky stát, že projektil trefí armor, který je proti němu odolný, pak se poškození děli o nějakou konstantu (např. /2)

 # Obrázky ze hry
 ![the_towers_game1](https://user-images.githubusercontent.com/32388847/32781445-61974222-c945-11e7-9717-360035ef43f0.PNG)
 
 List jednotek |  List věží
:-------------------------:|:-------------------------:
![the_towers_game2](https://user-images.githubusercontent.com/32388847/32781492-8931db3a-c945-11e7-9e6d-ecca4a2df983.PNG)  |  ![the_towers_game3](https://user-images.githubusercontent.com/32388847/32781712-51b15d6a-c946-11e7-87ee-0ea64bbf2e81.PNG)

 Menu |  Nejlepší score
:-------------------------:|:-------------------------:
![the_towers_game4](https://user-images.githubusercontent.com/32388847/32781869-cf3e925c-c946-11e7-9576-6f3dcbe888aa.PNG)  |  ![the_towers_game5](https://user-images.githubusercontent.com/32388847/32781935-03aafe72-c947-11e7-9bdd-d51f0110b8ed.PNG)

  

